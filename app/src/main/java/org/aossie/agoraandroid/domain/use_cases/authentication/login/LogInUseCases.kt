package org.aossie.agoraandroid.domain.use_cases.authentication.login

data class LogInUseCases(
  val faceBookLogInUseCase: FaceBookLogInUseCase,
  val getUserUseCase: GetUserUseCase,
  val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
  val saveUserUseCase: SaveUserUseCase,
  val userLogInUseCase: UserLogInUseCase
)
