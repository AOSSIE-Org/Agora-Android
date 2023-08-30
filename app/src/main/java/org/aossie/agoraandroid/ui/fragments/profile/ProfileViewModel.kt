package org.aossie.agoraandroid.ui.fragments.profile

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.network.responses.AuthToken
import org.aossie.agoraandroid.domain.model.UpdateUserDtoModel
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.useCases.profile.ProfileUseCases
import org.aossie.agoraandroid.ui.di.models.AppContext
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenDataState
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.ChangePasswordClick
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.DeletePic
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.EnteredConfirmPassword
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.EnteredFirstName
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.EnteredLastName
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.EnteredPassword
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.ToggleTwoFactor
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.UpdateImage
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreenEvent.UpdateProfileClick
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.GetBitmapFromUri
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.aossie.agoraandroid.utilities.toByteArray
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val profileUseCases: ProfileUseCases,
  private val appContext: AppContext
) : ViewModel() {

  val user = profileUseCases.getUser()
  var sessionExpiredListener: SessionExpiredListener? = null

  private val _userModelState = MutableStateFlow (UserModel())
  val userModelState = _userModelState.asStateFlow()

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  private val _profileDataState = MutableStateFlow (ProfileScreenDataState())
  val profileDataState = _profileDataState.asStateFlow()

  private val _uiEvents = MutableSharedFlow<UiEvents>()
  val uiEvents = _uiEvents.asSharedFlow()

  init {
    viewModelScope.launch {
      user.collectLatest {
        _userModelState.value = it
        if(profileDataState.value.avatar==null){
          val bitmap = decodeBitmap(it.avatarURL!!)
          setAvatar(bitmap)
        }
        _profileDataState.value = profileDataState.value.copy(
          firstName = it.firstName?:"",
          lastName = it.lastName?:"",
        )
      }
    }
  }

  fun onEvent(event: ProfileScreenEvent) {
    when(event) {
      ChangePasswordClick -> {
        val pass1 = profileDataState.value.newPassword
        val pass2 = profileDataState.value.confirmPassword

        if(pass1.isEmpty() || pass2.isEmpty()) {
          showMessage(string.password_empty_warn)
        }else if(pass1 != pass2) {
          showMessage(string.password_not_match_warn)
        }else {
          changePassword(pass2)
        }
      }
      is EnteredConfirmPassword -> {
        _profileDataState.value = profileDataState.value.copy(
          confirmPassword = event.confirmPassword
        )
      }
      is EnteredFirstName -> {
        _profileDataState.value = profileDataState.value.copy(
          firstName = event.firstName
        )
      }
      is EnteredLastName -> {
        _profileDataState.value = profileDataState.value.copy(
          lastName = event.lastName
        )
      }
      is EnteredPassword -> {
        _profileDataState.value = profileDataState.value.copy(
          newPassword = event.password
        )
      }
      is ToggleTwoFactor -> {
        showLoading("${if (event.checked) "Enabling" else "Disabling"} two factor authentication...")
        toggleTwoFactorAuth()
      }
      UpdateProfileClick -> {
        val firstName = profileDataState.value.firstName.trim()
        val lastName = profileDataState.value.lastName.trim()

        if(firstName.isNullOrEmpty()) {
          showMessage(string.first_name_empty)
        }else if(lastName.isNullOrEmpty()) {
          showMessage(string.last_name_empty)
        }else {
          val updatedUser = userModelState.value
          updatedUser.let {
            it.firstName = firstName
            it.lastName = lastName
          }
          updateUser(
            updatedUser
          )
        }
      }
      is DeletePic -> {
        deletePic()
      }
      is UpdateImage -> {
        updateImage(event.uri)
      }
    }
  }

  private fun updateImage(uri: Uri) = viewModelScope.launch {
    showLoading("Updating image...")
    try {
      val bitmap = GetBitmapFromUri.handleSamplingAndRotationBitmap(appContext.context, uri)
      val encodedImage = encodeJpegImage(bitmap!!)
      val url = encodedImage!!.toUri()
      changeAvatar(
        url.toString(),
        userModelState.value
      )
    } catch (e: FileNotFoundException) {
      showMessage(string.file_not_found)
    }
  }

  private fun encodeJpegImage(bitmap: Bitmap): String {
    val bytes = bitmap.toByteArray(Bitmap.CompressFormat.JPEG)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
  }

  private fun deletePic() = viewModelScope.launch {
    showLoading("Deleting avatar..")
    val imageUri = Uri.parse(
      ContentResolver.SCHEME_ANDROID_RESOURCE +
        "://" + appContext.context.resources.getResourcePackageName(R.drawable.ic_user1) +
        '/' + appContext.context.resources.getResourceTypeName(R.drawable.ic_user1) + '/' + appContext.context.resources.getResourceEntryName(
        R.drawable.ic_user1
      )
    )
    try {
      val bitmap = GetBitmapFromUri.handleSamplingAndRotationBitmap(appContext.context, imageUri)
      val encodedImage = encodeJpegImage(bitmap!!)
      val url = encodedImage!!.toUri()
      changeAvatar(
        url.toString(),
        userModelState.value
      )
    } catch (e: FileNotFoundException) {
      showMessage(string.file_not_found)
    }
  }

  fun changePassword(password: String) {
    showLoading("Changing password...")
    viewModelScope.launch {
      try {
        profileUseCases.changePassword(password)
        hideLoading()
        showMessage(R.string.password_updated)
        _uiEvents.emit(UiEvents.PasswordChanged)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  fun changeAvatar(
    url: String,
    user: UserModel
  ) {
    viewModelScope.launch {
      try {
        profileUseCases.changeAvatar(url)
        val authResponse = profileUseCases.getUserData()
        Timber.d(authResponse.toString())
        authResponse.let {
          val mUser = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL,
            it.crypto, it.twoFactorAuthentication, user.authToken,
            user.authTokenExpiresOn, user.refreshToken, user.refreshTokenExpiresOn,
            user.trustedDevice
          )
          profileUseCases.saveUser(mUser)
          showMessage(string.profile_updated)
          val bitmap = decodeBitmap(mUser.avatarURL!!)
          setAvatar(bitmap)
        }
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  fun toggleTwoFactorAuth() {
    viewModelScope.launch {
      try {
        profileUseCases.toggleTwoFactorAuth()
        showMessage(R.string.authentication_updated)
        _uiEvents.emit(UiEvents.TwoFactorAuthToggled)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  fun updateUser(
    user: UserModel
  ) {
    showLoading("Updating profile...")
    viewModelScope.launch {
      try {
        val updateUserDtoModel = UpdateUserDtoModel(
          identifier = user.username ?: "",
          email = user.email ?: "",
          firstName = user.firstName,
          lastName = user.lastName,
          avatarURL = user.avatarURL,
          twoFactorAuthentication = user.twoFactorAuthentication,
          authToken = AuthToken(user.authToken, user.authTokenExpiresOn),
          refreshToken = AuthToken(user.refreshToken, user.refreshTokenExpiresOn)
        )
        profileUseCases.updateUser(updateUserDtoModel)
        profileUseCases.saveUser(user)
        showMessage(R.string.user_updated)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  private fun decodeBitmap(encodedBitmap: String): Bitmap {
    val decodedString = Base64.decode(encodedBitmap, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  private fun setAvatar(bitmap: Bitmap) = viewModelScope.launch {
    val bytes = bitmap.toByteArray(Bitmap.CompressFormat.PNG)
    try {
      val avatarFolder = File(appContext.context.cacheDir, "avatars")
      if (!avatarFolder.exists()) {
        avatarFolder.mkdir()
      } else {
        avatarFolder.listFiles()?.forEach { file ->
          file.delete()
        }
      }
      val avatar = File(avatarFolder, "avatar_${System.currentTimeMillis()}.png")
      if (avatar.exists()) {
        avatar.delete()
      }
      val fos = FileOutputStream(avatar)
      fos.write(bytes)
      fos.flush()
      fos.close()
      _profileDataState.value = profileDataState.value.copy(
        avatar = avatar
      )
    } catch (e: IOException) {
      e.printStackTrace()
      showMessage(string.error_loading_image)
    }
  }

  private fun showLoading(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  fun showMessage(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair("",false)
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }

  sealed class UiEvents{
    object PasswordChanged:UiEvents()
    object TwoFactorAuthToggled:UiEvents()
  }
}
