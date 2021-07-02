package org.aossie.agoraandroid.ui.fragments.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
import com.squareup.picasso.NetworkPolicy.OFFLINE
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.DialogChangeAvatarBinding
import org.aossie.agoraandroid.databinding.FragmentProfileBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.utilities.GetBitmapFromUri
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInFrag
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.isUrl
import org.aossie.agoraandroid.utilities.loadImage
import org.aossie.agoraandroid.utilities.loadImageFromMemoryNoCache
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toByteArray
import org.aossie.agoraandroid.utilities.toggleIsEnable
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

const val CAMERA_PERMISSION_REQUEST_CODE = 1
const val STORAGE_PERMISSION_REQUEST_CODE = 2
const val CAMERA_INTENT_REQUEST_CODE = 3
const val STORAGE_INTENT_REQUEST_CODE = 4

class ProfileFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : Fragment(), SessionExpiredListener {

  private lateinit var binding: FragmentProfileBinding

  private var mAvatar = MutableLiveData<File>()

  private var encodedImage: String? = null

  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }
  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var mUser: User
//TODO Handle passchange
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel.sessionExpiredListener = this

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
  binding.firstNameTiet.addTextChangedListener(getTextWatcher(1))
    binding.lastNameTiet.addTextChangedListener(getTextWatcher(2))
    binding.newPasswordTiet.addTextChangedListener(getTextWatcher(3))
    binding.confirmPasswordTiet.addTextChangedListener(getTextWatcher(4))
  setObserver()

    binding.updateProfileBtn.setOnClickListener {
      binding.progressBar.show()
      toggleIsEnable()
      if (binding.firstNameTil.error == null && binding.lastNameTil.error == null) {
        hideKeyboardInFrag(this@ProfileFragment)
        val updatedUser = mUser
        updatedUser.let {
          it.firstName = binding.firstNameTiet.text.toString()
          it.lastName = binding.lastNameTiet.text.toString()
        }
        viewModel.updateUser(
          updatedUser
        )
      } else {
        binding.progressBar.hide()
        toggleIsEnable()
      }
    }

    binding.switchWidget.setOnClickListener {
      if (binding.switchWidget.isChecked) {
        AlertDialog.Builder(requireContext())
          .setTitle("Please Confirm")
          .setMessage("Are you sure you want to enable two factor authentication")
          .setCancelable(false)
          .setPositiveButton(android.R.string.ok) { dialog, _ ->
            binding.progressBar.show()
            toggleIsEnable()
            viewModel.toggleTwoFactorAuth()
            dialog.cancel()
          }
          .setNegativeButton(android.R.string.cancel) { dialog, _ ->
            binding.switchWidget.isChecked = false
            dialog.cancel()
          }
          .create()
          .show()
      } else {
        AlertDialog.Builder(requireContext())
          .setTitle("Please Confirm")
          .setMessage("Are you sure you want to disable two factor authentication")
          .setCancelable(false)
          .setPositiveButton(android.R.string.ok) { dialog, _ ->
            binding.progressBar.show()
            toggleIsEnable()
            viewModel.toggleTwoFactorAuth()
            dialog.cancel()
          }
          .setNegativeButton(android.R.string.cancel) { dialog, _ ->
            binding.switchWidget.isChecked = true
            dialog.cancel()
          }
          .create()
          .show()
      }
    }

    binding.fabEditProfilePic.setOnClickListener {
      showChangeProfileDialog()
    }



    binding.changePasswordBtn.setOnClickListener {
      val newPass = binding.newPasswordTiet.text.toString()
      val conPass = binding.confirmPasswordTiet.text.toString()
      when {
        newPass.isEmpty() ->
          binding.newPasswordTil.error = getString(string.password_empty_warn)
        conPass.isEmpty() ->
          binding.confirmPasswordTil.error = getString(string.password_empty_warn)
        newPass != conPass ->
          binding.confirmPasswordTil.error = getString(string.password_not_match_warn)
        else -> updateUIAndChangePassword()
      }
    }




    return binding.root
  }

  private fun updateUIAndChangePassword() {
    binding.progressBar.show()
    toggleIsEnable()
    hideKeyboardInFrag(this@ProfileFragment)
    viewModel.changePassword(binding.newPasswordTiet.text.toString())
  }

  private fun decodeBitmap(encodedBitmap: String): Bitmap {
    val decodedString = Base64.decode(encodedBitmap, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  private fun cacheAndSaveImage(url: String) {
    binding.ivProfilePic.loadImage(url, OFFLINE) {
      binding.ivProfilePic.loadImage(url)
    }
  }
  fun setObserver(){
    mAvatar.observe(
      viewLifecycleOwner,
      Observer {
        binding.ivProfilePic.loadImageFromMemoryNoCache(it)
      }
    )

    viewModel.user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          binding.user = it
          mUser = it
          if (it.avatarURL != null) {
            if (it.avatarURL.isUrl())
              cacheAndSaveImage(it.avatarURL)
            else {
              val bitmap = decodeBitmap(it.avatarURL)
              setAvatarFile(bitmap.toByteArray())
            }
          }
        }
      }
    )

    viewModel.passwordRequestCode.observe(
      viewLifecycleOwner,
      Observer {
        handlePassword(it)
      }
    )
    viewModel.userUpdateResponse.observe(
      viewLifecycleOwner,
      Observer {
        handleUser(it)
      }
    )

    viewModel.toggleTwoFactorAuthResponse.observe(
      viewLifecycleOwner,
      Observer {
        handleTwoFactorAuthentication(it)
        homeViewModel.doLogout()
      }
    )

    viewModel.changeAvatarResponse.observe(
      viewLifecycleOwner,
      Observer {
        handleChangeAvatar(it)
      }
    )
    homeViewModel.getLogoutLiveData.observe(viewLifecycleOwner,{
      when(it.status){
        ResponseUI.Status.ERROR ->{
          binding.progressBar.hide()
          binding.root.snackbar(it.message?:"")
          toggleIsEnable()
        }
        ResponseUI.Status.SUCCESS->{
          binding.progressBar.hide()
          toggleIsEnable()
          if (prefs.getIsFacebookUser()) {
            LoginManager.getInstance()
              .logOut()
          }
          homeViewModel.deleteUserData()
          Navigation.findNavController(binding.root)
            .navigate(
              ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
            )
        }
        ResponseUI.Status.LOADING -> {
          binding.progressBar.show()
          toggleIsEnable()
        }
      }
    })
  }

  private fun showChangeProfileDialog() {
    val dialogView = DialogChangeAvatarBinding.inflate(LayoutInflater.from(context))

    val dialog = AlertDialog.Builder(requireContext())
      .setView(dialogView.root)
      .create()

    dialogView.cameraView.setOnClickListener {
      dialog.cancel()
      if (ActivityCompat.checkSelfPermission(
          requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        openCamera()
      } else {
        askCameraPermission()
      }
    }

    dialogView.galleryView.setOnClickListener {
      dialog.cancel()
      if (ActivityCompat.checkSelfPermission(
          requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        openGallery()
      } else {
        askReadStoragePermission()
      }
    }

    dialog.show()
  }

  private fun askReadStoragePermission() {
    requestPermissions(
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE
    )
  }

  private fun askCameraPermission() {
    requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
  }

  private fun handleChangeAvatar(response: ResponseUI<Any>) = when(response.status) {
   ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.profile_updated))
    }
    ResponseUI.Status.ERROR -> onFailure(response.message?:"")

   else-> onStarted()
  }

  private fun handleUser(response: ResponseUI<Any>) = when(response.status) {
   ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.user_updated))
    }
    ResponseUI.Status.ERROR -> onFailure(response.message?:"")
    else -> onStarted()
  }

  private fun handleTwoFactorAuthentication(response: ResponseUI<Any>) = when(response.status) {
   ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.authentication_updated))
    }
    ResponseUI.Status.ERROR -> onFailure(response.message?:"")
   else -> onStarted()
  }

  private fun handlePassword(response: ResponseUI<Any>) = when (response.status) {
     ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.password_updated))
      loginViewModel.logInRequest(
        mUser.username!!, binding.newPasswordTiet.text.toString(), mUser.trustedDevice
      )
    }
    ResponseUI.Status.ERROR -> onFailure(response.message?:"")

    else->  onStarted()

  }

  private fun getTextWatcher(code: Int): TextWatcher {
    return object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        when (code) {
          1 -> {
            if (s.isNullOrEmpty()) binding.firstNameTil.error = getString(string.first_name_empty)
            else binding.firstNameTil.error = null
          }
          2 -> {
            if (s.isNullOrEmpty()) binding.lastNameTil.error = getString(string.last_name_empty)
            else binding.lastNameTil.error = null
          }
          3 -> {
            when {
              s.isNullOrEmpty() ->
                binding.newPasswordTil.error =
                  getString(string.password_empty_warn)
              else -> binding.newPasswordTil.error = null
            }
            checkNewPasswordAndConfirmPassword(s)
          }
          4 -> {
            when {
              s.isNullOrEmpty() ->
                binding.confirmPasswordTil.error =
                  getString(string.password_empty_warn)
              s.toString() != binding.newPasswordTiet.text.toString() ->
                binding.confirmPasswordTil.error =
                  getString(string.password_not_match_warn)
              else -> binding.confirmPasswordTil.error = null
            }
          }
        }
      }

      override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
      ) {
      }

      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
      ) {
      }
    }
  }

  private fun checkNewPasswordAndConfirmPassword(s: Editable?) {
    if (s.toString() == binding.confirmPasswordTiet.text.toString().trim()
    ) {
      binding.confirmPasswordTil.error = null
    } else {
      if (!binding.confirmPasswordTiet.text.isNullOrEmpty()) {
        binding.confirmPasswordTil.error =
          getString(string.password_not_match_warn)
      }
    }
  }



   fun onStarted() {
    binding.progressBar.show()
    toggleIsEnable()
  }

   fun onFailure(message: String) {
    binding.progressBar.hide()
    binding.root.snackbar(message)
    toggleIsEnable()
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    intentData: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, intentData)
    if (resultCode != Activity.RESULT_OK) return

    if (requestCode == STORAGE_INTENT_REQUEST_CODE && intentData?.data != null) {
      val imageUri = intentData.data ?: return
      try {
        val bitmap = GetBitmapFromUri.handleSamplingAndRotationBitmap(requireContext(), imageUri)
        encodedImage = encodeJpegImage(bitmap!!)
        val url = encodedImage!!.toUri()
        binding.progressBar.show()
        toggleIsEnable()
        viewModel.changeAvatar(
          url.toString(),
          mUser
        )
      } catch (e: FileNotFoundException) {
        binding.root.snackbar("File not found !")
      }
    } else if (requestCode == CAMERA_INTENT_REQUEST_CODE) {
      val bitmap = intentData?.extras?.get("data")
      if (bitmap is Bitmap) {
        encodedImage = encodePngImage(bitmap)
        val url = encodedImage!!.toUri()
        binding.progressBar.show()
        toggleIsEnable()
        viewModel.changeAvatar(
          url.toString(),
          mUser
        )
      }
    }
  }

  private fun openGallery() {
    val galleryIntent = Intent()
    galleryIntent.let {
      it.type = "image/*"
      it.action = Intent.ACTION_GET_CONTENT
    }
    startActivityForResult(galleryIntent, STORAGE_INTENT_REQUEST_CODE)
  }

  private fun openCamera() {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(cameraIntent, CAMERA_INTENT_REQUEST_CODE)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        openGallery()
      } else {
        binding.root.snackbar("Permission denied")
      }
    } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        openCamera()
      } else {
        binding.root.snackbar("Permission denied")
      }
    }
  }

  private fun encodeJpegImage(bitmap: Bitmap): String {
    val bytes = bitmap.toByteArray(Bitmap.CompressFormat.JPEG)
    setAvatarFile(bytes)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
  }

  private fun encodePngImage(bitmap: Bitmap): String {
    val bytes = bitmap.toByteArray(Bitmap.CompressFormat.PNG)
    setAvatarFile(bytes)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
  }

  private fun setAvatarFile(bytes: ByteArray) {
    try {
      val avatar = File(context?.cacheDir, "avatar")
      if (avatar.exists()) {
        avatar.delete()
      }
      val fos = FileOutputStream(avatar)
      fos.write(bytes)
      fos.flush()
      fos.close()
      mAvatar.value = avatar
    } catch (e: IOException) {
      e.printStackTrace()
      binding.root.snackbar("Error while loading the image")
    }
  }

  private fun toggleIsEnable() {
    binding.updateProfileBtn.toggleIsEnable()
    binding.changePasswordBtn.toggleIsEnable()
  }
}
