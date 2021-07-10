package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
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
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val _getSendVoterLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSendVoterLiveData = _getSendVoterLiveData
  private val _getImportVotersLiveData: MutableLiveData<ResponseUI<VotersDto>> = MutableLiveData()
  val getImportVotersLiveData = _getImportVotersLiveData
  lateinit var sessionExpiredListener: SessionExpiredListener
  var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

  fun inviteVoters(
    mVoters: ArrayList<VotersDto>,
    id: String
  ) {

    sendVoters(id, mVoters)
  }

  private fun sendVoters(
    id: String,
    body: List<VotersDto>
  ) {
    _getSendVoterLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = electionsRepository.sendVoters(id, body)
        Timber.d(response.toString())
        _getSendVoterLiveData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message)
      }
    }
  }

  private fun importValidator(
    email: String,
    name: String,
    mVoters: ArrayList<VotersDto>
  ): Boolean {
    val isNameValid = name.isNotEmpty()
    val isEmailValid =
      email.isNotEmpty() && email.matches(emailPattern.toRegex()) && !getEmailList(
        mVoters
      ).contains(email)
    return isNameValid && isEmailValid
  }

  fun getEmailList(mVoters: ArrayList<VotersDto>): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    for (voter in mVoters) {
      voter.voterEmail?.let { list.add(it) }
    }
    return list
  }

  fun readExcelData(
    context: Context,
    excelFilePath: String,
    existingVoters: ArrayList<VotersDto>
  ) {
    _getImportVotersLiveData.value = ResponseUI.loading()
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val inputStream: InputStream = FileInputStream(File(excelFilePath))
        val workbook = XSSFWorkbook(inputStream)
        val sheet: XSSFSheet = workbook.getSheetAt(0) ?: run {
          withContext(Dispatchers.Main) {
            _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.no_sheet))
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
          _getImportVotersLiveData.value = ResponseUI.success(list)
        }
      } catch (e: NotOfficeXmlFileException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.not_excel))
        }
      } catch (e: FileNotFoundException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value =
            ResponseUI.error(context.getString(string.file_not_available))
        }
      } catch (e: IOException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value =
            ResponseUI.error(context.getString(string.cannot_read_file))
        }
      }
    }
  }
}
