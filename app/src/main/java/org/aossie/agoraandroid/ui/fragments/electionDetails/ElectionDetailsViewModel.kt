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
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.data.dto.WinnerDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.FileUtils
import org.aossie.agoraandroid.utilities.NoInternetException
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
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val mVoterResponse = MutableLiveData<List<VotersDto>>()
  var voterResponse: LiveData<List<VotersDto>> = mVoterResponse
  private val mNotConnected = MutableLiveData<Boolean>()
  var notConnected: LiveData<Boolean> = mNotConnected
  private val mBallotResponse = MutableLiveData<List<BallotDto>>()
  var ballotResponse: LiveData<List<BallotDto>> = mBallotResponse
  private val mResultResponse = MutableLiveData<WinnerDto>()
  var resultResponse: LiveData<WinnerDto> = mResultResponse

  lateinit var displayElectionListener: DisplayElectionListener
  lateinit var shareResultListener: ShareResultListener

  suspend fun getElectionById(id: String): LiveData<Election> {
    return electionsRepository.getElectionById(id)
  }

  fun getBallot(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.getBallots(id!!).ballots
        Timber.d(response.toString())
        mBallotResponse.postValue(response)
        displayElectionListener.onSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun getVoter(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.getVoters(id!!)
        Timber.d(response.toString())
        mVoterResponse.postValue(response)
        displayElectionListener.onSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun deleteElection(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.deleteElection(id!!)
        Timber.d(response.toString())
        displayElectionListener.onSuccess(response[1])
        displayElectionListener.onDeleteElectionSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onSessionExpired()
      } catch (e: NoInternetException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun getResult(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.getResult(id!!)
        mResultResponse.postValue(response?.get(0))
        displayElectionListener.onSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
        displayElectionListener.onFailure(e.message!!)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun createImage(
    context: Context,
    bitmap: Bitmap
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      FileUtils.saveBitmap(context, bitmap)
        ?.let {
          shareResultListener.onShareSuccess(it)
        } ?: run {
        shareResultListener.onShareExportFailure(
          context.getString(string.something_went_wrong_please_try_again_later)
        )
      }
    }
  }

  fun createExcelFile(
    context: Context,
    winnerDto: WinnerDto,
    id: String
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      val workbook = createWorkbook(context, winnerDto)
      writeToExcelFile(context, workbook, id)
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
      .setCellValue("Candidate")
    title.createCell(1)
      .setCellValue("Votes")
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
  ) {
    val folderName = context.getExternalFilesDir(null)?.absolutePath
    val folder = File("$folderName", "Results")
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
      val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
      )
      shareResultListener.onExportSuccess(uri)
    } catch (e: FileNotFoundException) {
      shareResultListener.onShareExportFailure(context.getString(string.file_not_available))
    } catch (e: IOException) {
      shareResultListener.onShareExportFailure(context.getString(string.cannot_write_file))
    }
  }
}
