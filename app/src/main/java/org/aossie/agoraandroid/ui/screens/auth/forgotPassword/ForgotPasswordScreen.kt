package org.aossie.agoraandroid.ui.screens.auth.forgotPassword

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnBackIconClick
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnSendLinkClick
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnUserNameEntered
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
  screenState: ScreensState,
  userNameState:String,
  onEvent:(ForgotPasswordScreenEvents) -> Unit,
){
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      PrimaryTopAppBar(title = R.string.forgot_password) {
        onEvent(OnBackIconClick)
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
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        item {
          Image(
            modifier = Modifier
              .fillMaxWidth()
              .height(250.dp),
            painter = painterResource(id = R.drawable.annoyed),
            contentDescription = "")
        }
        item {
          Text(
            text = stringResource(id = R.string.enter_user_name_reset_pass_link),
            style = MaterialTheme.typography.titleLarge
          )
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(OnUserNameEntered(it))
            },
            value = userNameState,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.user_name)
          )
        }
        item {
          PrimaryButton(text = stringResource(id = string.send_link)) {
            onEvent(OnSendLinkClick)
          }
        }
      }
      PrimaryProgressSnackView(screenState = screenState)
    }
  }
}
