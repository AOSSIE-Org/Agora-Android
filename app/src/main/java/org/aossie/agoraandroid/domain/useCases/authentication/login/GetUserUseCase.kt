package org.aossie.agoraandroid.domain.useCases.authentication.login

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  operator fun invoke(): LiveData<UserModel> {
    val response: LiveData<User> = repository.getUser()
    return mappers.userEntityMapper.mapLiveDataFromEntity(response)
  }
}
