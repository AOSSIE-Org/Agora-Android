package org.aossie.agoraandroid.ui.activities.castVote

import android.content.Intent
import android.os.Bundle
import android.util.Log.getStackTraceString
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_cast_vote.btn_cast_vote
import kotlinx.android.synthetic.main.activity_cast_vote.constraintLayout
import kotlinx.android.synthetic.main.activity_cast_vote.progress_bar
import kotlinx.android.synthetic.main.activity_cast_vote.rv_candidates
import kotlinx.android.synthetic.main.activity_cast_vote.rv_selected_candidates
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.SelectCandidateAdapter
import org.aossie.agoraandroid.adapters.UpvotedCandidateAdapter
import org.aossie.agoraandroid.databinding.ActivityCastVoteBinding
import org.aossie.agoraandroid.ui.activities.MainActivity
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.CandidateRecyclerAdapterCallback
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import timber.log.Timber

private const val ACTIVE_ELECTION_LABEL = "ACTIVE"
private const val PENDING_ELECTION_LABEL = "PENDING"
private const val FINISHED_ELECTION_LABEL = "FINISHED"

class CastVoteActivity :
  AppCompatActivity(),
  CandidateRecyclerAdapterCallback {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var binding: ActivityCastVoteBinding

  private var passCode: String? = null
  private var id: String? = null

  private lateinit var candidates: ArrayList<String>
  private lateinit var hashMap: HashMap<String, Boolean>
  private lateinit var candidateAdapter: SelectCandidateAdapter

  private lateinit var selectedCandidates: ArrayList<String>
  private lateinit var upvotedCandidateAdapter: UpvotedCandidateAdapter

  private val viewModel: CastVoteViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreate(savedInstanceState: Bundle?) {

    (application as AgoraApp).appComponent.inject(this)
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cast_vote)

    window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_cast_vote)
    candidates = ArrayList()
    hashMap = HashMap()
    candidateAdapter = SelectCandidateAdapter(candidates, this)

    selectedCandidates = ArrayList()
    upvotedCandidateAdapter = UpvotedCandidateAdapter(selectedCandidates, this)

    rv_candidates.apply {
      layoutManager = LinearLayoutManager(this@CastVoteActivity, VERTICAL, false)
      adapter = candidateAdapter
    }

    rv_selected_candidates.apply {
      layoutManager = LinearLayoutManager(this@CastVoteActivity, VERTICAL, false)
      adapter = upvotedCandidateAdapter
    }

    val encodedURL = intent?.data
    Timber.d(encodedURL.toString())
    if (encodedURL != null) {
      Thread(
        Runnable {
          try {
            progress_bar.show()
            val originalURL = URL(encodedURL.toString())
            val ucon: HttpURLConnection = originalURL.openConnection() as HttpURLConnection
            ucon.instanceFollowRedirects = false
            val resolvedURL = URL(ucon.getHeaderField("Location"))
            val path = resolvedURL.path.toString()
            val strings = path.split("/")
            passCode = strings[3]
            id = strings[2]
            viewModel.verifyVoter(strings[2])
            Timber.d(resolvedURL.path.toString())
          } catch (ex: MalformedURLException) {
            Timber.e(getStackTraceString(ex))
            progress_bar.hide()
          } catch (ex: IOException) {
            Timber.e(getStackTraceString(ex))
            progress_bar.hide()
          }
        }
      ).start()
    }

    btn_cast_vote.setOnClickListener {
      if (selectedCandidates.size == 0) {
        binding.root.snackbar("Please select a candidate first")
      } else {
        AlertDialog.Builder(this)
          .setTitle("Confirmation")
          .setMessage("Please press confirm to cast vote to selected candidates")
          .setPositiveButton("Confirm") { _, _ ->
            progress_bar.show()
            if (selectedCandidates.size == 1) {
              viewModel.castVote(id!!, selectedCandidates[0], passCode!!)
            } else {
              var ballotInput = ""
              for (i in 0 until selectedCandidates.size) {
                ballotInput += if (i == selectedCandidates.size - 1) {
                  selectedCandidates[i]
                } else {
                  selectedCandidates[i] + ">"
                }
              }
              viewModel.castVote(id!!, ballotInput, passCode!!)
            }
          }
          .setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
          }
          .setCancelable(false)
          .create()
          .show()
      }
    }

    viewModel.verifyVoterResponse.observe(
      this,
      Observer {
        handleVerifyVoter(it)
      }
    )

    viewModel.castVoteResponse.observe(
      this,
      Observer {
        handleCastVote(it)
      }
    )
  }

  private fun handleCastVote(response: ResponseResult) = when (response) {
    is Success<*> -> {
      progress_bar.hide()
      AlertDialog.Builder(this)
        .setTitle(response.data.toString())
        .setMessage("Do you want to move to Home Screen ?")
        .setPositiveButton("Yes") { _, _ ->
          startActivity(Intent(this, MainActivity::class.java))
        }
        .setNegativeButton("No") { _, _ ->
          android.os.Process.killProcess(android.os.Process.myPid())
        }
        .setCancelable(false)
        .create()
        .show()
    }
    is Error<*> -> {
      binding.root.snackbar(response.error.toString())
      progress_bar.hide()
    }
  }

  private fun handleVerifyVoter(response: ResponseResult) = when (response) {
    is Success<*> -> {
      viewModel.election.observe(
        this,
        Observer {
          Timber.d(it.toString())
          binding.election = it
          candidates.clear()
          candidates.addAll(it.candidates!!)
          candidateAdapter.notifyDataSetChanged()
          if (it != null) {
            try {
              val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
              val formattedStartingDate: Date = formatter.parse(it.startingDate!!) as Date
              val formattedEndingDate: Date = formatter.parse(it.endingDate!!) as Date
              val currentDate = Calendar.getInstance().time
              val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)
              // set end and start date
              binding.tvEndDate.text = outFormat.format(formattedEndingDate)
              binding.tvStartDate.text = outFormat.format(formattedStartingDate)
              // set label color and election status
              if (currentDate.before(formattedStartingDate)) {
                binding.label.text = PENDING_ELECTION_LABEL
                binding.label.setBackgroundResource(R.drawable.pending_election_label)
              } else if (currentDate.after(formattedStartingDate) && currentDate.before(
                  formattedEndingDate
                )
              ) {
                binding.label.text = ACTIVE_ELECTION_LABEL
                binding.label.setBackgroundResource(R.drawable.active_election_label)
              } else if (currentDate.after(formattedEndingDate)) {
                binding.label.text = FINISHED_ELECTION_LABEL
                binding.label.setBackgroundResource(R.drawable.finished_election_label)
              }
            } catch (e: ParseException) {
              e.printStackTrace()
            }
          }
          constraintLayout.visibility = View.VISIBLE
          progress_bar.hide()
        }
      )
    }
    is Error<*> -> {
      binding.root.snackbar(response.error.toString())
      progress_bar.hide()
    }
  }

  override fun onItemClicked(
    name: String,
    itemView: TextView,
    requestCode: Int
  ) {
    if (requestCode == AppConstants.CANDIDATE_ITEM_CLICKED) {
      candidates.remove(name)
      selectedCandidates.add(name)
      candidateAdapter.notifyDataSetChanged()
      upvotedCandidateAdapter.notifyDataSetChanged()
    } else if (requestCode == AppConstants.UPVOTED_CANDIDATE_ITEM_CLICKED) {
      selectedCandidates.remove(name)
      candidates.add(name)
      upvotedCandidateAdapter.notifyDataSetChanged()
      candidateAdapter.notifyDataSetChanged()
    }
  }
}
