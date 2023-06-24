package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState

@Composable
fun PrimaryProgressSnackView(
  screenState: ScreensState
) {
  Box(modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(
          20.dp
        ),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      screenState.message.let {
        if (it.second) {
          when (val first = it.first) {
            is String -> PrimarySnackBar(text = first.toString())
            is Int -> PrimarySnackBar(text = stringResource(id = first))
          }
        }
      }
      screenState.loading.let {
        if (it.second) {
          when (val first = it.first) {
            is String -> PrimaryProgressView(text = first.toString())
            is Int -> PrimaryProgressView(text = stringResource(id = first))
          }
        }
      }
    }
  }
}