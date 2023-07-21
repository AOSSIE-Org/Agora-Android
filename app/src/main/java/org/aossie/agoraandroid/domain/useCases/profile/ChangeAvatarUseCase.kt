package org.aossie.agoraandroid.domain.useCases.profile

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class ChangeAvatarUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke(
    url: String
  ): List<String> {
    return repository.changeAvatar(url)
  }
}
