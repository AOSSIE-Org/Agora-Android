package org.aossie.agoraandroid.ui.screens.createElection.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CandidateItem(text: String, onDeleteClick: (String) -> Unit) {
  InputChip(
    modifier = Modifier.padding(bottom = 10.dp),
    selected = true,
    label = {
      Text(text = text)
    },
    trailingIcon = {
      IconButton(onClick = {
        onDeleteClick(text)
      }) {
        Icon(imageVector = Rounded.Close, contentDescription = "Close Icon")
      }
    },
    onClick = { })
}