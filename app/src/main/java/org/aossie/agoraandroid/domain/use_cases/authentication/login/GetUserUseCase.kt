package org.aossie.agoraandroid.domain.use_cases.authentication.login

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
  private val repository: UserRepository
){
   operator fun invoke(
   ) : LiveData<User> {
    return repository.getUser()
  }
}