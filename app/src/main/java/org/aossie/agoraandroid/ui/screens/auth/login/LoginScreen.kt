package org.aossie.agoraandroid.ui.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.BackArrowClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.EnteredPassword
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.EnteredUsername
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.ForgotPasswordClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.LoginClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.LoginFacebookClick
import org.aossie.agoraandroid.ui.screens.auth.models.LoginModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryPasswordField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  loginModel: LoginModel,
  screenState: ScreensState,
  onEvent:(LoginScreenEvent) -> Unit
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      PrimaryTopAppBar(title = R.string.login) {
        onEvent(BackArrowClick)
      }
    }
  ) {
    Box(
      modifier = Modifier
        .padding(it)
        .imePadding()
    ){
      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        item {
          Image(
            modifier = Modifier
              .fillMaxWidth()
              .height(250.dp),
            painter = painterResource(id = drawable.tree),
            contentDescription = "")
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredUsername(it))
            },
            value = loginModel.username,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.user_name)
          )
        }
        item {
          PrimaryPasswordField(
            onPasswordChange = {
              onEvent(EnteredPassword(it))
            },
            password = loginModel.password,
            label = stringResource(id = string.password)
          )
        }
        item {
          Box(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = stringResource(id = string.forgot_password),
              color = MaterialTheme.colorScheme.error,
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                  onEvent(ForgotPasswordClick)
                },
            )
          }
        }
        item {
          PrimaryButton(text = stringResource(id = string.login)) {
            onEvent(LoginClick)
          }
        }
        item {
          Text(
            text = stringResource(id = string.or),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
          )
        }
        item {
          PrimaryButton(
            text = stringResource(id = string.continue_with_facebook),
            backgroundColor = MaterialTheme.colorScheme.background,
            icon = painterResource(id = drawable.ic_facebook_logo)
          ) {
            onEvent(LoginFacebookClick)
          }
        }
      }
      PrimaryProgressSnackView(screenState = screenState)
    }
  }
}