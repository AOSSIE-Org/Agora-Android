package org.aossie.agoraandroid.ui.screens.result

import android.graphics.Bitmap

sealed class ResultScreenEvent {
  object OnBackClick: ResultScreenEvent()
  object OnExportCSVClick: ResultScreenEvent()
  data class OnShareClick(val bitmap: Bitmap): ResultScreenEvent()
}
