package org.aossie.agoraandroid.data.mappers

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.utilities.EntityMapper

class UserEntityMapper : EntityMapper<User, UserModel> {
  override fun mapFromEntity(entity: User): UserModel {
    return UserModel(
      username = entity.username,
      email = entity.email,
      firstName = entity.firstName,
      lastName = entity.lastName,
      avatarURL = entity.avatarURL,
      crypto = entity.crypto,
      twoFactorAuthentication = entity.twoFactorAuthentication,
      authToken = entity.authToken,
      authTokenExpiresOn = entity.authTokenExpiresOn,
      refreshToken = entity.refreshToken,
      refreshTokenExpiresOn = entity.refreshTokenExpiresOn,
      trustedDevice = entity.trustedDevice
    )
  }

  override fun mapToEntity(domainModel: UserModel): User {
    return User(
      username = domainModel.username,
      email = domainModel.email,
      firstName = domainModel.firstName,
      lastName = domainModel.lastName,
      avatarURL = domainModel.avatarURL,
      crypto = domainModel.crypto,
      twoFactorAuthentication = domainModel.twoFactorAuthentication,
      authToken = domainModel.authToken,
      authTokenExpiresOn = domainModel.authTokenExpiresOn,
      refreshToken = domainModel.refreshToken,
      refreshTokenExpiresOn = domainModel.refreshTokenExpiresOn,
      trustedDevice = domainModel.trustedDevice
    )
  }

  fun mapLiveDataFromEntity(userLivedata: LiveData<User>): LiveData<UserModel> {
    return Transformations.map(
      userLivedata, { mapFromEntity(it) }
    )
  }
}
