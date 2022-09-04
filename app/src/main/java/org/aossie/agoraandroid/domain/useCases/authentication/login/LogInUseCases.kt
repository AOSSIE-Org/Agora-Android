package org.aossie.agoraandroid.domain.useCases.authentication.login

data class LogInUseCases(
  val faceBookLogIn: FaceBookLogInUseCase,
  val getUser: GetUserUseCase,
  val refreshAccessToken: RefreshAccessTokenUseCase,
  val saveUser: SaveUserUseCase,
  val userLogIn: UserLogInUseCase
)
