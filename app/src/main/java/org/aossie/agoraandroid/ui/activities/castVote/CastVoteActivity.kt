package org.aossie.agoraandroid.ui.activities.castVote

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_cast_vote.progress_bar
import kotlinx.android.synthetic.main.activity_cast_vote.tv_link
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Success
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Error
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class CastVoteActivity : AppCompatActivity() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  lateinit var rootView: View

  private val viewModel: CastVoteViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    (application as AgoraApp).appComponent.inject(this)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cast_vote)

    rootView = window.decorView.rootView

    val encodedURL = intent?.data
    Log.d("friday", encodedURL.toString())
    if (encodedURL != null) {
      Log.d("App Link", encodedURL.toString())
      Thread(Runnable {
        try {
          progress_bar.show()
          val originalURL = URL(encodedURL.toString())
          val ucon: HttpURLConnection = originalURL.openConnection() as HttpURLConnection
          ucon.instanceFollowRedirects = false
          val resolvedURL = URL(ucon.getHeaderField("Location"))
          val path = resolvedURL.path.toString()
          val strings = path.split("/")
          viewModel.verifyVoter(strings[2], strings[3])
          Log.d("App Link", resolvedURL.toString())
          Log.d("App Link", resolvedURL.path.toString())
          Log.d("App Link", strings.toString())
        } catch (ex: MalformedURLException) {
          Log.e("App Link", Log.getStackTraceString(ex))
          progress_bar.hide()
        } catch (ex: IOException) {
          Log.e("App Link", Log.getStackTraceString(ex))
          progress_bar.hide()
        }
      }).start()
    }

    viewModel.verifyVoterResponse.observe(this, Observer {
      handleVerifyVoter(it)
    })
  }

  private fun handleVerifyVoter(response: ResponseResults) = when(response) {
    is Success -> {
      viewModel.election.observe(this, Observer {
        tv_link.text = it.toString()
        progress_bar.hide()
      })
    }
    is Error -> {
      rootView.snackbar(response.message)
      progress_bar.hide()
    }
  }

}