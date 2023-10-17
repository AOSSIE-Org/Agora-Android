package org.aossie.agoraandroid.ui.screens.result.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string

@Composable
fun WinnerCard(winnerName: String) {
  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 15.dp
    ),
  ) {
    Box(modifier = Modifier
      .fillMaxWidth()
      .background(
        brush = Brush.horizontalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
          )
        )
      )
      .padding(20.dp)
    ) {
      Column(
        modifier = Modifier.align(
          Alignment.CenterStart
        )
      ) {
        Text(
          text = winnerName,
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
          text = stringResource(id = string.winner_is),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }
      Image(
        painter = painterResource(id = drawable.agora),
        contentDescription = "",
        modifier = Modifier
          .size(120.dp)
          .align(Alignment.CenterEnd),
      )

    }
  }
}