package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconTextNavigationButton(
  text: String,
  arrowText: String? = null,
  iconStart: Int,
  iconStartTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  iconEnd: @Composable () -> Unit,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .padding(horizontal = 25.dp, vertical = 10.dp)
    ,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(10.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          painter = painterResource(id = iconStart),
          contentDescription = "",
          tint = iconStartTint
        )
      }
      Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
      )
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      arrowText?.let { text ->
        Text(
          text = text,
          style = MaterialTheme.typography.titleMedium
        )
      }
      iconEnd()
    }
  }
}
