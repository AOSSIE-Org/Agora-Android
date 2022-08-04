package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.remote.dto.BallotDto
import org.aossie.agoraandroid.data.remote.dto.VotersDto
import org.aossie.agoraandroid.data.remote.dto.WinnerDto

data class ElectionModel(
  val _id: String,
  val name: String? = null,
  val description: String? = null,
  val electionType: String? = null,
  val creatorName: String ? = null,
  val creatorEmail: String ? = null,
  val start: String? = null,
  val end: String? = null,
  val realtimeResult: String ? = null,
  val votingAlgo: String ? = null,
  val candidates: List<String> ? = null,
  val ballotVisibility: String ? = null,
  val voterListVisibility: String ? = null,
  val isInvite: Boolean ? = null,
  val isCompleted: Boolean ? = null,
  val isStarted: Boolean ? = null,
  val createdTime: String ? = null,
  val adminLink: String ? = null,
  val inviteCode: String ? = null,
  val ballot: List<BallotDto> ? = null,
  val voterList: List<VotersDto> ? = null,
  val winners: List<WinnerDto> ? = null,
  val isCounted: String ? = null,
  val noVacancies: String ? = null
)
