package org.aossie.agoraandroid.domain.useCases.updateprofile

import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
  private val repository: UserRepository
){
  suspend operator fun invoke() :AuthResponse{
    return repository.getUserData()
  }
}