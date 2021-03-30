package org.aossie.agoraandroid.data.network.responses

import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.data.db.model.Winner

data class ElectionResponse(
  var _id: String? = null,
  val name: String? = null,
  val description: String? = null,
  val electionType: String? = null,
  val creatorName: String ? = null,
  val creatorEmail: String ? = null,
  val startingDate: String? = null,
  val endingDate: String? = null,
  val realtimeResult: String ? = null,
  val votingAlgo: String ? = null,
  val candidates: ArrayList<String> ? = null,
  val ballotVisibility: String ? = null,
  val voterListVisibility: String ? = null,
  val isInvite: String ? = null,
  val isCompleted: String ? = null,
  val isStarted: String ? = null,
  val createdTime: String ? = null,
  val adminLink: String ? = null,
  val inviteCode: String ? = null,
  val ballot: ArrayList<Ballot> ? = null,
  val voterList: ArrayList<VoterList> ? = null,
  val winners: ArrayList<Winner> ? = null,
  val isCounted: String ? = null,
  val noVacancies: String ? = null
)
