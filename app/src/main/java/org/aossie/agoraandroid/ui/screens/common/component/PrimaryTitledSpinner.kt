package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryTitledSpinner(
  title:String,
  list:List<String>,
  selectedIndex:Int,
  onItemSelected:(Int) -> Unit,
  backgroundColor: Color = MaterialTheme.colorScheme.background,
  borderColor: Color = MaterialTheme.colorScheme.outline
) {
  Column {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    SpinnerEditField(
      list = list,
      selectedIndex = selectedIndex,
      onItemSelected = onItemSelected,
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = backgroundColor,
      borderColor = borderColor
    )
  }
}