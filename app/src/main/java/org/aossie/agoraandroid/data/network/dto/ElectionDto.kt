package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionDto(
  @Json(name = "ballot")
  val ballot: List<BallotDto>? = null,
  @Json(name = "ballotVisibility")
  val ballotVisibility: String? = null,
  @Json(name = "candidates")
  val candidates: List<String>? = null,
  @Json(name = "description")
  val description: String? = null,
  @Json(name = "electionType")
  val electionType: String? = null,
  @Json(name = "endingDate")
  val endingDate: String? = null,
  @Json(name = "isInvite")
  val isInvite: Boolean? = null,
  @Json(name = "isRealTime")
  val isRealTime: Boolean? = null,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "noVacancies")
  val noVacancies: Int? = null,
  @Json(name = "startingDate")
  val startingDate: String? = null,
  @Json(name = "voterListVisibility")
  val voterListVisibility: Boolean? = null,
  @Json(name = "votingAlgo")
  val votingAlgo: String? = null,
  var _id: String? = null,

)
