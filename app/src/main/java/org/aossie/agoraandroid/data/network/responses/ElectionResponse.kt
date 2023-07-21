package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.network.dto.BallotDto
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.data.network.dto.WinnerDto

@JsonClass(generateAdapter = true)
data class ElectionResponse(
  @Json(name = "adminLink")
  val adminLink: String,
  @Json(name = "ballot")
  val ballot: List<BallotDto>,
  @Json(name = "ballotVisibility")
  val ballotVisibility: String,
  @Json(name = "candidates")
  val candidates: List<String>,
  @Json(name = "createdTime")
  val createdTime: String,
  @Json(name = "creatorEmail")
  val creatorEmail: String,
  @Json(name = "creatorName")
  val creatorName: String,
  @Json(name = "description")
  val description: String,
  @Json(name = "electionType")
  val electionType: String,
  @Json(name = "end")
  val end: String,
  @Json(name = "_id")
  val id: String,
  @Json(name = "inviteCode")
  val inviteCode: String,
  @Json(name = "isCompleted")
  val isCompleted: Boolean,
  @Json(name = "isCounted")
  val isCounted: Boolean,
  @Json(name = "isInvite")
  val isInvite: Boolean,
  @Json(name = "isStarted")
  val isStarted: Boolean,
  @Json(name = "name")
  val name: String,
  @Json(name = "noVacancies")
  val noVacancies: Int,
  @Json(name = "realtimeResult")
  val realtimeResult: Boolean,
  @Json(name = "start")
  val start: String,
  @Json(name = "voterList")
  val voterList: List<VotersDto>,
  @Json(name = "voterListVisibility")
  val voterListVisibility: Boolean,
  @Json(name = "votingAlgo")
  val votingAlgo: String,
  @Json(name = "winners")
  val winners: List<WinnerDto>,

)
