package org.aossie.agoraandroid.ui.screens.welcome

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.welcome.components.ImageSlider

@Composable
fun WelcomeScreen(
  data: Array<Pair<Int, Int>>,
  onEvent:(WelcomeScreenEvent) -> Unit
) {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = !isSystemInDarkTheme()

  DisposableEffect(systemUiController, useDarkIcons) {
    systemUiController.setStatusBarColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons
    )
    onDispose {}
  }
  Column(
    modifier = Modifier.fillMaxSize()
  ) {

    ImageSlider(
      images = data,
      modifier = Modifier.fillMaxWidth().weight(0.75f)
    )
    Column(
      modifier = Modifier.fillMaxWidth().weight(0.25f).padding(horizontal = 25.dp),
      verticalArrangement = Arrangement.SpaceEvenly
    ) {
      PrimaryButton(
        text = stringResource(id = R.string.get_started),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
        strokeColor = MaterialTheme.colorScheme.primaryContainer
      ) {
        onEvent(WelcomeScreenEvent.GET_STARTED_CLICKED)
      }
      PrimaryButton(
        text = stringResource(id = R.string.already_have_an_account),
        backgroundColor = MaterialTheme.colorScheme.background,
        textColor = MaterialTheme.colorScheme.onBackground,
        strokeColor = MaterialTheme.colorScheme.primaryContainer
      ) {
        onEvent(WelcomeScreenEvent.ALREADY_HAVE_ACCOUNT_CLICKED)
      }
    }
  }
}