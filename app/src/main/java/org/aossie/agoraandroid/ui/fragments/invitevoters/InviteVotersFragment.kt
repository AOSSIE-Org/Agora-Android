package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_invite_voters.view.button_add_voter
import kotlinx.android.synthetic.main.fragment_invite_voters.view.button_invite_voter
import kotlinx.android.synthetic.main.fragment_invite_voters.view.recycler_view_voters
import kotlinx.android.synthetic.main.fragment_invite_voters.view.text_input_voter_email
import kotlinx.android.synthetic.main.fragment_invite_voters.view.text_input_voter_name
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.TextWatcherAdapter
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInActivity
import org.json.JSONException
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class InviteVotersFragment : Fragment(), InviteVoterListener {

  private lateinit var rootView: View

  private val mVoterNames = ArrayList<String>()
  private val mVoterEmails = ArrayList<String>()
  private var inviteVotersViewModel: InviteVotersViewModel? = null
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
        mVoterNames.removeAt(viewHolder.adapterPosition)
        mVoterEmails.removeAt(viewHolder.adapterPosition)
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

    inviteVotersViewModel = InviteVotersViewModel(activity!!.application, context!!)
    inviteVotersViewModel?.inviteVoterListener = this

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
//        if (getIntent().hasExtra("id") && getIntent().hasExtra("token")) {
//          hideKeyboardInActivity(this@InviteVotersActivity)
//          val id: String = getIntent().getStringExtra("id")
//          val token: String = getIntent().getStringExtra("token")
        val inviteVotersFragmentArgs = InviteVotersFragmentArgs.fromBundle(arguments!!)
        val id : String = inviteVotersFragmentArgs.id
        val token: String = inviteVotersFragmentArgs.token
        inviteVotersViewModel?.inviteVoters(mVoterNames, mVoterEmails, id, token)

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
      if (inviteVotersViewModel!!.inviteValidator(email, name, mVoterEmails)) {
        addCandidate(name, email)
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

  override fun onSendInviteSuccess() {
    Navigation.findNavController(rootView)
        .navigate(InviteVotersFragmentDirections.actionInviteVotersFragmentToHomeFragment())
  }

}
