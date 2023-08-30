package org.aossie.agoraandroid.ui.screens.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.aossie.agoraandroid.R

@Composable
fun LanguageUpdateDialog(
    languages: List<Pair<String,String>>,
    onDismissRequest:() -> Unit,
    onConfirmRequest:(Pair<String, String>) -> Unit,
    selectedLang: Pair<String, String>,
) {
  val selected = remember {
    mutableStateOf(selectedLang)
  }
  Dialog(
    onDismissRequest = onDismissRequest,
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = MaterialTheme.shapes.large,
      tonalElevation = AlertDialogDefaults.TonalElevation
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = stringResource(id = R.string.language),
          style = MaterialTheme.typography.headlineMedium
        )
        LazyColumn(
          modifier = Modifier
            .heightIn(max = 400.dp)
            .fillMaxWidth()
        ) {
          itemsIndexed(languages) { index, item ->
            LanguageRadioButtonItem(
              language = item,
              selected = selected.value,
              onCheckedChange = {
                selected.value  = it
              }
            )
          }
        }
        Row(modifier = Modifier.align(Alignment.End)) {
          TextButton(
            onClick = onDismissRequest,
          ) {
            Text("Cancel")
          }
          TextButton(
            onClick = {
              if(selected.value != selectedLang){
                onConfirmRequest(selected.value!!)
              }else{
                onDismissRequest.invoke()
              }
            },
          ) {
            Text("Update")
          }
        }
      }
    }
  }
}