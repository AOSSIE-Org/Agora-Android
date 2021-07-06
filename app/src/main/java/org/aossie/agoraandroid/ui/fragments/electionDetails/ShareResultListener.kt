package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.net.Uri

interface ShareResultListener {
  fun onShareSuccess(uri: Uri)
  fun onExportSuccess(uri: Uri)
  fun onShareExportFailure(message: String)
}
