package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimarySnackBar(
  text: String = "",
  modifier: Modifier = Modifier.fillMaxWidth(),
  backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
  contentColor: Color = MaterialTheme.colorScheme.background,
  actionText: String? = null,
  onActionClick:() -> Unit
) {
  Snackbar(
    modifier = modifier,
    containerColor = backgroundColor,
    content = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(text = text, color = contentColor,modifier= Modifier.weight(0.8f))
        actionText?.let {
          Text(
            text = it,
            color = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier.clickable { onActionClick() }
          )
        }
      }
    }
  )
}