package org.aossie.agoraandroid.ui.fragments.displayelections

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class DisplayElectionViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private var loadToast: LoadToast? = null
  private val mVoterResponse = MutableLiveData<String?>()
  var voterResponse: LiveData<String?> = mVoterResponse
  private val mBallotResponse = MutableLiveData<String?>()
  var ballotResponse: LiveData<String?> = mBallotResponse
  var displayElectionListener: DisplayElectionListener?= null
  fun getBallot(
    token: String?,
    id: String?
  ) {
    loadToast = LoadToast(context)
    loadToast!!.setText("Getting Details")
    loadToast!!.show()
    val apiService = RetrofitClient.getAPIService()
    val getBallotResponse = apiService.getBallot(token, id)
    getBallotResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          loadToast!!.success()
          mBallotResponse.value = response.body()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        loadToast!!.error()
        Toast.makeText(
            getApplication(),
            "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

  fun getVoter(
    token: String?,
    id: String?
  ) {
    loadToast = LoadToast(context)
    loadToast!!.setText("Getting Voters")
    loadToast!!.show()
    val apiService = RetrofitClient.getAPIService()
    val getVotersResponse = apiService.getVoters(token, id)
    getVotersResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          loadToast!!.success()
          if (response.body() != null) {
            mVoterResponse.postValue(response.body())
          }
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        loadToast!!.error()
        Toast.makeText(
            getApplication(),
            "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

  fun deleteElection(
    token: String?,
    id: String?
  ) {
    loadToast = LoadToast(context)
    loadToast!!.setText("Deleting Election")
    loadToast!!.show()
    val apiService = RetrofitClient.getAPIService()
    val deleteElectionResponse = apiService.deleteElection(token, id)
    deleteElectionResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          loadToast!!.success()
          Toast.makeText(
              getApplication(), "Election Deleted Successfully",
              Toast.LENGTH_SHORT
          )
              .show()
          displayElectionListener?.onDeleteElectionSuccess()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        loadToast!!.show()
        Toast.makeText(
            getApplication(),
            "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

}