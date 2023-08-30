package org.aossie.agoraandroid.ui.screens.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.auth.models.SignUpModel
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.BackArrowClick
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredEmail
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredFirstName
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredLastName
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredPassword
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredSecurityAnswer
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredUsername
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.SelectedSecurityQuestion
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.SignUpClICK
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryPasswordField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryTitledSpinner
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
  signUpModel: SignUpModel,
  screenState: ScreensState,
  onEvent:(SignUpScreenEvent) -> Unit
){

  val securityQuestionsList = stringArrayResource(id = R.array.security_questions).toList()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      PrimaryTopAppBar(title = R.string.create_account) {
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
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredUsername(it))
            },
            value = signUpModel.username,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.user_name)
          )
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredFirstName(it))
            },
            value = signUpModel.firstName,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.first_name)
          )
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredLastName(it))
            },
            value = signUpModel.lastName,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.last_name)
          )
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredEmail(it))
            },
            value = signUpModel.email,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.email_address),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email)
          )
        }
        item {
          PrimaryPasswordField(
            onPasswordChange = {
              onEvent(EnteredPassword(it))
            },
            password = signUpModel.password,
            label = stringResource(id = string.password)
          )
        }
        item {
          PrimaryTitledSpinner(
            title = stringResource(id = string.secret_security_question),
            list = securityQuestionsList,
            selectedIndex = if (signUpModel.securityQuestion.isNotEmpty()) securityQuestionsList.indexOf(
              signUpModel.securityQuestion
            ) else 0,
            onItemSelected = {
              onEvent(SelectedSecurityQuestion(securityQuestionsList[it]))
            }
          )
        }
        item {
          PrimaryLabelTextField(
            onValueChange = {
              onEvent(EnteredSecurityAnswer(it))
            },
            value = signUpModel.securityAnswer,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.answer)
          )
        }
        item {
          PrimaryButton(text = stringResource(id = string.sign_up)) {
            onEvent(SignUpClICK)
          }
        }
      }
      PrimaryProgressSnackView(screenState = screenState)
    }
  }
}