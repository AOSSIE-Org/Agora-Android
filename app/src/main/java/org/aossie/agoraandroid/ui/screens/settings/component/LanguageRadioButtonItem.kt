package org.aossie.agoraandroid.ui.screens.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LanguageRadioButtonItem(
  selected: Pair<String, String>,
  onCheckedChange: (Pair<String, String>) -> Unit,
  language: Pair<String, String>
){
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
    Checkbox(checked = selected.second==language.second, onCheckedChange = {
      if(it){
        onCheckedChange(language)
      }else{
        onCheckedChange(language)
      }
    })
    Text(text = language.first,
      style = MaterialTheme.typography.titleLarge,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}
