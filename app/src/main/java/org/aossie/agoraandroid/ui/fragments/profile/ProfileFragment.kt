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
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_change_avatar.view.camera_view
import kotlinx.android.synthetic.main.dialog_change_avatar.view.gallery_view
import kotlinx.android.synthetic.main.fragment_profile.view.fab_edit_profile_pic
import kotlinx.android.synthetic.main.fragment_profile.view.iv_profile_pic
import kotlinx.android.synthetic.main.fragment_profile.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.databinding.FragmentProfileBinding
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.utilities.GetBitmapFromUri
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInFrag
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import timber.log.Timber
import java.io.ByteArrayOutputStream
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
) : Fragment(), AuthListener {

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

  private lateinit var mUser: User

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel.authListener = this

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    viewModel.user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          Timber.d(it.toString())
          binding.user = it
          mUser = it
          if (it.avatarURL != null) {
            val bitmap = decodeBitmap(it.avatarURL)
            encodedImage = encodePngImage(bitmap)
          }
        }
      }
    )
    binding.firstNameTiet.addTextChangedListener(getTextWatcher(1))
    binding.lastNameTiet.addTextChangedListener(getTextWatcher(2))
    binding.newPasswordTiet.addTextChangedListener(getTextWatcher(3))
    binding.confirmPasswordTiet.addTextChangedListener(getTextWatcher(4))

    binding.updateProfileBtn.setOnClickListener {
      binding.root.progress_bar.show()
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
        binding.root.progress_bar.hide()
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
            binding.root.progress_bar.show()
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
            binding.root.progress_bar.show()
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

    binding.root.fab_edit_profile_pic.setOnClickListener {
      showChangeProfileDialog()
    }

    mAvatar.observe(
      viewLifecycleOwner,
      Observer {
        Picasso.get()
          .load(it)
          .placeholder(ContextCompat.getDrawable(requireContext(), drawable.ic_user)!!)
          .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
          .into(binding.root.iv_profile_pic)
      }
    )

    binding.changePasswordBtn.setOnClickListener {
      val newPass = binding.newPasswordTiet.text.toString()
      val conPass = binding.confirmPasswordTiet.text.toString()
      if (binding.newPasswordTil.error == null && binding.confirmPasswordTil.error == null) {
        when {
          newPass.isEmpty() -> binding.newPasswordTil.error = getString(string.password_empty_warn)
          conPass.isEmpty() ->
            binding.confirmPasswordTil.error =
              getString(string.password_empty_warn)
          newPass != conPass ->
            binding.confirmPasswordTil.error =
              getString(string.password_not_match_warn)
          else -> {
            binding.root.progress_bar.show()
            toggleIsEnable()
            hideKeyboardInFrag(this@ProfileFragment)
            viewModel.changePassword(binding.newPasswordTiet.text.toString())
          }
        }
      } else {
        binding.root.progress_bar.hide()
        toggleIsEnable()
      }
    }

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
    return binding.root
  }

  private fun decodeBitmap(encodedBitmap: String): Bitmap {
    val decodedString = Base64.decode(encodedBitmap, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  private fun showChangeProfileDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_change_avatar, null)

    val dialog = AlertDialog.Builder(requireContext())
      .setView(dialogView)
      .create()

    dialogView.camera_view.setOnClickListener {
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

    dialogView.gallery_view.setOnClickListener {
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

  private fun handleChangeAvatar(response: ResponseResult) = when (response) {
    is Success -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.profile_updated))
    }
    is Error -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(response.error.toString())
    }
  }

  private fun handleUser(response: ResponseResult) = when (response) {
    is Success -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.user_updated))
      loginViewModel.logInRequest(mUser.username!!, mUser.password!!, mUser.trustedDevice)
    }
    is Error -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(response.error.toString())
    }
  }

  private fun handleTwoFactorAuthentication(response: ResponseResult) = when (response) {
    is Success -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.authentication_updated))
    }
    is Error -> {
      toggleIsEnable()
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.error.toString())
    }
  }

  private fun handlePassword(response: ResponseResult) = when (response) {
    is Success -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(getString(string.password_updated))
      loginViewModel.logInRequest(
        mUser.username!!, binding.newPasswordTiet.text.toString(), mUser.trustedDevice
      )
    }
    is Error -> {
      binding.root.progress_bar.hide()
      toggleIsEnable()
      binding.root.snackbar(response.error.toString())
    }
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
              s.toString() == mUser.password ->
                binding.newPasswordTil.error =
                  getString(string.password_same_oldpassword_warn)
              else -> binding.newPasswordTil.error = null
            }
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

  override fun onSuccess(message: String?) {
    binding.root.progress_bar.hide()
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

  override fun onStarted() {
    binding.root.progress_bar.show()
    toggleIsEnable()
  }

  override fun onFailure(message: String) {
    binding.root.progress_bar.hide()
    binding.root.snackbar(message)
    toggleIsEnable()
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
        binding.root.progress_bar.show()
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
        binding.root.progress_bar.show()
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
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos)
    val bytes = bos.toByteArray()
    setAvatarFile(bytes)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
  }

  private fun encodePngImage(bitmap: Bitmap): String {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
    val bytes = bos.toByteArray()
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
