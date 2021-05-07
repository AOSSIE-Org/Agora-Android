package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_election_details.view.button_ballot
import kotlinx.android.synthetic.main.fragment_election_details.view.button_delete
import kotlinx.android.synthetic.main.fragment_election_details.view.button_invite_voters
import kotlinx.android.synthetic.main.fragment_election_details.view.button_result
import kotlinx.android.synthetic.main.fragment_election_details.view.button_voters
import kotlinx.android.synthetic.main.fragment_election_details.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentElectionDetailsBinding
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.isConnected
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */

private const val ACTIVE_ELECTION_LABEL = "ACTIVE"
private const val PENDING_ELECTION_LABEL = "PENDING"
private const val FINISHED_ELECTION_LABEL = "FINISHED"

class ElectionDetailsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : Fragment(),
  DisplayElectionListener {
  lateinit var binding: FragmentElectionDetailsBinding
  private var id: String? = null
  private var status: String? = null
  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_election_details, container, false)
    val args =
      ElectionDetailsFragmentArgs.fromBundle(
        requireArguments()
      )
    id = args.id
    electionDetailsViewModel.displayElectionListener = this
    binding.root.button_ballot.setOnClickListener {
      val action =
        ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToBallotFragment(
          id!!
        )
      Navigation.findNavController(binding.root)
        .navigate(action)
    }
    binding.root.button_voters.setOnClickListener {
      val action =
        ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToVotersFragment(
          id!!
        )
      Navigation.findNavController(binding.root)
        .navigate(action)
    }
    binding.root.button_invite_voters.setOnClickListener {
      if (status == "FINISHED") {
        binding.root.snackbar(resources.getString(R.string.election_finished))
      } else {
        val action =
          ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToInviteVotersFragment(
            id!!
          )
        Navigation.findNavController(binding.root)
          .navigate(action)
      }
    }
    binding.root.button_result.setOnClickListener {
      if (status == "PENDING") {
        binding.root.snackbar(resources.getString(R.string.election_not_started))
      } else {
        if (requireContext().isConnected()) {
          val action =
            ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToResultFragment(
              id!!
            )
          Navigation.findNavController(binding.root)
            .navigate(action)
        } else {
          binding.root.snackbar(resources.getString(R.string.no_network))
        }
      }
    }

    binding.root.button_delete.setOnClickListener {
      when (status) {
        "ACTIVE" -> binding.root.snackbar(
          resources.getString(R.string.active_elections_not_started)
        )
        "FINISHED" -> electionDetailsViewModel.deleteElection(id)
        "PENDING" -> electionDetailsViewModel.deleteElection(id)
      }
    }

    getElectionById()

    return binding.root
  }

  private fun getElectionById() {
    Coroutines.main {
      electionDetailsViewModel.getElectionById(id!!)
        .observe(
          viewLifecycleOwner,
          {
            if (it != null) {
              Timber.d(it.toString())
              binding.election = it
              try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                val formattedStartingDate: Date = formatter.parse(it.start!!) as Date
                val formattedEndingDate: Date = formatter.parse(it.end!!) as Date
                val currentDate = Calendar.getInstance()
                  .time
                val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)
                // set end and start date
                binding.tvEndDate.text = outFormat.format(formattedEndingDate)
                binding.tvStartDate.text = outFormat.format(formattedStartingDate)
                // set label color and election status
                if (currentDate.before(formattedStartingDate)) {
                  binding.label.text =
                    PENDING_ELECTION_LABEL
                  status = "PENDING"
                  binding.label.setBackgroundResource(R.drawable.pending_election_label)
                } else if (currentDate.after(formattedStartingDate) && currentDate.before(
                    formattedEndingDate
                  )
                ) {
                  binding.label.text =
                    ACTIVE_ELECTION_LABEL
                  status = "ACTIVE"
                  binding.label.setBackgroundResource(R.drawable.active_election_label)
                } else if (currentDate.after(formattedEndingDate)) {
                  binding.label.text =
                    FINISHED_ELECTION_LABEL
                  status = "FINISHED"
                  binding.label.setBackgroundResource(R.drawable.finished_election_label)
                }
              } catch (e: ParseException) {
                e.printStackTrace()
              }
              // add candidates name
              val mCandidatesName = StringBuilder()
              val candidates = it.candidates
              if (candidates != null) {
                for (j in 0 until candidates.size) {
                  mCandidatesName.append(candidates[j])
                  if (j != candidates.size - 1) {
                    mCandidatesName.append(", ")
                  }
                }
              }
              binding.tvCandidateList.text = mCandidatesName
              binding.executePendingBindings()
            }
          }
        )
    }
  }

  override fun onDeleteElectionSuccess() {
    prefs.setUpdateNeeded(true)
    Navigation.findNavController(binding.root)
      .navigate(
        ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToHomeFragment()
      )
  }

  override fun onSuccess(message: String?) {
    if (message != null) binding.root.snackbar(message)
    binding.root.progress_bar.hide()
    binding.root.button_delete.toggleIsEnable()
  }

  override fun onStarted() {
    binding.root.progress_bar.show()
    binding.root.button_delete.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    binding.root.snackbar(message)
    binding.root.progress_bar.hide()
    binding.root.button_delete.toggleIsEnable()
  }
}
