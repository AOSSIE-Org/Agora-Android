package org.aossie.agoraandroid.ui.fragments.createelection

import android.Manifest
import android.R.layout
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.doOnLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.takusemba.spotlight.Spotlight
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentCreateElectionBinding
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.FileUtils
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.TargetData
import org.aossie.agoraandroid.utilities.errorDialog
import org.aossie.agoraandroid.utilities.getSpotlight
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.scrollToView
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.toggleIsEnable
import java.util.Calendar
import javax.inject.Inject

const val STORAGE_PERMISSION_REQUEST_CODE = 2
const val STORAGE_INTENT_REQUEST_CODE = 4

/**
 * A simple [Fragment] subclass.
 */
class CreateElectionFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private var sDay = 0
  private var sMonth: Int = 0
  private var sYear: Int = 0
  private var eDay = 0
  private var eMonth: Int = 0
  private var eYear: Int = 0
  private var mStartDate: String? = null
  private var mEndDate: String? = null
  private var startDateCalendar: Calendar? = null
  private var endDateCalendar: Calendar? = null
  private var datePickerDialog: DatePickerDialog? = null
  private var timePickerDialog: TimePickerDialog? = null

  private val mCandidates = ArrayList<String>()
  private var candidateRecyclerAdapter: CandidateRecyclerAdapter? = null

  lateinit var algorithmsAdapter: ArrayAdapter<CharSequence>
  private var votingAlgorithm: String? = null

  lateinit var ballotVisibilityAdapter: ArrayAdapter<CharSequence>
  private var ballotVisibility: String? = null

  private var spotlight: Spotlight? = null
  private var spotlightTargets: ArrayList<TargetData>? = null
  private var currentSpotlightIndex = 0

  private val createElectionViewModel: CreateElectionViewModel by viewModels {
    viewModelFactory
  }

  private lateinit var binding: FragmentCreateElectionBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCreateElectionBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {
    initView()
    initListeners()
    initObserver()

    binding.root.doOnLayout {
      checkIsFirstOpen()
    }
  }

  private fun initObserver() {
    lifecycleScope.launch {
      createElectionViewModel.getCreateElectionData.collect {
        if (it != null) {
          when (it.status) {
            ResponseUI.Status.LOADING -> {

              binding.progressBar.show()
              binding.submitDetailsBtn.toggleIsEnable()
            }
            ResponseUI.Status.SUCCESS -> {

              binding.progressBar.hide()
              binding.submitDetailsBtn.toggleIsEnable()
              notify(it.message)
              lifecycleScope.launch {
                prefs.setUpdateNeeded(true)
              }
              Navigation.findNavController(binding.root)
                .navigate(CreateElectionFragmentDirections.actionCreateElectionFragmentToHomeFragment())
            }
            ResponseUI.Status.ERROR -> {

              binding.progressBar.hide()
              notify(it.message)
              binding.submitDetailsBtn.toggleIsEnable()
            }
          }
        }
      }
    }

    lifecycleScope.launch {
      createElectionViewModel.getImportVotersLiveData.collect {
        if (it != null) {
          when (it.status) {
            ResponseUI.Status.LOADING -> { // Do Nothing
            }

            ResponseUI.Status.SUCCESS -> onReadSuccess(it.dataList)

            ResponseUI.Status.ERROR -> onReadFailure(it.message)
          }
        }
      }
    }
  }

  override fun onNetworkConnected() {
    initView()
  }

  private fun initView() {
    candidateRecyclerAdapter = CandidateRecyclerAdapter(mCandidates)
    binding.namesRv.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.namesRv)
    binding.namesRv.adapter = candidateRecyclerAdapter

    algorithmsAdapter = ArrayAdapter.createFromResource(
      requireContext(), array.algorithms,
      layout.simple_spinner_item
    )
    algorithmsAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)

    binding.spinnerAlgorithm.adapter = algorithmsAdapter

    ballotVisibilityAdapter = ArrayAdapter.createFromResource(
      requireContext(), array.ballot_visibility,
      layout.simple_spinner_item
    )
    ballotVisibilityAdapter.setDropDownViewResource(layout.simple_spinner_dropdown_item)

    binding.spinnerBallotVisibility.adapter = ballotVisibilityAdapter
  }

  private fun initListeners() {
    binding.startDateTil.setOnClickListener { handleStartDateTime() }

    binding.etStartDate.setOnClickListener { handleStartDateTime() }

    binding.endDateTil.setOnClickListener { handleEndDateTime() }

    binding.etEndDate.setOnClickListener { handleEndDateTime() }

    binding.etElectionName.doAfterTextChanged {
      afterTextChanged()
    }

    binding.etElectionDescription.doAfterTextChanged {
      afterTextChanged()
    }

    binding.etStartDate.doAfterTextChanged {
      afterTextChanged()
    }

    binding.etEndDate.doAfterTextChanged {
      afterTextChanged()
    }

    binding.submitDetailsBtn.setOnClickListener {
      HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
      if (validateInputs()) {
        if (mCandidates.isNotEmpty() and (Calendar.getInstance() < startDateCalendar)) {
          val mElectionName = binding.electionNameTil.editText?.text.toString()
          val mElectionDescription = binding.electionDescriptionTil.editText?.text.toString()
          val mFinalIsInvite = binding.checkboxInvite.isChecked
          val mFinalIsRealTime = binding.checkboxRealTime.isChecked
          val voterListVisibility = binding.checkboxVoterVisibility.isChecked
          val startTime = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", startDateCalendar)
          val endTime = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", endDateCalendar)
          createElectionViewModel.createElection(
            ElectionDtoModel(
              listOf(),
              ballotVisibility,
              mCandidates,
              mElectionDescription,
              "Election",
              endTime.toString(),
              mFinalIsInvite,
              mFinalIsRealTime,
              mElectionName,
              1,
              startTime.toString(),
              voterListVisibility,
              votingAlgorithm
            )
          )
        } else {
          if (mCandidates.isEmpty())
            notify(getString(string.add_one_candidate))
          else
            notify(getString(string.time_tracker))
        }
      }
    }

    binding.etCandidateName.doAfterTextChanged {
      val candidateNameInput = it.toString().trim()
      binding.addCandidateBtn.isEnabled = candidateNameInput.isNotEmpty()
    }

    binding.addCandidateBtn.setOnClickListener {
      val name = binding.candidateTil.editText?.text.toString().trim()
      addCandidate(name)
    }

    binding.spinnerAlgorithm.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(
        adapterView: AdapterView<*>,
        view: View?,
        i: Int,
        l: Long
      ) {
        votingAlgorithm = adapterView.getItemAtPosition(i)
          .toString()
      }

      override fun onNothingSelected(adapterView: AdapterView<*>?) {
        votingAlgorithm = resources.getStringArray(array.security_questions)[0]
      }
    }

    binding.spinnerBallotVisibility.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(
        adapterView: AdapterView<*>,
        view: View?,
        i: Int,
        l: Long
      ) {
        ballotVisibility = adapterView.getItemAtPosition(i)
          .toString()
      }

      override fun onNothingSelected(adapterView: AdapterView<*>?) {
        ballotVisibility = resources.getStringArray(array.security_questions)[0]
      }
    }

    binding.importCandidates.setOnClickListener {
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

  private fun afterTextChanged() {
    val electionNameInput: String = binding.etElectionName.text
      .toString()
      .trim()
    val electionDescriptionInput: String = binding.etElectionDescription.text
      .toString()
      .trim()
    val startDateInput: String = binding.etStartDate.text
      .toString()
      .trim()
    val endDateInput: String = binding.etEndDate.text
      .toString()
      .trim()
    binding.submitDetailsBtn.isEnabled = electionNameInput.isNotEmpty() &&
      electionDescriptionInput.isNotEmpty() &&
      startDateInput.isNotEmpty() &&
      endDateInput.isNotEmpty()
  }

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
        mCandidates.removeAt(viewHolder.absoluteAdapterPosition)
        candidateRecyclerAdapter?.notifyDataSetChanged()
        if (mCandidates.isEmpty()) binding.textViewSwipe.hide()
      }
    }

  private fun addCandidate(cName: String) {
    mCandidates.add(cName)
    candidateRecyclerAdapter?.notifyDataSetChanged()
    binding.candidateTil.editText?.setText("")
    binding.textViewSwipe.show()
  }

  private fun importCandidates(cNames: List<String>) {
    mCandidates.addAll(cNames)
    candidateRecyclerAdapter?.notifyDataSetChanged()
    binding.textViewSwipe.show()
  }

  private fun validateInputs(): Boolean {
    return if (startDateCalendar == null || endDateCalendar == null) {
      binding.root.errorDialog(getString(string.enter_details))
      false
    } else {
      if (endDateCalendar!!.before(startDateCalendar)) {
        binding.root.errorDialog(getString(string.end_date_after_starting_date) + "i.e. $mStartDate")
        false
      } else {
        true
      }
    }
  }

  private fun isDialogsVisible(): Boolean {
    return datePickerDialog?.isShowing == true && timePickerDialog?.isShowing == true
  }

  private fun handleStartDateTime() {
    if (isDialogsVisible()) return
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR_OF_DAY]
    val MINUTE = calendar[Calendar.MINUTE]
    datePickerDialog =
      DatePickerDialog(
        requireContext(),
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
          mStartDate = "$year/${month + 1}/$dayOfMonth"
          sDay = dayOfMonth
          sMonth = month
          sYear = year
        },
        YEAR, MONTH, DATE
      ).apply { datePicker.minDate = System.currentTimeMillis() - 1000 }
    val timePickerDialog = TimePickerDialog(
      context,
      TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        mStartDate = if (hourOfDay < 10 && minute < 10) "$mStartDate at 0$hourOfDay:0$minute"
        else if (hourOfDay < 10) "$mStartDate at 0$hourOfDay:$minute"
        else if (minute < 10) "$mStartDate at $hourOfDay:0$minute"
        else "$mStartDate at $hourOfDay:$minute"
        // Formatting the starting date in Date-Time format
        startDateCalendar = Calendar.getInstance()
        startDateCalendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startDateCalendar?.set(Calendar.MINUTE, minute)
        startDateCalendar?.set(Calendar.YEAR, sYear)
        startDateCalendar?.set(Calendar.MONTH, sMonth)
        startDateCalendar?.set(Calendar.DAY_OF_MONTH, sDay)
        if (startDateCalendar != null && startDateCalendar!!.before(Calendar.getInstance())) {
          binding.root.errorDialog(getString(string.start_date_after_current_date))
        } else {
          binding.startDateTil.editText?.setText(mStartDate)
        }
      },
      HOUR, MINUTE, true
    )
    timePickerDialog.show()
    datePickerDialog?.show()
    datePickerDialog?.setOnCancelListener {
      timePickerDialog.dismiss()
    }
  }

  private fun handleEndDateTime() {
    if (isDialogsVisible()) return
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR_OF_DAY]
    val MINUTE = calendar[Calendar.MINUTE]
    datePickerDialog =
      DatePickerDialog(
        requireContext(),
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
          mEndDate = "$year/${month + 1}/$dayOfMonth"
          eYear = year
          eMonth = month
          eDay = dayOfMonth
        },
        YEAR, MONTH, DATE
      ).apply { datePicker.minDate = System.currentTimeMillis() - 1000 }
    val timePickerDialog = TimePickerDialog(
      requireContext(),
      TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        // Formatting the ending date in Date-Time format
        mEndDate = if (hourOfDay < 10 && minute < 10) "$mEndDate at 0$hourOfDay:0$minute"
        else if (hourOfDay < 10) "$mEndDate at 0$hourOfDay:$minute"
        else if (minute < 10) "$mEndDate at $hourOfDay:0$minute"
        else "$mEndDate at $hourOfDay:$minute"
        endDateCalendar = Calendar.getInstance()
        endDateCalendar?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        endDateCalendar?.set(Calendar.MINUTE, minute)
        endDateCalendar?.set(Calendar.YEAR, eYear)
        endDateCalendar?.set(Calendar.MONTH, eMonth)
        endDateCalendar?.set(Calendar.DAY_OF_MONTH, eDay)
        if (endDateCalendar != null && endDateCalendar!!.before(Calendar.getInstance())) {
          binding.root.errorDialog(getString(string.end_date_after_starting_date))
        } else {
          binding.endDateTil.editText?.setText(mEndDate)
        }
      },
      HOUR, MINUTE, true
    )
    timePickerDialog.show()
    datePickerDialog?.show()
    datePickerDialog?.setOnCancelListener {
      timePickerDialog.dismiss()
    }
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
        ?.let { createElectionViewModel.readExcelData(requireContext(), it) }
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
        notify(getString(string.permission_denied))
      }
    }
  }

  private fun onReadSuccess(list: List<String>?) {
    list?.let { if (list.isNotEmpty()) importCandidates(list) }
  }

  private fun onReadFailure(message: String?) {
    notify(message)
  }

  private fun checkIsFirstOpen() {
    lifecycleScope.launch {
      if (!prefs.isDisplayed(binding.root.id.toString())
        .first()
      ) {
        spotlightTargets = getSpotlightTargets()
        prefs.setDisplayed(binding.root.id.toString())
        showSpotlight()
      }
    }
  }

  private fun showSpotlight() {
    spotlightTargets?.let {
      if (currentSpotlightIndex in it.indices) {
        scrollToView(binding.scrollView, it[currentSpotlightIndex].targetView)
        spotlight = requireActivity().getSpotlight(
          it[currentSpotlightIndex++],
          {
            destroySpotlight()
          },
          {
            it.clear()
            destroySpotlight()
          },
          {
            if (isAdded) {
              showSpotlight()
            }
          }
        )
        spotlight?.start()
      }
    }
  }

  private fun getSpotlightTargets(): ArrayList<TargetData> {
    val targetData = ArrayList<TargetData>()
    targetData.add(
      TargetData(
        binding.etElectionName, getString(string.Create_Election),
        getString(string.create_election_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.linearLayout, getString(string.election_method),
        getString(string.election_method_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.etElectionDescription, getString(string.election_description),
        getString(string.election_description_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.etCandidateName, getString(string.candidates),
        getString(string.election_candidates_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.importCandidates, getString(string.election_import_candidates),
        getString(string.election_import_spotlight),
        false
      )
    )
    targetData.add(
      TargetData(
        binding.linearLayout2, getString(string.election_visibility),
        getString(string.election_visibility_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.checkboxVoterVisibility, getString(string.election_voter_visibility),
        getString(string.election_voter_visibility_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.startDateTil, getString(string.start_date_format),
        getString(string.election_start_date_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.endDateTil, getString(string.end_date_format),
        getString(string.election_end_date_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.checkboxRealTime, getString(string.real_time),
        getString(string.election_real_time_spotlight)
      )
    )
    targetData.add(
      TargetData(
        binding.checkboxInvite, getString(string.invite),
        getString(string.election_invite_spotlight)
      )
    )
    return targetData
  }

  private fun destroySpotlight() {
    spotlight?.finish()
    spotlight = null
  }

  override fun onPause() {
    super.onPause()
    destroySpotlight()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    destroySpotlight()
  }
}
