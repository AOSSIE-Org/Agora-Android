package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.utilities.ApiException
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
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {
  lateinit var inviteVoterListener: InviteVoterListener
  lateinit var readVotersListener: ReadVotersListener
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
    inviteVoterListener.onStarted()
    viewModelScope.launch {
      try {
        val response = electionsRepository.sendVoters(id, body)
        Timber.d(response.toString())
        inviteVoterListener.onSuccess(response[1])
      } catch (e: ApiException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        inviteVoterListener.onSessionExpired()
      } catch (e: NoInternetException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: Exception) {
        inviteVoterListener.onFailure(e.message!!)
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
      email.isNotEmpty() && email.matches(emailPattern.toRegex()) && !getEmailList(mVoters).contains(email)
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
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val inputStream: InputStream = FileInputStream(File(excelFilePath))
        val workbook = XSSFWorkbook(inputStream)
        val sheet: XSSFSheet = workbook.getSheetAt(0) ?: run {
          withContext(Dispatchers.Main) {
            readVotersListener.onReadFailure(context.getString(string.no_sheet))
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
          readVotersListener.onReadSuccess(list)
        }
      } catch (e: NotOfficeXmlFileException) {
        withContext(Dispatchers.Main) {
          readVotersListener.onReadFailure(context.getString(string.not_excel))
        }
      } catch (e: FileNotFoundException) {
        withContext(Dispatchers.Main) {
          readVotersListener.onReadFailure(context.getString(string.file_not_available))
        }
      } catch (e: IOException) {
        withContext(Dispatchers.Main) {
          readVotersListener.onReadFailure(context.getString(string.cannot_read_file))
        }
      }
    }
  }
}
