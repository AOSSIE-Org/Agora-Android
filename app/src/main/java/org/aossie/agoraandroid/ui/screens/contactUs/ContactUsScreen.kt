package org.aossie.agoraandroid.ui.screens.contactUs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.common.component.IconTextNavigationButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimarySnackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(
  message: State<String>,
  onBtnGitlabClicked: () -> Unit,
  onBtnGitterClicked: () -> Unit,
  onBtnReportBugClicked: () -> Unit,
  hideSnackBarClick: () -> Unit
) {
  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    Box {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        item {
          Image(
            painter = painterResource(id = drawable.ic_contact_us),
            contentDescription = "",
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 230.dp)
          )
        }
        item {
          Text(
            text = stringResource(id = string.feel_free_to_contact_us_on_our_gitter_channel_and_work_with_us_on_gitlab),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 25.dp)
          )
        }
        item {
          Divider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
              .padding(horizontal = 25.dp)
              .fillMaxWidth()
          )
        }
        item {
          Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            IconTextNavigationButton(
              text = stringResource(id = string.gitlab),
              iconStart = drawable.ic_gitlab,
              iconStartTint = Color.Unspecified,
              iconEnd = {
                Icon(
                  imageVector = Rounded.NavigateNext,
                  contentDescription = ""
                )
              }) {
              onBtnGitlabClicked.invoke()
            }
            IconTextNavigationButton(
              text = stringResource(id = string.gitter),
              iconStart = drawable.ic_gitter,
              iconStartTint = Color.Unspecified,
              iconEnd = {
                Icon(
                  imageVector = Rounded.NavigateNext,
                  contentDescription = ""
                )
              }) {
              onBtnGitterClicked.invoke()
            }
            IconTextNavigationButton(
              text = stringResource(id = string.report_a_bug),
              iconStart = drawable.ic_bug,
              iconStartTint = Color.Unspecified,
              iconEnd = {
                Icon(
                  imageVector = Rounded.NavigateNext,
                  contentDescription = ""
                )
              }) {
              onBtnReportBugClicked.invoke()
            }
          }
        }
      }
      if(message.value.isNotEmpty()){
        PrimarySnackBar(text = message.value) {
          hideSnackBarClick.invoke()
        }
      }
    }
  }
}