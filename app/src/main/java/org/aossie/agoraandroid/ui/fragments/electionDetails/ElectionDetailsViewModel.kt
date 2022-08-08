package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.network.dto.BallotDto
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
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
import javax.inject.Inject

class ElectionDetailsViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepositoryImpl
) : ViewModel() {

  private val _getVoterResponseLiveData = MutableLiveData<ResponseUI<VotersDto>>()
  var getVoterResponseLiveData: LiveData<ResponseUI<VotersDto>> = _getVoterResponseLiveData
  private val _getBallotResponseLiveData = MutableLiveData<ResponseUI<BallotDto>>()
  var getBallotResponseLiveData: LiveData<ResponseUI<BallotDto>> = _getBallotResponseLiveData
  private val _getShareResponseLiveData = MutableLiveData<ResponseUI<Uri>>()
  var getShareResponseLiveData: LiveData<ResponseUI<Uri>> = _getShareResponseLiveData
  private val _getResultResponseLiveData = MutableLiveData<ResponseUI<WinnerDto>>()
  var getResultResponseLiveData: LiveData<ResponseUI<WinnerDto>> = _getResultResponseLiveData
  private val _getDeleteElectionLiveData = MutableLiveData<ResponseUI<WinnerDto>>()
  var getDeleteElectionLiveData: LiveData<ResponseUI<WinnerDto>> = _getDeleteElectionLiveData

  lateinit var sessionExpiredListener: SessionExpiredListener

  fun getElectionById(id: String): LiveData<Election> {
    return electionsRepository.getElectionById(id)
  }

  fun getBallot(
    id: String?
  ) {
    _getBallotResponseLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response: List<BallotDto> = electionsRepository.getBallots(id!!).ballots
        Timber.d(response.toString())
        _getBallotResponseLiveData.value = ResponseUI.success(response)
      } catch (e: ApiException) {
        _getBallotResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getBallotResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getBallotResponseLiveData.value = ResponseUI.error(e.message)
      }
    }
  }

  fun getVoter(
    id: String?
  ) {
    _getVoterResponseLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = electionsRepository.getVoters(id!!)
        Timber.d(response.toString())
        _getVoterResponseLiveData.value = ResponseUI.success(response)
      } catch (e: ApiException) {
        _getVoterResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getVoterResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getVoterResponseLiveData.value = ResponseUI.error(e.message)
      }
    }
  }

  fun deleteElection(
    id: String?
  ) {
    _getDeleteElectionLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = electionsRepository.deleteElection(id!!)
        Timber.d(response.toString())
        _getDeleteElectionLiveData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message)
      }
    }
  }

  fun getResult(
    id: String?
  ) {
    _getResultResponseLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = electionsRepository.getResult(id!!)
        if (!response.isNullOrEmpty())
          _getResultResponseLiveData.value = ResponseUI.success(response[0])
        else
          _getResultResponseLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getResultResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getResultResponseLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getResultResponseLiveData.value = ResponseUI.error(e.message)
      }
    }
  }

  fun createImage(
    context: Context,
    bitmap: Bitmap
  ) {
    viewModelScope.launch {
      val btm = withContext(Dispatchers.IO) {
        FileUtils.saveBitmap(context, bitmap)
      }
      btm?.let {
        _getShareResponseLiveData.value = ResponseUI.success(it)
      } ?: run {
        _getShareResponseLiveData.value =
          ResponseUI.error(context.getString(string.something_went_wrong_please_try_again_later))
      }
    }
  }

  fun createExcelFile(
    context: Context,
    winnerDto: WinnerDto,
    id: String
  ) {
    viewModelScope.launch {
      val workbook = withContext(Dispatchers.Default) {
        createWorkbook(context, winnerDto)
      }
      try {
        val uri = withContext(Dispatchers.IO) {
          writeToExcelFile(context, workbook, id)
        }
        _getShareResponseLiveData.value = ResponseUI.success(uri)
      } catch (e: FileNotFoundException) {
        _getShareResponseLiveData.value =
          ResponseUI.error(context.getString(string.file_not_available))
      } catch (e: IOException) {
        _getShareResponseLiveData.value =
          ResponseUI.error(context.getString(string.cannot_write_file))
      } catch (e: Exception) {
        _getShareResponseLiveData.value =
          ResponseUI.error(context.getString(string.something_went_wrong_please_try_again_later))
      }
    }
  }

  private fun createWorkbook(
    context: Context,
    winnerDto: WinnerDto
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
}
