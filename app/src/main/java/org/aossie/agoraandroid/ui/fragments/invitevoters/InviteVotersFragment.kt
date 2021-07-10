package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.databinding.FragmentInviteVotersBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.fragments.createelection.STORAGE_INTENT_REQUEST_CODE
import org.aossie.agoraandroid.ui.fragments.createelection.STORAGE_PERMISSION_REQUEST_CODE
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.FileUtils
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import org.json.JSONException
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class InviteVotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : Fragment(), SessionExpiredListener {

  private lateinit var binding: FragmentInviteVotersBinding

  var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

  private val mVoters = ArrayList<VotersDto>()
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
        val deletedVoter = mVoters[viewHolder.absoluteAdapterPosition]
        Snackbar.make(binding.root, string.voter_removed, Snackbar.LENGTH_LONG)
          .setAction(AppConstants.undo) {
            addVoter(deletedVoter)
          }
          .show()
        mVoters.removeAt(viewHolder.absoluteAdapterPosition)
        voterRecyclerAdapter?.notifyDataSetChanged()
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_voters, container, false)
    inviteVotersViewModel.sessionExpiredListener = this

    initView()
    initListeners()
    initObserver()

    return binding.root
  }

  private fun initObserver() {
    inviteVotersViewModel.getSendVoterLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> onStarted()
          ResponseUI.Status.SUCCESS -> onSuccess(it.message ?: "")
          ResponseUI.Status.ERROR -> onFailure(it.message)
        }
      }
    )
    inviteVotersViewModel.getImportVotersLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> {
            // Do Nothing
          }
          ResponseUI.Status.SUCCESS -> onReadSuccess(it.dataList)
          ResponseUI.Status.ERROR -> onReadFailure(it.message)
        }
      }
    )
  }

  private fun initView() {
    voterRecyclerAdapter = VoterRecyclerAdapter(mVoters)
    binding.recyclerViewVoters.apply {
      layoutManager = LinearLayoutManager(context)
      ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
      adapter = voterRecyclerAdapter
    }
  }

  private fun initListeners() {
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
        inviteVotersViewModel.inviteVoters(mVoters, id)
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
      if (inviteValidator(email, name)) {
        addVoter(VotersDto(name, email))
      } else {
        binding.root.snackbar(getString(string.enter_valid_details))
      }
    }

    binding.importVoter.setOnClickListener {
      if (ActivityCompat.checkSelfPermission(
          requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        searchExcelFile()
      } else {
        askReadStoragePermission()
      }
    }
  }

  private fun searchExcelFile() {
    val excelIntent = Intent()
    excelIntent.let {
      it.action = Intent.ACTION_GET_CONTENT
      it.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }
    startActivityForResult(excelIntent, STORAGE_INTENT_REQUEST_CODE)
  }

  private fun askReadStoragePermission() {
    requestPermissions(
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE
    )
  }

  private fun addVoter(
    voter: VotersDto,
  ) {
    mVoters.add(voter)
    voterRecyclerAdapter?.notifyDataSetChanged()
    binding.textInputVoterName.editText?.setText("")
    binding.textInputVoterEmail.editText?.setText("")
  }

  private fun importVoters(
    voters: List<VotersDto>,
  ) {
    mVoters.addAll(voters)
    voterRecyclerAdapter!!.notifyDataSetChanged()
  }

  fun onStarted() {
    binding.progressBar.show()
    binding.buttonInviteVoter.toggleIsEnable()
  }

  fun onFailure(message: String?) {
    binding.progressBar.hide()
    binding.buttonInviteVoter.toggleIsEnable()
    val mMessage = StringBuilder()
    mMessage.append(message)
    binding.root.snackbar(mMessage.toString())
  }

  fun onSuccess(message: String) {
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
  ): Boolean {
    if (email.isEmpty()) {
      binding.textInputVoterEmail.error = getString(string.enter_voter_email)
      return false
    } else if (!email.matches(emailPattern.toRegex())) {
      binding.textInputVoterEmail.error = getString(string.enter_valid_voter_email)
      return false
    } else if (inviteVotersViewModel.getEmailList(mVoters).contains(email)) {
      binding.textInputVoterEmail.error = getString(string.voter_same_email)
      return false
    }
    binding.textInputVoterEmail.error = null
    return true
  }

  private fun nameValidator(name: String): Boolean {
    if (name.isEmpty()) {
      binding.textInputVoterName.error = getString(string.enter_voter_name)
      return false
    }
    binding.textInputVoterName.error = null
    return true
  }

  private fun inviteValidator(
    email: String,
    name: String,
  ): Boolean {
    val isNameValid = nameValidator(name)
    val isEmailValid = emailValidator(email)
    return isNameValid && isEmailValid
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    intentData: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, intentData)
    if (resultCode != Activity.RESULT_OK) return

    if (requestCode == STORAGE_INTENT_REQUEST_CODE) {
      val fileUri = intentData?.data ?: return
      FileUtils.getPathFromUri(requireContext(), fileUri)
        ?.let { inviteVotersViewModel.readExcelData(requireContext(), it, mVoters) }
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        searchExcelFile()
      } else {
        binding.root.snackbar(getString(string.permission_denied))
      }
    }
  }

  private fun onReadSuccess(list: List<VotersDto>?) {
    list?.let {
      if (list.isNotEmpty()) importVoters(list)
    }
  }

  fun onReadFailure(message: String?) {
    binding.root.snackbar(message)
  }
}
