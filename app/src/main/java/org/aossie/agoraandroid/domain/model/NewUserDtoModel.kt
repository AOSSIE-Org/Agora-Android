package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.network.dto.SecurityQuestionDto

data class NewUserDtoModel(
  val email: String? = null,
  val firstName: String? = null,
  val identifier: String? = null,
  val lastName: String? = null,
  val password: String? = null,
  val securityQuestion: SecurityQuestionDto? = null
)
