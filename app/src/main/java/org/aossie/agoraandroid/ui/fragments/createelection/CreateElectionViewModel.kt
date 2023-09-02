package org.aossie.agoraandroid.ui.fragments.createelection

import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
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
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.useCases.createElection.CreateElectionUseCase
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.AddCandidate
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.CreateElectionsClick
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.DeleteCandidate
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.EnteredElectionDescription
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.EnteredElectionName
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.OpenCSVFileClick
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedBallotVisibility
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedEndDateTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedInvite
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedRealTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedStartDateTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedVoterListVisibility
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedVotingAlgorithm
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SnackBarActionClick
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class CreateElectionViewModel
@Inject
constructor(
  private val createElectionUseCase: CreateElectionUseCase
) : ViewModel() {

  private val _createElectionDataState = MutableStateFlow (ElectionDtoModel())
  val createElectionDataState = _createElectionDataState.asStateFlow()

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  private val _uiEvents = MutableSharedFlow<CreateElectionUiEvents>()
  val uiEvents = _uiEvents.asSharedFlow()

  init {
    _createElectionDataState.value = createElectionDataState.value.copy(
      isRealTime = false,
      isInvite = false,
      voterListVisibility = false,
      votingAlgo = "Oklahoma",
      ballotVisibility = "Completely secret and never shown to anyone"
    )
  }

  fun createElection(election: ElectionDtoModel) {
    showLoading("Creating Election...")
    viewModelScope.launch {
      try {
        val response = createElectionUseCase(election)
        showMessage(response[1])
        delay(1000)
        _uiEvents.emit(CreateElectionUiEvents.ElectionCreated)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  private fun readExcelData(
    context: Context,
    excelFilePath: String
  ) {
    showLoading("Reading CSV file...")
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
        val list: ArrayList<String> = ArrayList()
        for (row in sheet.rowIterator()) {
          row.getCell(0)
            ?.let {
              if (it.stringCellValue.isNotEmpty()) list.add(it.stringCellValue)
            }
        }
        withContext(Dispatchers.Main) {
          hideLoading()
          _createElectionDataState.value = createElectionDataState.value.copy(
            candidates = list
          )
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

  @RequiresApi(VERSION_CODES.O) fun onEvent(event: CreateElectionScreenEvent) {
    when(event){
      is AddCandidate -> {
        if(createElectionDataState.value.candidates.isNullOrEmpty()){
          _createElectionDataState.value = createElectionDataState.value.copy(
            candidates = listOf(event.candidateName)
          )
        }else{
          _createElectionDataState.value = createElectionDataState.value.copy(
            candidates = createElectionDataState.value.candidates!! + event.candidateName
          )
        }

      }
      is DeleteCandidate -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          candidates = createElectionDataState.value.candidates!! - event.candidateName
        )
      }
      is EnteredElectionDescription -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          description = event.electionDescription
        )
      }
      is EnteredElectionName -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          name = event.electionName
        )
      }
      is SelectedBallotVisibility -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          ballotVisibility = event.ballotVisibility
        )
      }
      is SelectedEndDateTime -> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val endDateTime = LocalDateTime.parse(event.endDateTime, formatter)
        if(createElectionDataState.value.startingDate.isNullOrEmpty()){
          showMessage(string.end_date_after_starting_date)
        }else {
          val startDateTime = LocalDateTime.parse(createElectionDataState.value.startingDate, formatter)
          val nowDateTime = LocalDateTime.now()
          if (endDateTime.isBefore(startDateTime) || endDateTime.isBefore(nowDateTime)) {
            showMessage(string.end_date_after_starting_date)
          } else {
            _createElectionDataState.value = createElectionDataState.value.copy(
              endingDate = event.endDateTime
            )
          }
        }
      }
      is SelectedInvite -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          isInvite = event.invite
        )
      }
      is SelectedRealTime -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          isRealTime = event.realTime
        )
      }
      is SelectedStartDateTime -> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val startDateTime = LocalDateTime.parse(event.startDateTime, formatter)
        val nowDateTime = LocalDateTime.now()
        if (startDateTime.isBefore(nowDateTime)) {
          showMessage(string.start_date_after_current_date)
        }else {
          _createElectionDataState.value = createElectionDataState.value.copy(
            startingDate = event.startDateTime
          )
        }
      }
      is SelectedVoterListVisibility -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          voterListVisibility = event.voterListVisibility
        )
      }
      is SelectedVotingAlgorithm -> {
        _createElectionDataState.value = createElectionDataState.value.copy(
          votingAlgo = event.votingAlgorithm
        )
      }
      SnackBarActionClick -> { hideSnackBar() }
      CreateElectionsClick -> {
        if(validate(createElectionDataState.value)){
          createElection(
            createElectionDataState.value.copy(
              ballot = emptyList(),
              electionType = "Election",
              noVacancies = 1
            )
          )
        }
      }
      is OpenCSVFileClick -> {
        readExcelData(context = event.context,event.filePath)
      }
      else -> {}
    }
  }

  private fun validate(electionDtoModel: ElectionDtoModel): Boolean {
    if(electionDtoModel.name.isNullOrEmpty() || electionDtoModel.name.trim().isEmpty()){
      showMessage(string.invalid_election_name)
      return false
    }
    if(electionDtoModel.description.isNullOrEmpty() || electionDtoModel.description.trim().isEmpty()){
      showMessage(string.invalid_election_description)
      return false
    }
    if(electionDtoModel.candidates.isNullOrEmpty()){
      showMessage(string.add_one_candidate)
      return false
    }
    if(electionDtoModel.startingDate.isNullOrEmpty()){
      showMessage(string.invalid_start_date)
      return false
    }
    if(electionDtoModel.endingDate.isNullOrEmpty()){
      showMessage(string.invalid_end_date)
      return false
    }
    return true
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
  
  sealed class CreateElectionUiEvents {
    object ElectionCreated: CreateElectionUiEvents()
  }
}
