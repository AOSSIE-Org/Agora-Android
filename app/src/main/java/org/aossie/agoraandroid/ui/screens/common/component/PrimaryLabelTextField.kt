package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryLabelTextField(
  label: String="",
  value: String,
  modifier: Modifier = Modifier,
  maxLength: Int?=null,
  onValueChange: (String) -> Unit,
  keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
){
  Column {
    Text(
      text = label,
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
      modifier = modifier,
      value = value,
      onValueChange = onValueChange,
      keyboardOptions = keyboardOptions,
      colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline
      )
    )
  }

}