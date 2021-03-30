package org.aossie.agoraandroid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
  val username: String? = null,
  val email: String? = null,
  var firstName: String? = null,
  var lastName: String? = null,
  val avatarURL: String? = null,
  val crypto: String? = null,
  val twoFactorAuthentication: Boolean? = null,
  var token: String? = null,
  var expiredAt: String? = null,
  val password: String? = null,
  val trustedDevice: String? = null
) {
  @PrimaryKey(autoGenerate = false)
  var uid: Int = CURRENT_USER_ID
}
