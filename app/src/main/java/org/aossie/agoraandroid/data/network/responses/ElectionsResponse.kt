package org.aossie.agoraandroid.data.network.responses

import org.aossie.agoraandroid.data.db.entities.Election

data class ElectionsResponse(
  val elections: List<Election>
)