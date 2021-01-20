package org.aossie.agoraandroid.data.network.responses

import org.aossie.agoraandroid.data.db.entities.Election

data class ElectionsResponse(
  var elections: List<Election>
)