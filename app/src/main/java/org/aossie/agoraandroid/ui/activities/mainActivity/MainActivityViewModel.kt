package org.aossie.agoraandroid.ui.activities.mainActivity

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.utilities.Coroutines
import javax.inject.Inject

class MainActivityViewModel
@Inject
constructor(
  val userRepository: UserRepository
): ViewModel() {

  fun deleteUserData() {
    Coroutines.main {
      userRepository.deleteUser()
    }
  }
}