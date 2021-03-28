package org.aossie.agoraandroid.data.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.data.db.model.Winner

@Entity
data class Election(
  @PrimaryKey
  @NonNull
  val _id : String,
  val name : String?= null,
  val description :String?= null,
  val electionType :String?= null,
  val creatorName : String ?= null,
  val creatorEmail: String ?= null,
  val start: String?= null,
  val end: String?= null,
  val realtimeResult: String ?= null,
  val votingAlgo: String ?= null,
  val candidates: ArrayList<String> ?= null,
  val ballotVisibility: String ?= null,
  val voterListVisibility: String ?= null,
  val isInvite: String ?= null,
  val isCompleted: String ?= null,
  val isStarted: String ?= null,
  val createdTime: String ?= null,
  val adminLink: String ?= null,
  val inviteCode: String ?= null,
  val ballot: ArrayList<Ballot> ?= null,
  val voterList: ArrayList<VoterList> ?= null,
  val winners: ArrayList<Winner> ?= null,
  val isCounted: String ?= null,
  val noVacancies: String ?= null
)