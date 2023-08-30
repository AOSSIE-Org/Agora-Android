package org.aossie.agoraandroid.ui.screens.profile

import android.net.Uri

sealed class ProfileScreenEvent{
  data class EnteredFirstName(val firstName:String): ProfileScreenEvent()
  data class EnteredLastName(val lastName:String): ProfileScreenEvent()
  data class EnteredPassword(val password:String): ProfileScreenEvent()
  data class EnteredConfirmPassword(val confirmPassword:String): ProfileScreenEvent()
  object UpdateProfileClick: ProfileScreenEvent()
  object ChangePasswordClick: ProfileScreenEvent()
  data class ToggleTwoFactor(val checked: Boolean): ProfileScreenEvent()
  data class UpdateImage(val uri: Uri,): ProfileScreenEvent()
  object DeletePic: ProfileScreenEvent()

}
