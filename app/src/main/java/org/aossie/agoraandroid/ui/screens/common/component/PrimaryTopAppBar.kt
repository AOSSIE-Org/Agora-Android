package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTopAppBar(
  title:Int,
  onBackArrowClick:() -> Unit){
  TopAppBar(
    title = {
      Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.titleLarge,
      )
    },
    navigationIcon = {
      IconButton(onClick = onBackArrowClick) {
        Icon(imageVector = Rounded.ArrowBack, contentDescription = "")
      }
    },
    colors = TopAppBarDefaults.smallTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
      navigationIconContentColor = MaterialTheme.colorScheme.onBackground
    )
  )
}