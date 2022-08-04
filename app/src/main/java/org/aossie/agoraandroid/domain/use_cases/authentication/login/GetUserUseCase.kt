package org.aossie.agoraandroid.domain.use_cases.authentication.login

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.mappers.UserEntityMapper
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository
){
  private val mapper : UserEntityMapper = UserEntityMapper()
   operator fun invoke(
   ) : LiveData<UserModel> {
    val response : LiveData<User> = repository.getUser()
     return mapper.mapLiveDataFromEntity(response)
  }
}