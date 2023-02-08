package org.aossie.agoraandroid.data.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CandidateDto(
  @Json(name = "id")
  val id: String? = null,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "party")
  val party: String? = null
) : Parcelable
