package org.aossie.agoraandroid.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.screens.common.component.IconTextNavigationButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(){
  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(it),
      contentPadding = PaddingValues(vertical = 20.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
      item {
        Image(
          painter = painterResource(id = R.drawable.ic_about),
          contentDescription = "",
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 230.dp)
        )
      }
      item {
        Text(
          text = stringResource(id = R.string.app_name),
          style = MaterialTheme.typography.headlineLarge,
          modifier = Modifier.padding(horizontal = 25.dp)
        )
      }
      item {
        Text(
          text = stringResource(id = R.string.about_info) +" "+ stringResource(id = R.string.about_info2),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Light,
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
            text = stringResource(id = R.string.privacy_policy),
            iconStart = R.drawable.ic_privacy_policy,
            iconEnd = {
              Icon(
                imageVector = Icons.Rounded.NavigateNext,
                contentDescription = "")
            }) {

          }
          IconTextNavigationButton(
            text = stringResource(id = R.string.terms_and_conditions),
            iconStart = R.drawable.ic_terms_condition,
            iconEnd = {
              Icon(
                imageVector = Icons.Rounded.NavigateNext,
                contentDescription = "")
            }) {

          }
        }
      }
    }
  }
}