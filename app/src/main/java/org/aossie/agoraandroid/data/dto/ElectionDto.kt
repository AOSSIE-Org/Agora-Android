package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionDto(
  @Json(name = "ballot")
  val ballot: List<BallotDto>?,
  @Json(name = "ballotVisibility")
  val ballotVisibility: String?,
  @Json(name = "candidates")
  val candidates: List<String>?,
  @Json(name = "description")
  val description: String?,
  @Json(name = "electionType")
  val electionType: String?,
  @Json(name = "endingDate")
  val endingDate: String?,
  @Json(name = "isInvite")
  val isInvite: Boolean?,
  @Json(name = "isRealTime")
  val isRealTime: Boolean?,
  @Json(name = "name")
  val name: String?,
  @Json(name = "noVacancies")
  val noVacancies: Int?,
  @Json(name = "startingDate")
  val startingDate: String?,
  @Json(name = "voterListVisibility")
  val voterListVisibility: Boolean?,
  @Json(name = "votingAlgo")
  val votingAlgo: String?,
  var _id: String? = null,

)
