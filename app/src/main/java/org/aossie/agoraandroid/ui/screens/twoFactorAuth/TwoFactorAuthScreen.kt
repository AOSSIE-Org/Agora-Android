package org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.OnBackClick
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.ResendOtpClick
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.VerifyOtpClick
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.OtpTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorAuthScreen(
  progressErrorState: ScreensState,
  onEvent: (TwoFactorAuthScreenEvent) -> Unit
) {

  var otp by remember {
    mutableStateOf("")
  }
  var trustDevice by remember {
    mutableStateOf(false)
  }
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()

  DisposableEffect(systemUiController, useDarkIcons) {
    systemUiController.setSystemBarsColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons
    )
    onDispose {}
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground,
    topBar = {
      TopAppBar(
        navigationIcon = {
           IconButton(onClick = {
             onEvent(OnBackClick)
           }) {
             Icon(
               imageVector = Icons.Rounded.ArrowBackIos,
               contentDescription = ""
             )
           }
        },
        title = {},
        colors = TopAppBarDefaults.largeTopAppBarColors(
          navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
          containerColor = MaterialTheme.colorScheme.background
        )
      )
    }
  ) {

    Box(modifier = Modifier.padding(it)) {

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        item {
          Text(
            text = stringResource(id = string.two_factor_authentication),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
          )
        }
        item {
          Text(
            text = stringResource(id = string.please_enter_the_one_time_password_we_have_sent_to_your_registered_email_address),
            style = MaterialTheme.typography.bodyMedium
          )
        }

        item {
          OtpTextField(
            otpText = otp,
            onOtpTextChange = { value, otpInputFilled ->
              otp = value
            }
          )
        }

        item { 
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = stringResource(id = R.string.didnt_receive_otp),
              style = MaterialTheme.typography.bodyLarge
            )
            ClickableText(
              text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.inversePrimary)) {
                  append(stringResource(id = R.string.resend_otp))
                }
              },
              style = MaterialTheme.typography.bodyLarge,
              onClick = {
                onEvent(ResendOtpClick)
              }
            )
          }
        }
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = stringResource(id = R.string.do_you_trust_this_device),
              style = MaterialTheme.typography.titleMedium,
            )
            Checkbox(
              checked = trustDevice,
              onCheckedChange = {
                trustDevice = it
              }
            )
          }
        }
        item {
          PrimaryButton(text = stringResource(id = R.string.verify)) {
            onEvent(VerifyOtpClick(otp,trustDevice))
          }
        }
      }

      PrimaryProgressSnackView(screenState = progressErrorState)
    }
  }
}