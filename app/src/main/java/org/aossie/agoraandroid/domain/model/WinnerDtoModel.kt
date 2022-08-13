package org.aossie.agoraandroid.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.aossie.agoraandroid.data.network.dto.CandidateDto
import org.aossie.agoraandroid.data.network.dto.ScoreDto

@Parcelize
data class WinnerDtoModel(
  val candidate: CandidateDto? = null,
  val score: ScoreDto? = null
) : Parcelable
