package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
  backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
  textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
  text:String,
  strokeColor:Color= MaterialTheme.colorScheme.primaryContainer,
  cornerRadius:Dp=10.dp,
  borderWidth:Dp=2.dp,
  icon : Painter? = null,
  onClick:() -> Unit,
){
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = borderWidth,
        color = strokeColor,
        shape = RoundedCornerShape(
          size = cornerRadius
        )
      )
      .clip(
        shape = RoundedCornerShape(
          size = cornerRadius
        )
      )
      .background(color = backgroundColor)
      .clickable {
        onClick()
      }
      .padding(
        vertical = 15.dp
      )
    ,
    contentAlignment = Alignment.Center,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      icon?.let {
        Image(
          painter = it,
          contentDescription = "Icons",
        )
        Spacer(modifier = Modifier.width(10.dp))
      }
      Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.titleLarge
      )
    }

  }
}
