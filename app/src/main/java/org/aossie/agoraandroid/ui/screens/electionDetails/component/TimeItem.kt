package org.aossie.agoraandroid.ui.screens.electionDetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeItem(label: String, text: String) {
  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text(text = "$label-", style = MaterialTheme.typography.labelLarge)
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(imageVector = Rounded.Schedule, contentDescription = "")
      Spacer(modifier = Modifier.width(3.dp))
      Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
  }
}