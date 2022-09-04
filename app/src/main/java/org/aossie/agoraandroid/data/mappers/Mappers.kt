package org.aossie.agoraandroid.data.mappers

data class Mappers(
  val authResponseEntityMapper: AuthResponseEntityMapper,
  val ballotDtoEntityMapper: BallotDtoEntityMapper,
  val electionDtoEntityMapper: ElectionDtoEntityMapper,
  val electionEntityMapper: ElectionEntityMapper,
  val loginDtoEntityMapper: LogInDtoEntityMapper,
  val newUserDtoEntityMapper: NewUserDtoEntityMapper,
  val updateUserDtoEntityMapper: UpdateUserDtoEntityMapper,
  val userEntityMapper: UserEntityMapper,
  val verifyOtpDtoEntityMapper: VerifyOtpDtoEntityMapper,
  val votersDtoEntityMapper: VotersDtoEntityMapper,
  val winnerDtoEntityMapper: WinnerDtoEntityMapper
)
