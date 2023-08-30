package org.aossie.agoraandroid.ui.screens.share

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareWithOthersScreen(
  onShareClick:() -> Unit
){
  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(25.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
      Image(
        painter = painterResource(id = R.drawable.share),
        contentDescription = "",
        modifier = Modifier
          .fillMaxWidth()
          .padding(it)
          .heightIn(max = 230.dp)
      )
      Text(
        text = stringResource(id = R.string.share_with_others_to_make_open_source_a_more_beautiful_place),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Light,
      )
      PrimaryButton(
        text = stringResource(id = R.string.share),
        icon = painterResource(id = R.drawable.ic_share)
      ) {
        onShareClick()
      }
    }
  }
}