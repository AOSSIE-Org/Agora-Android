package org.aossie.agoraandroid.ui.screens.inviteVoters

import android.content.Context
import org.aossie.agoraandroid.data.network.dto.VotersDto

sealed class InviteVotersScreenEvent{
  data class EnteredVoterName(val voterName: String): InviteVotersScreenEvent()
  data class EnteredVoterEmail(val voterEmail: String): InviteVotersScreenEvent()
  data class DeleteVoterClick(val votersDto: VotersDto): InviteVotersScreenEvent()
  data class OpenCSVFileClick(val path: String, val context: Context) : InviteVotersScreenEvent()
  object InviteVotersClick: InviteVotersScreenEvent()
  object SnackBarActionClick: InviteVotersScreenEvent()
  object AddVoterClick: InviteVotersScreenEvent()
}
