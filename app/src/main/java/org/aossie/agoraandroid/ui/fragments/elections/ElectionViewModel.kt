package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.ElectionsUseCases
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class ElectionViewModel
@Inject
constructor(
  private val electionsUseCases: ElectionsUseCases
) : ViewModel() {

  private val _exceptionLiveData = MutableLiveData<String>()
  val exceptionLiveData: LiveData<String>
    get() = _exceptionLiveData

  fun getElections(): Flow<List<ElectionModel>> {
    viewModelScope.launch {
      try {
        electionsUseCases.fetchAndSaveElection()
      } catch (e: NoInternetException) {
        _exceptionLiveData.postValue(e.message) // handling exception from ElectionsRepositoryImpl class
      } catch (e: ApiException) {
        _exceptionLiveData.postValue(e.message)
      } catch (e: SessionExpirationException) {
        _exceptionLiveData.postValue(e.message)
      } catch (e: IOException) {
        _exceptionLiveData.postValue(e.message)
      }
    }
    return electionsUseCases.getElections()
  }

  fun filter(
    mElections: List<ElectionModel>,
    query: String
  ): List<ElectionModel> {
    return mElections.filter {
      it.name?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true ||
        it.description?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true
    }
  }
}
