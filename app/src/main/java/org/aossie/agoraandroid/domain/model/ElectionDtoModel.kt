package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.network.dto.BallotDto

data class ElectionDtoModel(
  val ballot: List<BallotDto>? = null,
  val ballotVisibility: String? = null,
  val candidates: List<String>? = null,
  val description: String? = null,
  val electionType: String? = null,
  val endingDate: String? = null,
  val isInvite: Boolean? = null,
  val isRealTime: Boolean? = null,
  val name: String? = null,
  val noVacancies: Int? = null,
  val startingDate: String? = null,
  val voterListVisibility: Boolean? = null,
  val votingAlgo: String? = null,
  var _id: String? = null,
)
