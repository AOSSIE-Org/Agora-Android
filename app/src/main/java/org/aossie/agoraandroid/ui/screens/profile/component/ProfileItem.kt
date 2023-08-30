package org.aossie.agoraandroid.ui.screens.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import kotlinx.coroutines.Dispatchers
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.UserModel
import java.io.File

enum class ProfileButtonOptions {
  Delete, Camera, Gallery
}

@Composable
fun ProfileItem(userModel: UserModel?, avatar: File?, changeAvatarClick: (ProfileButtonOptions) -> Unit) {

  val context = LocalContext.current

  val dropDownMenuState = remember { mutableStateOf(false) }
  val menuItems = listOf(
    stringResource(id = string.delete) to painterResource(id = drawable.ic_delete_24) to ProfileButtonOptions.Delete,
    stringResource(id = string.camera) to painterResource(id = drawable.ic_camera) to ProfileButtonOptions.Camera,
    stringResource(id = string.gallery) to painterResource(id = drawable.ic_gallery) to ProfileButtonOptions.Gallery
  )
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Box(
      modifier = Modifier.size(140.dp)
    ) {
      Card(
        modifier = Modifier
          .size(120.dp)
          .align(Alignment.TopCenter),
      ) {
        if (userModel != null) {
          AsyncImage(
            model = Builder(LocalContext.current)
              .data(avatar?: drawable.ic_user_new)
              .dispatcher(Dispatchers.IO)
              .error(drawable.ic_user_new)
              .crossfade(true)
              .build(),
            modifier = Modifier
              .fillMaxSize(),
            contentScale = ContentScale.Crop,
            contentDescription = ""
          )
        } else {
          CircularProgressIndicator()
        }
      }
      Box(
        modifier = Modifier
          .size(60.dp)
          .background(
            color = MaterialTheme.colorScheme.background,
            shape = CircleShape,
          )
          .align(Alignment.BottomEnd)
      ) {
        Box(
          modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(
              color = MaterialTheme.colorScheme.primaryContainer,
            )
            .align(Alignment.Center)
            .clickable { dropDownMenuState.value = true },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Rounded.PhotoCamera,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
          )
          DropdownMenu(
            modifier = Modifier.align(Alignment.BottomEnd),
            expanded = dropDownMenuState.value,
            onDismissRequest = { dropDownMenuState.value = false }
          ) {
            menuItems.forEach { item ->
              DropdownMenuItem(
                text = {
                  Text(text = item.first.first)
                },
                leadingIcon = {
                  Icon(
                    painter = item.first.second,
                    contentDescription = ""
                  )
                },
                onClick = {
                  changeAvatarClick(item.second)
                  dropDownMenuState.value = false
                }
              )
            }
          }
        }
      }
    }
    userModel?.let {
      Text(
        text = (userModel.username ?: "") ,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold
      )
      Text(
        text = userModel.email?: "",
        style = MaterialTheme.typography.titleMedium
      )
    }
  }
}