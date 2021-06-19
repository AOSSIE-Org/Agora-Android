package org.aossie.agoraandroid.ui.activities.castVote

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog.Builder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.facebook.login.LoginManager
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.adapters.CandidatesAdapter
import org.aossie.agoraandroid.adapters.SelectedCandidateAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.SessionExpired
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.databinding.ActivityCastVoteBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivity
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.CandidateRecyclerAdapterCallback
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.isConnected
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CastVoteActivity :
  AppCompatActivity(),
  CandidateRecyclerAdapterCallback,
  GetResolvedPathListener {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var binding: ActivityCastVoteBinding

  private var passCode: String? = null
  private var id: String? = null

  private lateinit var candidates: ArrayList<String>
  private lateinit var candidatesAdapter: CandidatesAdapter

  private lateinit var selectedCandidates: ArrayList<String>
  private lateinit var selectedCandidateAdapter: SelectedCandidateAdapter

  @Inject
  lateinit var prefs: PreferenceProvider

  private val viewModel: CastVoteViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    (application as AgoraApp).appComponent.inject(this)
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_cast_vote)
    viewModel.getResolvedPathListener = this
    initObservers()
    processURL()
  }

  private fun init(
    candidatesList: List<String>,
    isActive: Boolean
  ) {
    initObjects(candidatesList,isActive)
    initView(isActive)
    initListeners()
  }

  private fun initObjects(
    candidatesList: List<String>,
    isActive: Boolean
  ) {
    candidates = ArrayList(candidatesList)
    selectedCandidates = ArrayList()
    candidatesAdapter = CandidatesAdapter(candidates, isActive, this)
    selectedCandidateAdapter = SelectedCandidateAdapter(selectedCandidates, this)
  }

  private fun initView(isActive: Boolean) {
    binding.tvSelectCandidate.apply {
      text = if(isActive){
        getString(string.please_select_candidate)
      }else{
        getString(string.candidates)
      }
    }
    binding.rvCandidates.apply {
      layoutManager = LinearLayoutManager(this@CastVoteActivity, VERTICAL, false)
      adapter = candidatesAdapter
    }
    binding.rvSelectedCandidates.apply {
      layoutManager = LinearLayoutManager(this@CastVoteActivity, VERTICAL, false)
      adapter = selectedCandidateAdapter
    }
  }

  private fun initListeners() {
    binding.btnCastVote.setOnClickListener {
      if (selectedCandidates.size == 0) {
        binding.root.snackbar(getString(string.select_candidate))
      } else {
        Builder(this)
          .setTitle(getString(string.confirm_vote))
          .setMessage(getString(string.confirm_vote_message))
          .setPositiveButton(getString(string.confirm_button)) { _, _ ->
            binding.progressBar.show()
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
          .setNegativeButton(getString(string.cancel_button)) { dialog, _ ->
            dialog.cancel()
          }
          .setCancelable(false)
          .create()
          .show()
      }
    }
  }

  private fun initObservers() {
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
    is Success -> {
      navigateToMainActivity(getString(string.vote_successful))
    }
    is Error -> {
      binding.progressBar.hide()
      binding.root.snackbar(response.error.toString())
    }
    is SessionExpired -> {
      // do nothing
    }
  }

  private fun handleVerifyVoter(response: ResponseResult) = when (response) {
    is Success -> {
      viewModel.election.observe(
        this,
        Observer {
          Timber.d(it.toString())
          binding.election = it
          if (it != null) {
            try {
              val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
              val formattedStartingDate: Date = formatter.parse(it.startingDate!!) as Date
              val formattedEndingDate: Date = formatter.parse(it.endingDate!!) as Date
              val currentDate = Calendar.getInstance().time
              val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)

              init(it.candidates!!, (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)))
              notifyStatusToUser(currentDate, formattedStartingDate, formattedEndingDate)

              // set end and start date
              binding.tvEndDate.text = outFormat.format(formattedEndingDate)
              binding.tvStartDate.text = outFormat.format(formattedStartingDate)
              // set label color and election status
              binding.label.text = getEventStatus(currentDate, formattedStartingDate, formattedEndingDate)
              binding.label.setBackgroundResource(getEventColor(currentDate, formattedStartingDate, formattedEndingDate))
            } catch (e: ParseException) {
              e.printStackTrace()
            }
          }
          binding.constraintLayout.visibility = View.VISIBLE
          binding.progressBar.hide()
        }
      )
    }
    is Error -> {
      navigateToMainActivity(response.error.toString())
    }
    is SessionExpired -> {
      // do nothing
    }
  }

  private fun notifyStatusToUser(
    currentDate: Date,
    formattedStartingDate: Date,
    formattedEndingDate: Date
  ) {
    when {
      currentDate.before(formattedStartingDate) -> {
        binding.voteLayout.visibility = View.GONE
        binding.root.snackbar(getString(string.election_not_started))
      }
      currentDate.after(formattedStartingDate) && currentDate.before(
        formattedEndingDate
      ) -> {
        binding.voteLayout.visibility = View.VISIBLE
      }
      currentDate.after(formattedEndingDate) -> {
        binding.voteLayout.visibility = View.GONE
        binding.root.snackbar(getString(string.election_finished))
      }
      else -> {
      }
    }
  }

  private fun getEventStatus(
    currentDate: Date,
    formattedStartingDate: Date?,
    formattedEndingDate: Date?
  ): String? {
    return when {
      currentDate.before(formattedStartingDate) -> AppConstants.PENDING
      currentDate.after(formattedStartingDate) && currentDate.before(
        formattedEndingDate
      ) -> AppConstants.ACTIVE
      currentDate.after(formattedEndingDate) -> AppConstants.FINISHED
      else -> null
    }
  }

  private fun getEventColor(
    currentDate: Date,
    formattedStartingDate: Date?,
    formattedEndingDate: Date?
  ): Int {
    return when {
      currentDate.before(formattedStartingDate) -> drawable.pending_election_label
      currentDate.after(formattedStartingDate) && currentDate.before(
        formattedEndingDate
      ) -> drawable.active_election_label
      currentDate.after(formattedEndingDate) -> drawable.finished_election_label
      else -> drawable.finished_election_label
    }
  }

  private fun processURL() {
    if (isConnected()) {
      val encodedURL = intent?.data
      if (encodedURL != null) {
        viewModel.getResolvedPath(encodedURL.toString())
      } else {
        navigateToMainActivity(getString(string.invalid_url))
      }
    } else {
      navigateToMainActivity(getString(string.no_network))
    }
  }

  private fun navigateToMainActivity(message: String) {
    binding.progressBar.hide()
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra(AppConstants.SHOW_SNACKBAR_KEY, message)
    startActivity(intent)
    finish()
  }

  override fun onItemClicked(
    name: String,
    itemView: TextView,
    requestCode: Int
  ) {
    if (requestCode == AppConstants.CANDIDATE_ITEM_CLICKED) {
      candidates.remove(name)
      selectedCandidates.add(name)
      candidatesAdapter.notifyDataSetChanged()
      selectedCandidateAdapter.notifyDataSetChanged()
    } else if (requestCode == AppConstants.UPVOTED_CANDIDATE_ITEM_CLICKED) {
      selectedCandidates.remove(name)
      candidates.add(name)
      selectedCandidateAdapter.notifyDataSetChanged()
      candidatesAdapter.notifyDataSetChanged()
    }
  }

  override fun onStarted() {
    binding.progressBar.show()
  }

  override fun onSuccess(message: String) {
    val strings = message.split("/")
    passCode = strings[3]
    id = strings[2]
    viewModel.verifyVoter(strings[2])
  }

  override fun onFailure(message: String?) {
    navigateToMainActivity(message ?: getString(string.invalid_url))
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    startActivity(intent)
    finish()
  }
}
