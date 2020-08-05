package org.aossie.agoraandroid.data.network.responses

import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.data.db.model.Winner

data class ElectionResponse(
  var _id : String?= null,
  var name : String?= null,
  var description :String?= null,
  var electionType :String?= null,
  var creatorName : String ?= null,
  var creatorEmail: String ?= null,
  var start: String?= null,
  var end: String?= null,
  var realtimeResult: String ?= null,
  var votingAlgo: String ?= null,
  var candidates: ArrayList<String> ?= null,
  var ballotVisibility: String ?= null,
  var voterListVisibility: String ?= null,
  var isInvite: String ?= null,
  var isCompleted: String ?= null,
  var isStarted: String ?= null,
  var createdTime: String ?= null,
  var adminLink: String ?= null,
  var inviteCode: String ?= null,
  var ballot: ArrayList<Ballot> ?= null,
  var voterList: ArrayList<VoterList> ?= null,
  var winners: ArrayList<Winner> ?= null,
  var isCounted: String ?= null,
  var noVacancies: String ?= null
)