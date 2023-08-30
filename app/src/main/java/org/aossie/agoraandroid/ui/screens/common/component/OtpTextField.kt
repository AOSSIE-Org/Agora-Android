package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtpTextField(
  modifier: Modifier = Modifier,
  otpText: String,
  otpCount: Int = 6,
  onOtpTextChange: (String, Boolean) -> Unit
) {
  LaunchedEffect(Unit) {
    if (otpText.length > otpCount) {
      throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
    }
  }

  BasicTextField(
    modifier = modifier,
    maxLines = 1,
    value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
    onValueChange = {
      if (it.text.length <= otpCount) {
        onOtpTextChange.invoke(it.text, it.text.length == otpCount)
      }
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
    decorationBox = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        repeat(otpCount) { index ->
          CharView(
            index = index,
            text = otpText
          )
        }
      }
    }
  )
}

@Composable
private fun CharView(
  index: Int,
  text: String
) {
  val isFocused = text.length == index
  val char = when {
    index == text.length -> "0"
    index > text.length -> ""
    else -> text[index].toString()
  }
  Text(
    modifier = Modifier
      .width(40.dp)
      .border(
       shape = RoundedCornerShape(8.dp),
        color = if (isFocused) {
          MaterialTheme.colorScheme.outlineVariant
        } else {
          MaterialTheme.colorScheme.outline
        },
        width = 1.dp
      )
      .padding(2.dp),
    text = char,
    style = MaterialTheme.typography.headlineMedium,
    color = if (isFocused) {
      MaterialTheme.colorScheme.outlineVariant
    } else {
      MaterialTheme.colorScheme.outline
    },
    textAlign = TextAlign.Center
  )
}