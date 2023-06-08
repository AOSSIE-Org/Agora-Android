package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryProgressView(
  text: String = "Loading...",
  modifier: Modifier=Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
  contentColor: Color = MaterialTheme.colorScheme.background
) {
  Snackbar(
    modifier = modifier,
    containerColor = backgroundColor,
    content = {
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(text = text, color = contentColor)
        CircularProgressIndicator(
          modifier = Modifier.size(24.dp),
          color =contentColor,
          strokeWidth = 3.dp
        )
      }
    }
  )
}