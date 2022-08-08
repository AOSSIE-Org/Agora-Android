package org.aossie.agoraandroid.ui.fragments.createelection

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.use_cases.create_election.CreateElectionUseCase
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
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
  private val createElectionUseCase: CreateElectionUseCase
) : ViewModel() {

  private val _getCreateElectionData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getCreateElectionData = _getCreateElectionData
  private val _getImportVotersLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getImportVotersLiveData = _getImportVotersLiveData

  fun createElection(election: ElectionDtoModel) {
    _getCreateElectionData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = createElectionUseCase(election)
        _getCreateElectionData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
        _getCreateElectionData.value = ResponseUI.error(e.message)
      } catch (e: NoInternetException) {
        _getCreateElectionData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getCreateElectionData.value = ResponseUI.error(e.message)
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
            _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.no_sheet))
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
          _getImportVotersLiveData.value = ResponseUI.success(list)
        }
      } catch (e: NotOfficeXmlFileException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.not_excel))
        }
      } catch (e: FileNotFoundException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.file_not_available))
        }
      } catch (e: IOException) {
        withContext(Dispatchers.Main) {
          _getImportVotersLiveData.value = ResponseUI.error(context.getString(string.cannot_read_file))
        }
      }
    }
  }
}
