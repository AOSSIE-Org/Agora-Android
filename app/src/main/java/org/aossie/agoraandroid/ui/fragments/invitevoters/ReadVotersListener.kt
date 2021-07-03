package org.aossie.agoraandroid.ui.fragments.invitevoters

import org.aossie.agoraandroid.data.dto.VotersDto

interface ReadVotersListener {

  fun onReadSuccess(list: ArrayList<VotersDto>)
  fun onReadFailure(message: String)
}
