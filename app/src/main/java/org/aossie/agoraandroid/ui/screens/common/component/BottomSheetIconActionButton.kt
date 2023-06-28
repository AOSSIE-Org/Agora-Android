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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetIconActionButton(
  iconStart: ImageVector? = null,
  iconStartPainter: Painter? = null,
  iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  text: String, onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
      .padding(horizontal = 25.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .background(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(8.dp)
        ),
      contentAlignment = Alignment.Center
    ) {
      iconStart?.let {
        Icon(
          modifier = Modifier.size(20.dp),
          imageVector = it,
          tint = iconColor,
          contentDescription = "Bottom sheet icon"
        )
      }
      iconStartPainter?.let {
        Icon(
          modifier = Modifier.size(20.dp),
          painter = it,
          tint = iconColor,
          contentDescription = "Bottom sheet icon"
        )
      }
    }
    Text(
      text = text,
      style = MaterialTheme.typography.titleLarge
    )
  }
}