package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_invite_voters.view.button_add_voter
import kotlinx.android.synthetic.main.fragment_invite_voters.view.button_invite_voter
import kotlinx.android.synthetic.main.fragment_invite_voters.view.progress_bar
import kotlinx.android.synthetic.main.fragment_invite_voters.view.recycler_view_voters
import kotlinx.android.synthetic.main.fragment_invite_voters.view.text_input_voter_email
import kotlinx.android.synthetic.main.fragment_invite_voters.view.text_input_voter_name
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.adapters.TextWatcherAdapter
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import org.json.JSONException
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class InviteVotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : Fragment(), InviteVoterListener {

  private lateinit var rootView: View

  var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

  private val mVoterNames = ArrayList<String>()
  private val mVoterEmails = ArrayList<String>()
  private val inviteVotersViewModel: InviteVotersViewModel by viewModels {
    viewModelFactory
  }
  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }
  private var voterRecyclerAdapter: VoterRecyclerAdapter? = null
  private val itemTouchHelperCallback: SimpleCallback =
    object : SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(
        viewHolder: ViewHolder,
        direction: Int
      ) {
        val lastName = mVoterNames[viewHolder.absoluteAdapterPosition]
        val lastEmail = mVoterEmails[viewHolder.absoluteAdapterPosition]
        Snackbar.make(rootView, R.string.voter_removed, Snackbar.LENGTH_LONG)
          .setAction(AppConstants.undo) {
            addCandidate(lastName, lastEmail)
          }.show()

        mVoterNames.removeAt(viewHolder.absoluteAdapterPosition)
        mVoterEmails.removeAt(viewHolder.absoluteAdapterPosition)
        voterRecyclerAdapter!!.notifyDataSetChanged()
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_invite_voters, container, false)

    inviteVotersViewModel.inviteVoterListener = this

    voterRecyclerAdapter = VoterRecyclerAdapter(mVoterNames, mVoterEmails)
    rootView.recycler_view_voters.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rootView.recycler_view_voters)
    rootView.recycler_view_voters.adapter = voterRecyclerAdapter
    rootView.text_input_voter_name.editText
      ?.addTextChangedListener(object : TextWatcherAdapter() {
        override fun onTextChanged(
          p0: CharSequence?,
          p1: Int,
          p2: Int,
          p3: Int
        ) {
          if (rootView.text_input_voter_name.editText!!.text.isNotEmpty()) {
            rootView.text_input_voter_name.error = null
          }
        }
      })
    rootView.text_input_voter_email.editText
      ?.addTextChangedListener(object : TextWatcherAdapter() {
        override fun onTextChanged(
          p0: CharSequence?,
          p1: Int,
          p2: Int,
          p3: Int
        ) {
          if (rootView.text_input_voter_email.editText!!.text.isNotEmpty()) {
            rootView.text_input_voter_email.error = null
          }
        }
      })

    rootView.button_invite_voter.setOnClickListener {
      try {
        val inviteVotersFragmentArgs = InviteVotersFragmentArgs.fromBundle(requireArguments())
        val id: String = inviteVotersFragmentArgs.id
        inviteVotersViewModel.inviteVoters(mVoterNames, mVoterEmails, id)
      } catch (e: JSONException) {
        e.printStackTrace()
      }
    }

    rootView.button_add_voter.setOnClickListener {
      val name = rootView.text_input_voter_name.editText
        ?.text
        .toString()
      val email = rootView.text_input_voter_email.editText
        ?.text
        .toString()
      if (inviteValidator(email, name, mVoterEmails)) {
        addCandidate(name, email)
      } else {
        rootView.snackbar("Enter valid name and email address")
      }
    }

    return rootView
  }

  private fun addCandidate(
    voterName: String,
    voterEmail: String
  ) {
    mVoterNames.add(voterName)
    mVoterEmails.add(voterEmail)
    voterRecyclerAdapter!!.notifyDataSetChanged()
    rootView.text_input_voter_name.editText?.setText("")
    rootView.text_input_voter_email.editText?.setText("")
  }

  override fun onStarted() {
    rootView.progress_bar.show()
    rootView.button_invite_voter.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.button_invite_voter.toggleIsEnable()
    val mMessage = StringBuilder()
    mMessage.append(message)
    rootView.snackbar(mMessage.toString())
  }

  override fun onSuccess(message: String) {
    rootView.progress_bar.hide()
    rootView.button_invite_voter.toggleIsEnable()
    prefs.setUpdateNeeded(true)
    rootView.snackbar(message)
    Navigation.findNavController(rootView)
      .navigate(InviteVotersFragmentDirections.actionInviteVotersFragmentToHomeFragment())
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }

  private fun emailValidator(
    email: String,
    mVoterEmails: ArrayList<String>
  ): Boolean {
    val base = context as Activity
    if (email.isEmpty()) {
      (
        base.findViewById<View>(
          R.id.text_input_voter_email
        ) as TextInputLayout
        ).error = "Please enter Voter's Email"
      return false
    } else if (mVoterEmails.contains(email)) {
      (
        base.findViewById<View>(
          R.id.text_input_voter_email
        ) as TextInputLayout
        ).error = base.resources
        .getString(string.voter_same_email)
      return false
    } else if (!email.matches(emailPattern.toRegex())) {
      (
        base.findViewById<View>(
          R.id.text_input_voter_email
        ) as TextInputLayout
        ).error = "Please enter valid email address"
      return false
    }
    (
      base.findViewById<View>(
        R.id.text_input_voter_email
      ) as TextInputLayout
      ).error = null
    return true
  }

  private fun nameValidator(name: String): Boolean {
    val base = context as Activity
    if (name.isEmpty()) {
      (
        base.findViewById<View>(
          R.id.text_input_voter_name
        ) as TextInputLayout
        ).error = "Please enter Voter's Name"
      return false
    }
    (
      base.findViewById<View>(
        R.id.text_input_voter_name
      ) as TextInputLayout
      ).error = null
    return true
  }

  private fun inviteValidator(
    email: String,
    name: String,
    mVoterEmails: ArrayList<String>
  ): Boolean {
    val isNameValid = nameValidator(name)
    val isEmailValid = emailValidator(email, mVoterEmails)
    return isNameValid && isEmailValid
  }
}
