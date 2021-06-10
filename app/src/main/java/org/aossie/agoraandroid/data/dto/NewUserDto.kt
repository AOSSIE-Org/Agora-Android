package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewUserDto(
  @Json(name = "email")
  val email: String,
  @Json(name = "firstName")
  val firstName: String,
  @Json(name = "identifier")
  val identifier: String,
  @Json(name = "lastName")
  val lastName: String,
  @Json(name = "password")
  val password: String,
  @Json(name = "securityQuestion")
  val securityQuestion: SecurityQuestionDto
)
