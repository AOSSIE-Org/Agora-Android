package org.aossie.agoraandroid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
  var username: String? = null,
  var email: String? = null,
  var firstName: String? = null,
  var lastName: String? = null,
  var twoFactorAuthentication: Boolean? = null
){
  @PrimaryKey(autoGenerate = false)
  var uid : Int = CURRENT_USER_ID
}