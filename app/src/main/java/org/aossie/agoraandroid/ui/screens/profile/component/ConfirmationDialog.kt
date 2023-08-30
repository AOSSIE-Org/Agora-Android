package org.aossie.agoraandroid.ui.screens.profile.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationDialog(
  showDialog: Boolean,
  enableTwoFactor:Boolean,
  onConfirm: () -> Unit,
  onCancel: () -> Unit
) {
  if (showDialog) {
    AlertDialog(
      onDismissRequest = onCancel,
      title = {
        Text("Please Confirm")
      },
      text = {
        if(enableTwoFactor){
          Text("Are you sure you want to enable two-factor authentication?")
        }else{
          Text("Are you sure you want to disable two factor authentication?")
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onConfirm()
          }
        ) {
          Text("OK")
        }
      },
      dismissButton = {
        Button(
          onClick = {
            onCancel()
          }
        ) {
          Text("Cancel")
        }
      },
      modifier = Modifier.padding(16.dp)
    )
  }
}