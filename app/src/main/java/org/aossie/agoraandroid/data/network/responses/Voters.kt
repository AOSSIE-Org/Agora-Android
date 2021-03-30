package org.aossie.agoraandroid.data.network.responses

import org.aossie.agoraandroid.data.db.model.VoterList

data class Voters(
  val voters: List<VoterList>
)
