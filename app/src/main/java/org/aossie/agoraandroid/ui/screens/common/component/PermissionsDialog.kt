package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionsDialog(
  title:String,
  description:String,
  onDialogDismiss:() -> Unit,
  onDialogConfirm:() -> Unit
) {
  AlertDialog(
    onDismissRequest = onDialogDismiss,
    title = { Text(title, style = MaterialTheme.typography.titleLarge) },
    text = {
      Text(text = description, style = MaterialTheme.typography.bodyMedium)
    },
    confirmButton = {
      Button(onClick = onDialogConfirm) {
        Text("OK")
      }
    },
    dismissButton = {
      Button(onClick = onDialogDismiss) {
        Text("Cancel")
      }
    }
  )
}
