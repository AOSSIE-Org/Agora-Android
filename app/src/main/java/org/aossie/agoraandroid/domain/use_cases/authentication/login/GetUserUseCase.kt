package org.aossie.agoraandroid.domain.use_cases.authentication.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.mappers.UserEntityMapper
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository
) {
  private val mapper: UserEntityMapper = UserEntityMapper()
  operator fun invoke(): Flow<UserModel> {
    val response: Flow<User> = repository.getUser()
    return flow {
      response.collect {
        if (it != null) {
          val userModel = mapper.mapFromEntity(it)
          emit(userModel)
        }
      }
    }
  }
}
