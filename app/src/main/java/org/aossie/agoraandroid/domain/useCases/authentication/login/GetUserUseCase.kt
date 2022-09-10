package org.aossie.agoraandroid.domain.useCases.authentication.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  operator fun invoke(): Flow<UserModel> {
    val response = repository.getUser()
    return response.map {
      mappers.userEntityMapper.mapFromEntity(it)
    }
  }
}
