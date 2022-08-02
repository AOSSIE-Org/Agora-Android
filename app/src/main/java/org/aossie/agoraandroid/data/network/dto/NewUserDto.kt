package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewUserDto(
  @Json(name = "email")
  val email: String? = null,
  @Json(name = "firstName")
  val firstName: String? = null,
  @Json(name = "identifier")
  val identifier: String? = null,
  @Json(name = "lastName")
  val lastName: String? = null,
  @Json(name = "password")
  val password: String? = null,
  @Json(name = "securityQuestion")
  val securityQuestion: SecurityQuestionDto? = null
)
