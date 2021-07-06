package org.aossie.agoraandroid.ui.fragments.createelection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import javax.inject.Inject

internal class CreateElectionViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  lateinit var createElectionListener: CreateElectionListener
  lateinit var readCandidatesListener: ReadCandidatesListener

  fun createElection(election: ElectionDto) {
    createElectionListener.onStarted()
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val response = electionsRepository.createElection(election)
        createElectionListener.onSuccess(response[1])
      } catch (e: ApiException) {
        createElectionListener.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        createElectionListener.onFailure(e.message!!)
      } catch (e: Exception) {
        createElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun readExcelData(
    context: Context,
    excelFilePath: String
  ) {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val inputStream: InputStream = FileInputStream(File(excelFilePath))
        val workbook = XSSFWorkbook(inputStream)
        val sheet: XSSFSheet = workbook.getSheetAt(0) ?: run {
          withContext(Dispatchers.Main) {
            readCandidatesListener.onReadFailure(context.getString(string.no_sheet))
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
          readCandidatesListener.onReadSuccess(list)
        }
      } catch (e: NotOfficeXmlFileException) {
        withContext(Dispatchers.Main) {
          readCandidatesListener.onReadFailure(context.getString(string.not_excel))
        }
      } catch (e: FileNotFoundException) {
        withContext(Dispatchers.Main) {
          readCandidatesListener.onReadFailure(context.getString(string.file_not_available))
        }
      } catch (e: IOException) {
        withContext(Dispatchers.Main) {
          readCandidatesListener.onReadFailure(context.getString(string.cannot_read_file))
        }
      }
    }
  }
}
