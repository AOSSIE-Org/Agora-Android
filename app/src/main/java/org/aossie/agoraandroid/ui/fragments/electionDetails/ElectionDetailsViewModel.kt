package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.model.VotersDtoModel
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.domain.useCases.electionDetails.ElectionDetailsUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.DeleteElectionClick
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.InviteVotersClick
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.ResultClick
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.ElectionUtils
import org.aossie.agoraandroid.utilities.FileUtils
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ElectionDetailsViewModel
@Inject
constructor(
  private val electionDetailsUseCases: ElectionDetailsUseCases
) : ViewModel() {

  private val _getShareResponseStateFlow = MutableStateFlow<ResponseUI<Uri>?>(null)
  var getShareResponseStateFlow: StateFlow<ResponseUI<Uri>?> = _getShareResponseStateFlow
  private val _getResultResponseStateFlow = MutableStateFlow<ResponseUI<WinnerDtoModel>?>(null)
  var getResultResponseStateFlow: StateFlow<ResponseUI<WinnerDtoModel>?> =
    _getResultResponseStateFlow

  lateinit var sessionExpiredListener: SessionExpiredListener

  private val _uiEventsFlow = MutableSharedFlow<UiEvents>()
  val uiEventsFlow = _uiEventsFlow.asSharedFlow()

  private val _progressAndErrorState = mutableStateOf(ScreensState())
  val progressAndErrorState: State<ScreensState> = _progressAndErrorState

  private val _votersListState = MutableStateFlow<List<VotersDtoModel>>(emptyList())
  val votersListState = _votersListState.asStateFlow()

  private val _ballotsListState = MutableStateFlow<List<BallotDtoModel>>(emptyList())
  val ballotsListState = _ballotsListState.asStateFlow()

  private var _electionState = mutableStateOf<ElectionModel?>(null)
  val electionState: State<ElectionModel?> = _electionState

  private var _resultState = mutableStateOf<WinnerDtoModel?>(null)
  val resultState: State<WinnerDtoModel?> = _resultState

  private var status: AppConstants.Status? = null

  fun getElectionDetailsById(id: String) = viewModelScope.launch {
    showLoading("Loading election details...")
    electionDetailsUseCases.getElectionById(id).collectLatest {
      hideLoading()
      _electionState.value = it
      getStatus(it)
    }
  }

  private fun getStatus(election: ElectionModel) {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    val formattedStartingDate: Date? = formatter.parse(election.start)
    val formattedEndingDate: Date? = formatter.parse(election.end)
    val currentDate = Calendar.getInstance().time
    status = ElectionUtils.getEventStatus(currentDate, formattedStartingDate, formattedEndingDate)
  }

  fun getElectionById(id: String): Flow<ElectionModel> {
    return electionDetailsUseCases.getElectionById(id)
  }

  fun getBallot(
    id: String?
  ) {
    showLoading("Listing ballots...")
    viewModelScope.launch {
      try {
        val response: List<BallotDtoModel> = electionDetailsUseCases.getBallots(id)
        Timber.d(response.toString())
        _ballotsListState.value = response
        hideLoading()
      } catch (e: ApiException) {
        showMessage(e.message!!)
        getBallotsFromDb(id)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
        getBallotsFromDb(id)
      } catch (e: Exception) {
        showMessage(e.message!!)
        getBallotsFromDb(id)
      }
    }
  }

  private fun getBallotsFromDb(id: String?) {
    showLoading("Listing ballots...")
    viewModelScope.launch {
      getElectionById(id!!)
        .collect {
          if (it != null) {
            _ballotsListState.value = (it.ballot as List<BallotDtoModel>)
          }
          hideLoading()
        }
    }
  }

  fun getVoter(
    id: String?
  ) {
    showLoading("Listing voters...")
    viewModelScope.launch {
      try {
        val response = electionDetailsUseCases.getVoters(id)
        Timber.d(response.toString())
        _votersListState.value = response
        hideLoading()
      } catch (e: ApiException) {
        showMessage(e.message!!)
        getVotersFromDb(id)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
        getVotersFromDb(id)
      } catch (e: Exception) {
        showMessage(e.message!!)
        getVotersFromDb(id)
      }
    }
  }

  private fun getVotersFromDb(id: String?) {
    showLoading("Listing voters...")
    viewModelScope.launch {
      getElectionById(id!!)
        .collect {
          if (it != null) {
            _votersListState.value = (it.voterList as List<VotersDtoModel>)
          }
          hideLoading()
        }
    }
  }

  fun deleteElection(
    id: String?
  ) {
    showLoading("Deleting election...")
    viewModelScope.launch {
      try {
        val response = electionDetailsUseCases.deleteElection(id)
        Timber.d(response.toString())
        showMessage(response[1])
        _uiEventsFlow.emit(UiEvents.ElectionDeleted)
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

  fun getResult(
    id: String?
  ) {
    showLoading("Loading result...")
    viewModelScope.launch {
      try {
        val response = electionDetailsUseCases.getResult(id)
        hideLoading()
        if (!response.isNullOrEmpty())
          _resultState.value = response[0]
        else
          showMessage("Result Not Found!!")
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

  fun createImage(
    context: Context,
    bitmap: Bitmap
  ) {
    showLoading("Sharing result...")
    viewModelScope.launch {
      val btm = withContext(Dispatchers.IO) {
        FileUtils.saveBitmap(context, bitmap)
      }
      btm?.let {
        hideLoading()
        _uiEventsFlow.emit(UiEvents.ShareFile(it))
      } ?: run {
        showMessage(string.something_went_wrong_please_try_again_later)
      }
    }
  }

  fun createExcelFile(
    context: Context,
    id: String
  ) {
    if(resultState.value==null){
      return
    }
    showLoading("Creating excel file...")
    viewModelScope.launch {
      val workbook = withContext(Dispatchers.Default) {
        createWorkbook(context, resultState.value!!)
      }
      try {
        val uri = withContext(Dispatchers.IO) {
          writeToExcelFile(context, workbook, id)
        }
        hideLoading()
        _uiEventsFlow.emit(UiEvents.ShareFile(uri))
      } catch (e: FileNotFoundException) {
        showMessage(string.file_not_available)
      } catch (e: IOException) {
        showMessage(string.cannot_write_file)
      } catch (e: Exception) {
        showMessage(string.something_went_wrong_please_try_again_later)
      }
    }
  }

  private fun createWorkbook(
    context: Context,
    winnerDto: WinnerDtoModel
  ): XSSFWorkbook {
    val workbook = XSSFWorkbook()
    val sheet: XSSFSheet = workbook.createSheet(context.getString(string.result))
    val title = sheet.createRow(0)
    title.createCell(0)
      .setCellValue(context.getString(string.candidate))
    title.createCell(1)
      .setCellValue(context.getString(string.votes))
    val winner = sheet.createRow(1)
    winner.createCell(0)
      .setCellValue(winnerDto.candidate?.name)
    winner.createCell(1)
      .setCellValue(winnerDto.score?.numerator.toString())
    val others = sheet.createRow(2)
    others.createCell(0)
      .setCellValue(context.getString(string.others))
    others.createCell(1)
      .setCellValue(winnerDto.score?.denominator.toString())
    return workbook
  }

  private fun writeToExcelFile(
    context: Context,
    workbook: XSSFWorkbook,
    id: String
  ): Uri {
    val folderName = context.getExternalFilesDir(null)?.absolutePath
    val folder = File("$folderName", context.getString(string.result))
    if (!folder.exists()) {
      folder.mkdirs()
    }
    val fileName = "$id.xlsx"
    val file = File(folder, fileName)
    if (file.exists()) file.delete()
    try {
      val fileOut = FileOutputStream(file)
      workbook.write(fileOut)
      fileOut.flush()
      fileOut.close()
      return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
      )
    } catch (e: Exception) {
      throw e
    }
  }

  fun onEvent(event: ElectionDetailsScreenEvent) = viewModelScope.launch {
    when(event){
      DeleteElectionClick -> {
        when (status) {
          AppConstants.Status.ACTIVE -> showMessage(string.active_elections_not_started)
          AppConstants.Status.FINISHED -> deleteElection(electionState.value!!._id)
          AppConstants.Status.PENDING -> deleteElection(electionState.value!!._id)
          else -> {}
        }
      }
      InviteVotersClick -> {
        if (status == AppConstants.Status.FINISHED) {
          showMessage(string.election_finished)
        } else {
          _uiEventsFlow.emit(UiEvents.InviteVoters)
        }
      }
      ResultClick -> {
        if (status == AppConstants.Status.PENDING) {
          showMessage(string.election_not_started)
        } else {
          _uiEventsFlow.emit(UiEvents.ViewResults)
        }
      }
      else -> {}
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

  sealed class UiEvents{
    object ElectionDeleted:UiEvents()
    object InviteVoters:UiEvents()
    object ViewResults:UiEvents()
    data class ShareFile(val uri: Uri):UiEvents()
  }
}