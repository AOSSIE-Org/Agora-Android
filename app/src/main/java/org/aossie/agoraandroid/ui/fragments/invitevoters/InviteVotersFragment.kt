package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentInviteVotersBinding
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

  private lateinit var binding: FragmentInviteVotersBinding

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
        Snackbar.make(binding.root, R.string.voter_removed, Snackbar.LENGTH_LONG)
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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_voters, container, false)

    inviteVotersViewModel.inviteVoterListener = this

    voterRecyclerAdapter = VoterRecyclerAdapter(mVoterNames, mVoterEmails)
    binding.recyclerViewVoters.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerViewVoters)
    binding.recyclerViewVoters.adapter = voterRecyclerAdapter
    binding.textInputVoterName.editText?.doAfterTextChanged {
      if (it.toString().isNotEmpty()) {
        binding.textInputVoterName.error = null
      }
    }
    binding.textInputVoterEmail.editText?.doAfterTextChanged {
      if (it.toString().isNotEmpty()) {
        binding.textInputVoterEmail.error = null
      }
    }

    binding.buttonInviteVoter.setOnClickListener {
      try {
        val inviteVotersFragmentArgs = InviteVotersFragmentArgs.fromBundle(requireArguments())
        val id: String = inviteVotersFragmentArgs.id
        inviteVotersViewModel.inviteVoters(mVoterNames, mVoterEmails, id)
      } catch (e: JSONException) {
        e.printStackTrace()
      }
    }

    binding.buttonAddVoter.setOnClickListener {
      val name = binding.textInputVoterName.editText
        ?.text
        .toString()
      val email = binding.textInputVoterEmail.editText
        ?.text
        .toString()
      if (inviteValidator(email, name, mVoterEmails)) {
        addCandidate(name, email)
      } else {
        binding.root.snackbar("Enter valid name and email address")
      }
    }

    return binding.root
  }

  private fun addCandidate(
    voterName: String,
    voterEmail: String
  ) {
    mVoterNames.add(voterName)
    mVoterEmails.add(voterEmail)
    voterRecyclerAdapter!!.notifyDataSetChanged()
    binding.textInputVoterName.editText?.setText("")
    binding.textInputVoterEmail.editText?.setText("")
  }

  override fun onStarted() {
    binding.progressBar.show()
    binding.buttonInviteVoter.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    binding.progressBar.hide()
    binding.buttonInviteVoter.toggleIsEnable()
    val mMessage = StringBuilder()
    mMessage.append(message)
    binding.root.snackbar(mMessage.toString())
  }

  override fun onSuccess(message: String) {
    binding.progressBar.hide()
    binding.buttonInviteVoter.toggleIsEnable()
    lifecycleScope.launch {
      prefs.setUpdateNeeded(true)
    }
    binding.root.snackbar(message)
    Navigation.findNavController(binding.root)
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
