package org.aossie.agoraandroid.domain.useCases.auth_useCases.signup

import org.aossie.agoraandroid.data.remote.dto.NewUserDto
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
  private val repository: UserRepository
) {
     suspend operator fun invoke (
            userDto: NewUserDto
     ) : String {
       return repository.userSignup(userDto)
     }
}