package org.aossie.agoraandroid.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.IconTextNavigationButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressView
import org.aossie.agoraandroid.ui.screens.common.component.PrimarySnackBar
import org.aossie.agoraandroid.ui.screens.settings.component.LanguageUpdateDialog
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
  userModel: UserModel,
  avatar: File?,
  appLangState: String,
  supportedLang: List<Pair<String, String>>,
  screenState: ScreensState,
  screenEvent: (SettingsScreenEvent) -> Unit
) {

  val languageDialogState = remember { mutableStateOf(false) }
  val selectedLang = supportedLang.find {
    it.second == appLangState
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    Box(modifier = Modifier.padding(it)) {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 25.dp),
      ) {
        item {
          ProfileItem(userModel,avatar)
        }
        item {
          Spacer(modifier = Modifier.height(15.dp))
          Divider(
            modifier = Modifier.padding(horizontal = 25.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
          )
          Spacer(modifier = Modifier.height(15.dp))
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.language),
            arrowText = selectedLang?.first ?: "English",
            iconStart = R.drawable.ic_translate,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              languageDialogState.value = true
            }
          )
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.account_settings),
            iconStart = R.drawable.ic_user_new,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              screenEvent(SettingsScreenEvent.OnAccountSettingClick)
            }
          )
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.about_us),
            iconStart = R.drawable.ic_info,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              screenEvent(SettingsScreenEvent.OnAboutUsClick)
            }
          )
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.share_with_others),
            iconStart = R.drawable.ic_share,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              screenEvent(SettingsScreenEvent.OnShareWithOthersClick)
            }
          )
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.contact_us),
            iconStart = R.drawable.ic_contact_us,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              screenEvent(SettingsScreenEvent.OnContactUsClick)
            }
          )
        }
        item {
          IconTextNavigationButton(
            text = stringResource(id = R.string.logout),
            iconStart = R.drawable.ic_logout,
            iconEnd = {
              Icon(imageVector = Icons.Rounded.NavigateNext, contentDescription = "")
            },
            onClick = {
              screenEvent(SettingsScreenEvent.OnLogoutClick)
            }
          )
        }
      }

      if(languageDialogState.value){
        LanguageUpdateDialog(
          languages = supportedLang,
          onDismissRequest = {
            languageDialogState.value = false
          },
          onConfirmRequest = {
             screenEvent(SettingsScreenEvent.ChangeAppLanguage(it))
          },
          selectedLang = selectedLang!!,
        )
      }

      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(
            horizontal = 20.dp,
          ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (screenState.error.second) {
          PrimarySnackBar(
            text = screenState.error.first,
            actionText = "ok"
          ) {
            screenEvent(SettingsScreenEvent.OnSnackBarActionClick)
          }
        }
        if (screenState.errorResource.second) {
          PrimarySnackBar(
            text = stringResource(id = screenState.errorResource.first),
            actionText = "ok"
          ) {
            screenEvent(SettingsScreenEvent.OnSnackBarActionClick)
          }
        }
        if (screenState.isLoading.second) {
          PrimaryProgressView(
            text = screenState.isLoading.first,
          )
        }
      }
    }
  }
}

@Composable
fun ProfileItem(userModel: UserModel, avatar: File?) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 25.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Card(
      modifier = Modifier.size(100.dp),
      elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
      )
    ) {
      AsyncImage(
        model = ImageRequest
          .Builder(LocalContext.current)
          .data(avatar?.toUri() ?: userModel.avatarURL)
          .dispatcher(Dispatchers.IO)
          .error(R.drawable.ic_user)
          .crossfade(true)
          .build(),
        modifier = Modifier
          .fillMaxSize(),
        contentDescription = "")
    }
    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(10.dp)) {
      Text(
        text = (userModel.firstName ?: "") + " " + (userModel.lastName ?: ""),
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
