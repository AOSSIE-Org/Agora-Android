package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryPasswordField(
    label: String="",
    password: String="",
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    onPasswordChange: (String) -> Unit
){

    var passwordVisibility by remember { mutableStateOf(false) }

    Column{
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
          value = password,
          singleLine = true,
          onValueChange = {
              onPasswordChange(it)
          },
          modifier = Modifier
              .fillMaxWidth(),
          visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
          trailingIcon = {
              IconButton(
                  onClick = { passwordVisibility = !passwordVisibility },
              ) {
                  Icon(
                      painter = painterResource(if (passwordVisibility) com.google.android.material.R.drawable.design_ic_visibility_off else com.google.android.material.R.drawable.design_ic_visibility),
                      contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                  )
              }
          },
          colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          keyboardOptions = keyboardOptions
        )
    }
}

