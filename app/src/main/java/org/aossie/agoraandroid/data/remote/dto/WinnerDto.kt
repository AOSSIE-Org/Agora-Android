package org.aossie.agoraandroid.data.remote.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WinnerDto(
  @Json(name = "candidate")
  val candidate: CandidateDto? = null,
  @Json(name = "score")
  val score: ScoreDto? = null
) : Parcelable
