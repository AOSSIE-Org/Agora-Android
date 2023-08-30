package org.aossie.agoraandroid.ui.screens.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.BuildConfig
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PermissionsDialog
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryPasswordField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.profile.component.ConfirmationDialog
import org.aossie.agoraandroid.ui.screens.profile.component.IconTextSwitchButton
import org.aossie.agoraandroid.ui.screens.profile.component.ProfileButtonOptions.Camera
import org.aossie.agoraandroid.ui.screens.profile.component.ProfileButtonOptions.Delete
import org.aossie.agoraandroid.ui.screens.profile.component.ProfileButtonOptions.Gallery
import org.aossie.agoraandroid.ui.screens.profile.component.ProfileItem
import org.aossie.agoraandroid.utilities.canAuthenticateBiometric
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
  prefs: PreferenceProvider,
  screenState: ScreensState,
  userData: UserModel?,
  profileDataState: ProfileScreenDataState,
  onEvent: (ProfileScreenEvent) -> Unit
) {

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val twoFactorAuth = remember {
    mutableStateOf(false)
  }

  val bioMetricState = remember {
    mutableStateOf(false)
  }

  val showTwoFactorDialog = remember {
    mutableStateOf(false)
  }
  val storagePermissionState = rememberMultiplePermissionsState(
    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
  )
  val cameraPermissionState = rememberMultiplePermissionsState(
    listOf( android.Manifest.permission.CAMERA)
  )
  val permissionDialog = remember { mutableStateOf(Pair("",false) to storagePermissionState) }
  val storagePermissionText = stringResource(id = string.storage_permission_required)
  val cameraPermissionText = stringResource(id = string.camera_permission_required)

  val getImageFromGallery = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
  ) { uri: Uri? ->
    uri?.let {
      onEvent(ProfileScreenEvent.UpdateImage(it))
    }
  }

  val file = context.createImageFile()
  val uri = FileProvider.getUriForFile(
    Objects.requireNonNull(context),
    BuildConfig.APPLICATION_ID + ".provider", file
  )

  val cameraLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
      onEvent(ProfileScreenEvent.UpdateImage(uri))
    }

  LaunchedEffect(key1 = prefs) {
    bioMetricState.value = prefs.isBiometricEnabled().first()
  }

  LaunchedEffect(key1 = userData) {
    userData?.let {
      twoFactorAuth.value = it.twoFactorAuthentication?:false
    }
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    Box(modifier = Modifier.padding(it)){

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {

        item {
          ProfileItem(userModel = userData, avatar = profileDataState.avatar) {
            when(it) {
              Delete -> {
                onEvent(ProfileScreenEvent.DeletePic)
              }
              Camera -> {
                if(cameraPermissionState.allPermissionsGranted){
                  cameraLauncher.launch(uri)
                }else{
                  if (cameraPermissionState.shouldShowRationale) {
                    permissionDialog.value = Pair(cameraPermissionText, true) to cameraPermissionState
                  } else {
                    permissionDialog.value = Pair(cameraPermissionText, true) to cameraPermissionState
                  }
                }
              }
              Gallery -> {
                if(storagePermissionState.allPermissionsGranted){
                  getImageFromGallery.launch("image/*")
                }else{
                  if (storagePermissionState.shouldShowRationale) {
                    permissionDialog.value = Pair(storagePermissionText, true) to storagePermissionState
                  } else {
                    permissionDialog.value = Pair(storagePermissionText, true) to storagePermissionState
                  }
                }
              }
            }
          }
        }
        item { 
          Spacer(modifier = Modifier.height(10.dp))
        }
        item {
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.weight(0.45f)) {
              PrimaryLabelTextField(
                label = stringResource(id = string.first_name),
                value = profileDataState.firstName,
                onValueChange = {
                  onEvent(ProfileScreenEvent.EnteredFirstName(it))
                }
              )
            }
            Box(modifier = Modifier.weight(0.45f)) {
              PrimaryLabelTextField(
                label = stringResource(id = string.last_name),
                value = profileDataState.lastName,
                onValueChange = {
                  onEvent(ProfileScreenEvent.EnteredLastName(it))
                }
              )
            }
          }
        }
        item {
          PrimaryButton(text = stringResource(id = string.update_profile)) {
            onEvent(ProfileScreenEvent.UpdateProfileClick)
          }
        }
        item {
          PrimaryPasswordField(
            label = stringResource(id = string.new_password),
            password = profileDataState.newPassword,
            onPasswordChange = {
              onEvent(ProfileScreenEvent.EnteredPassword(it))
            }
          )
        }
        item {
          PrimaryPasswordField(
            label = stringResource(id = string.confirm_new_password),
            password = profileDataState.confirmPassword,
            onPasswordChange = {
              onEvent(ProfileScreenEvent.EnteredConfirmPassword(it))
            }
          )
        }
        item {
          PrimaryButton(text = stringResource(id = string.change_password)) {
            onEvent(ProfileScreenEvent.ChangePasswordClick)
          }
        }
        item {
          IconTextSwitchButton(
            text = stringResource(id = string.toggle_two_factor_authentication),
            iconStart = drawable.ic_two_factor_auth,
            checked = twoFactorAuth.value,
            onCheckedChange = {
              twoFactorAuth.value = it
              showTwoFactorDialog.value = true
            }
          )
        }
        if(context.canAuthenticateBiometric()){
          item {
            IconTextSwitchButton(
              text = stringResource(id = string.toggle_biometric_authentication),
              iconStart = drawable.ic_fingerprint_24,
              checked = bioMetricState.value,
              onCheckedChange = {
                bioMetricState.value = it
                coroutineScope.launch {
                  prefs.enableBiometric(it)
                }
              }
            )
          }
        }
      }

      if (permissionDialog.value.first.second) {
        PermissionsDialog(
          title = "Permission !",
          description = permissionDialog.value.first.first,
          onDialogConfirm = {
            permissionDialog.value.second.launchMultiplePermissionRequest()
            permissionDialog.value = Pair("",false) to storagePermissionState
          },
          onDialogDismiss = {
            permissionDialog.value = Pair("",false) to storagePermissionState
          }
        )
      }

      ConfirmationDialog(
        showDialog = showTwoFactorDialog.value,
        enableTwoFactor = twoFactorAuth.value,
        onConfirm = {
          showTwoFactorDialog.value = false
          onEvent(ProfileScreenEvent.ToggleTwoFactor(twoFactorAuth.value))
        }) {
        showTwoFactorDialog.value = false
        twoFactorAuth.value = !twoFactorAuth.value
      }
      PrimaryProgressSnackView(screenState = screenState)
    }
  }
}

fun Context.createImageFile(): File {
  val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
  val imageFileName = "JPEG_" + timeStamp + "_"
  val image = File.createTempFile(
    imageFileName,
    ".jpg",
    externalCacheDir
  )
  return image
}