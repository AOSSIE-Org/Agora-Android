package org.aossie.agoraandroid.domain.useCases.updateprofile

import org.aossie.agoraandroid.data.remote.dto.UpdateUserDto
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke(
    updateUserDto: UpdateUserDto
  ) : List<String> {
    return repository.updateUser(updateUserDto)
  }
}