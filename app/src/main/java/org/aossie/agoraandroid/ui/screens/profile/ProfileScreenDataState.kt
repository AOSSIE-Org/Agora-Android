package org.aossie.agoraandroid.ui.screens.profile

import java.io.File

data class ProfileScreenDataState(
  val firstName:String = "",
  val lastName:String = "",
  val newPassword:String = "",
  val confirmPassword:String = "",
  val avatar:File? = null
)