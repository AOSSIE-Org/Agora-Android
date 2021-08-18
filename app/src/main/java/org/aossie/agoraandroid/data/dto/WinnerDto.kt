package org.aossie.agoraandroid.data.dto

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WinnerDto(
  @Json(name = "candidate")
  val candidate: CandidateDto? = null,
  @Json(name = "score")
  val score: ScoreDto? = null
) : Parcelable {
  constructor(parcel: Parcel) : this()

  override fun writeToParcel(parcel: Parcel, flags: Int) {}

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<WinnerDto> {
    override fun createFromParcel(parcel: Parcel): WinnerDto {
      return WinnerDto(parcel)
    }

    override fun newArray(size: Int): Array<WinnerDto?> {
      return arrayOfNulls(size)
    }
  }
}
