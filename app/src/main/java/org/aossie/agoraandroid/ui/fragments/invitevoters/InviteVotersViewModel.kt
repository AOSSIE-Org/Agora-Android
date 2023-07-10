package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.Repository.FCMRepository
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.AddVoterClick
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.DeleteVoterClick
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.EnteredVoterEmail
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.EnteredVoterName
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.OpenCSVFileClick
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.SnackBarActionClick
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepositoryImpl,
  private val fcmRepository: FCMRepository
) : ViewModel() {

  private val _inviteVotersDataState = MutableStateFlow (VotersDto("",""))
  val inviteVotersDataState = _inviteVotersDataState.asStateFlow()

  private val _votersListState = MutableStateFlow<List<VotersDto>>(emptyList())
  val votersListState = _votersListState.asStateFlow()

  lateinit var sessionExpiredListener: SessionExpiredListener
  var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

  private val _progressAndErrorState = mutableStateOf(ScreensState())
  val progressAndErrorState: State<ScreensState> = _progressAndErrorState

  private val _uiEvents = MutableSharedFlow<InviteVotersUiEvents>()
  val uiEvents = _uiEvents.asSharedFlow()

  fun onEvent(event: InviteVotersScreenEvent){
    when(event) {
      AddVoterClick -> {
        if(nameValidator(inviteVotersDataState.value.voterName)
          && emailValidator(inviteVotersDataState.value.voterEmail)){
          _votersListState.value = votersListState.value + inviteVotersDataState.value
          _inviteVotersDataState.value = inviteVotersDataState.value.copy(
            voterEmail = "",
            voterName = ""
          )
        }

      }
      is EnteredVoterEmail -> {
        _inviteVotersDataState.value = inviteVotersDataState.value.copy(
          voterEmail = event.voterEmail
        )
      }
      is EnteredVoterName -> {
        _inviteVotersDataState.value = inviteVotersDataState.value.copy(
          voterName = event.voterName
        )
      }
      SnackBarActionClick -> {
        hideSnackBar()
      }
      is DeleteVoterClick -> {
        _votersListState.value = votersListState.value - event.votersDto
      }

      is OpenCSVFileClick -> {
        readExcelData(event.context,event.path,votersListState.value)
      }
      else -> {}
    }
  }

  private fun emailValidator(
    email: String?,
  ): Boolean {
    if (email.isNullOrEmpty()) {
      showMessage(string.enter_voter_email)
      return false
    } else if (!email.matches(emailPattern.toRegex())) {
      showMessage(string.enter_valid_voter_email)
      return false
    } else if (getEmailList(_votersListState.value).contains(email)) {
      showMessage(string.voter_same_email)
      return false
    }
    return true
  }

  private fun nameValidator(name: String?): Boolean {
    if (name.isNullOrEmpty()) {
      showMessage(string.enter_voter_name)
      return false
    }
    return true
  }

  fun inviteVoters(
    mVoters: List<VotersDto>,
    id: String
  ) {
    if(mVoters.isEmpty()){
      showMessage(R.string.empty_voters_list)
      return
    }
    sendVoters(id, mVoters)
  }

  private fun sendVoters(
    id: String,
    body: List<VotersDto>
  ) {
    showLoading("Inviting voters...")
    viewModelScope.launch {
      try {
        val response = electionsRepository.sendVoters(id, body)
        Timber.d(response.toString())
        showMessage(string.voters_invited)
        delay(1000)
        _uiEvents.emit(InviteVotersUiEvents.VotersInvited)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  private fun importValidator(
    email: String,
    name: String,
    mVoters: List<VotersDto>
  ): Boolean {
    val isNameValid = name.isNotEmpty()
    val isEmailValid =
      email.isNotEmpty() && email.matches(emailPattern.toRegex()) && !getEmailList(
        mVoters
      ).contains(email)
    return isNameValid && isEmailValid
  }

  private fun getEmailList(mVoters: List<VotersDto>): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    for (voter in mVoters) {
      voter.voterEmail?.let { list.add(it) }
    }
    return list
  }

  fun readExcelData(
    context: Context,
    excelFilePath: String,
    existingVoters: List<VotersDto>
  ) {
    showLoading("Reading file...")
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val inputStream: InputStream = FileInputStream(File(excelFilePath))
        val workbook = XSSFWorkbook(inputStream)
        val sheet: XSSFSheet = workbook.getSheetAt(0) ?: run {
          withContext(Dispatchers.Main) {
            showMessage(string.no_sheet)
          }
          return@launch
        }
        val list: ArrayList<VotersDto> = ArrayList()
        for (row in sheet.rowIterator()) {
          val name = row.getCell(0)?.stringCellValue ?: ""
          val email = row.getCell(1)?.stringCellValue ?: ""
          if (importValidator(email, name, existingVoters)) list.add(VotersDto(name, email))
        }
        withContext(Dispatchers.Main) {
          hideLoading()
          _votersListState.value = votersListState.value + list
        }
      } catch (e: NotOfficeXmlFileException) {
        withContext(Dispatchers.Main) {
          showMessage(string.not_excel)
        }
      } catch (e: FileNotFoundException) {
        withContext(Dispatchers.Main) {
          showMessage(string.file_not_available)
        }
      } catch (e: IOException) {
        withContext(Dispatchers.Main) {
          showMessage(string.cannot_read_file)
        }
      }
    }
  }

  fun sendFCM(
    mail: String?,
    title: String,
    body: String,
    electionId: String,
  ) {
    viewModelScope.launch {
      try {
        mail?.let {
          it.substring(0, it.indexOf("@")).let { topic ->
            fcmRepository.sendFCM(topic, title, body, electionId)
          }
        }
      } catch (e: Exception) {
        Timber.d(e)
      }
    }
  }

  private fun showLoading(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  fun showMessage(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair("",false)
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }

  sealed class InviteVotersUiEvents{
    object VotersInvited: InviteVotersUiEvents()
  }
}
