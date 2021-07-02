package org.aossie.agoraandroid.ui.fragments.createelection

import android.R.layout
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentCreateElectionBinding
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.errorDialog
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import java.util.ArrayList
import java.util.Calendar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CreateElectionFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs,
  private val prefs: PreferenceProvider
) : Fragment() {
  lateinit var binding: FragmentCreateElectionBinding
  private var sDay = 0
  private var sMonth: Int = 0
  private var sYear: Int = 0
  private var eDay = 0
  private var eMonth: Int = 0
  private var eYear: Int = 0
  private var mElectionName: String? = null
  private var mElectionDescription: String? = null
  private var mStartDate: String? = null
  private var mEndDate: String? = null
  private var calendar2: Calendar? = null
  private var calendar3: Calendar? = null
  private var datePickerDialog: DatePickerDialog? = null
  private var timePickerDialog: TimePickerDialog? = null

  private val mCandidates = ArrayList<String>()
  private var candidateRecyclerAdapter: CandidateRecyclerAdapter? = null

  lateinit var algorithmsAdapter: ArrayAdapter<CharSequence>
  private var votingAlgorithm: String? = null

  lateinit var ballotVisibilityAdapter: ArrayAdapter<CharSequence>
  private var ballotVisibility: String? = null

  private val createElectionViewModel: CreateElectionViewModel by viewModels {
    viewModelFactory
  }
  private var mFinalIsInvite: Boolean? = null
  private var mFinalIsRealTime: Boolean? = null
  private var voterListVisibility: Boolean? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_election, container, false)

    initView()

    initListeners()

    createElectionViewModel.getCreateElectionData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> {

            binding.progressBar.show()
            binding.submitDetailsBtn.toggleIsEnable()
          }
          ResponseUI.Status.SUCCESS -> {

            binding.progressBar.hide()
            binding.submitDetailsBtn.toggleIsEnable()
            if (!it.message.isNullOrBlank()) binding.root.snackbar(it.message ?: "")
            prefs.setUpdateNeeded(true)
            electionDetailsSharedPrefs.clearElectionData()
            Navigation.findNavController(binding.root)
              .navigate(CreateElectionFragmentDirections.actionCreateElectionFragmentToHomeFragment())
          }
          ResponseUI.Status.ERROR -> {

            binding.progressBar.hide()
            binding.root.snackbar(it.message ?: "")
            binding.submitDetailsBtn.toggleIsEnable()
          }
        }
      }
    )

    return binding.root
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

    binding.etElectionName.addTextChangedListener(textWatcher)

    binding.etElectionDescription.addTextChangedListener(textWatcher)

    binding.etStartDate.addTextChangedListener(textWatcher)

    binding.etEndDate.addTextChangedListener(textWatcher)

    binding.submitDetailsBtn.setOnClickListener {
      mElectionName = binding.electionNameTil.editText?.text.toString()
      mElectionDescription = binding.electionDescriptionTil.editText?.text.toString()
      mStartDate = binding.startDateTil.editText?.text.toString()
      mEndDate = binding.endDateTil.editText?.text.toString()
      if (validateInputs()) {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        binding.endDateTil.error = null
        electionDetailsSharedPrefs.saveElectionName(mElectionName)
        electionDetailsSharedPrefs.saveElectionDesc(mElectionDescription)
        if (mCandidates.isNotEmpty()) {
          HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
          electionDetailsSharedPrefs.saveCandidates(mCandidates)
          mFinalIsInvite = binding.checkboxInvite.isChecked
          mFinalIsRealTime = binding.checkboxRealTime.isChecked
          voterListVisibility = binding.checkboxVoterVisibility.isChecked
          electionDetailsSharedPrefs.saveIsInvite(mFinalIsInvite)
          electionDetailsSharedPrefs.saveIsRealTime(mFinalIsRealTime)
          electionDetailsSharedPrefs.saveVoterListVisibility(voterListVisibility)
          electionDetailsSharedPrefs.saveVotingAlgo(votingAlgorithm)
          electionDetailsSharedPrefs.saveBallotVisibility(ballotVisibility)
          createElectionViewModel.createElection()
        } else {
          binding.root.snackbar("Please Add At least One Candidate")
        }
      }
    }

    binding.etCandidateName.addTextChangedListener(candidateTextWatcher)

    binding.addCandidateBtn.setOnClickListener {
      val name = binding.candidateTil.editText?.text.toString()
        .trim { it <= ' ' }
      addCandidate(name)
    }

    binding.spinnerAlgorithm.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(
        adapterView: AdapterView<*>,
        view: View,
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
        view: View,
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
        mCandidates.removeAt(viewHolder.adapterPosition)
        candidateRecyclerAdapter!!.notifyDataSetChanged()
        if (mCandidates.isEmpty()) binding.textViewSwipe.hide()
      }
    }

  private fun addCandidate(cName: String) {
    mCandidates.add(cName)
    candidateRecyclerAdapter!!.notifyDataSetChanged()
    binding.candidateTil.editText?.setText("")
    binding.textViewSwipe.show()
  }

  private val candidateTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(
      s: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int
    ) {
      val candidateNameInput: String = binding.etCandidateName.text
        .toString()
        .trim()
      binding.addCandidateBtn.isEnabled = candidateNameInput.isNotEmpty()
    }
  }

  private fun validateInputs(): Boolean {
    return if (calendar2 == null || calendar3 == null) {
      binding.root.errorDialog("Please enter all the details")
      false
    } else {
      if (calendar3!!.before(calendar2)) {
        binding.root.errorDialog("End date should be after starting date and time i.e. $mStartDate")
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
        calendar2 = Calendar.getInstance()
        calendar2?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar2?.set(Calendar.MINUTE, minute)
        calendar2?.set(Calendar.YEAR, sYear)
        calendar2?.set(Calendar.MONTH, sMonth)
        calendar2?.set(Calendar.DAY_OF_MONTH, sDay)
        if (calendar2 != null && calendar2!!.before(Calendar.getInstance())) {
          binding.root.errorDialog("Start date should be after current date and time")
        } else {
          binding.startDateTil.editText?.setText(mStartDate)
          val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
          electionDetailsSharedPrefs.saveStartTime(charSequence.toString())
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
        calendar3 = Calendar.getInstance()
        calendar3?.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar3?.set(Calendar.MINUTE, minute)
        calendar3?.set(Calendar.YEAR, eYear)
        calendar3?.set(Calendar.MONTH, eMonth)
        calendar3?.set(Calendar.DAY_OF_MONTH, eDay)
        if (calendar3 != null && calendar3!!.before(Calendar.getInstance())) {
          binding.root.errorDialog("End date should be after current date and time")
        } else {
          binding.endDateTil.editText?.setText(mEndDate)
          val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
          electionDetailsSharedPrefs.saveEndTime(charSequence2.toString())
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

  private val textWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(
      s: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int
    ) {
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
  }
}
