package org.aossie.agoraandroid.ui.fragments.createelection

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.fragment_create_election.view.add_candidate_btn
import kotlinx.android.synthetic.main.fragment_create_election.view.btn_end_date
import kotlinx.android.synthetic.main.fragment_create_election.view.btn_start_date
import kotlinx.android.synthetic.main.fragment_create_election.view.candidate_til
import kotlinx.android.synthetic.main.fragment_create_election.view.checkbox_invite
import kotlinx.android.synthetic.main.fragment_create_election.view.checkbox_real_time
import kotlinx.android.synthetic.main.fragment_create_election.view.checkbox_voter_visibility
import kotlinx.android.synthetic.main.fragment_create_election.view.election_description_til
import kotlinx.android.synthetic.main.fragment_create_election.view.election_name_til
import kotlinx.android.synthetic.main.fragment_create_election.view.end_date_til
import kotlinx.android.synthetic.main.fragment_create_election.view.et_candidate_name
import kotlinx.android.synthetic.main.fragment_create_election.view.et_election_description
import kotlinx.android.synthetic.main.fragment_create_election.view.et_election_name
import kotlinx.android.synthetic.main.fragment_create_election.view.et_end_date
import kotlinx.android.synthetic.main.fragment_create_election.view.et_start_date
import kotlinx.android.synthetic.main.fragment_create_election.view.names_rv
import kotlinx.android.synthetic.main.fragment_create_election.view.progress_bar
import kotlinx.android.synthetic.main.fragment_create_election.view.spinner_algorithm
import kotlinx.android.synthetic.main.fragment_create_election.view.spinner_ballot_visibility
import kotlinx.android.synthetic.main.fragment_create_election.view.start_date_til
import kotlinx.android.synthetic.main.fragment_create_election.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.errorDialog
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
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
): Fragment(), CreateElectionListener {

  private var sDay = 0
  private  var sMonth:Int = 0
  private  var sYear:Int = 0
  private var eDay = 0
  private  var eMonth:Int = 0
  private  var eYear:Int = 0
  private var mElectionName: String? = null
  private var mElectionDescription: String? = null
  private var mStartDate: String? = null
  private var mEndDate: String? = null
  private var calendar2: Calendar? = null
  private var calendar3: Calendar? = null

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



  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_create_election, container, false)

    createElectionViewModel.createElectionListener = this

    rootView.btn_start_date.setOnClickListener { handleStartDateTime() }

    rootView.btn_end_date.setOnClickListener { handleEndDateTime() }

    rootView.et_election_name.addTextChangedListener(textWatcher)

    rootView.et_election_description.addTextChangedListener(textWatcher)

    rootView.et_start_date.addTextChangedListener(textWatcher)

    rootView.et_end_date.addTextChangedListener(textWatcher)

    rootView.submit_details_btn.setOnClickListener {
      mElectionName = rootView.election_name_til.editText?.text.toString()
      mElectionDescription = rootView.election_description_til.editText?.text.toString()
      mStartDate = rootView.start_date_til.editText?.text.toString()
      mEndDate = rootView.end_date_til.editText?.text.toString()
      if(validateInputs()){
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        rootView.end_date_til.error = null
        electionDetailsSharedPrefs.saveElectionName(mElectionName)
        electionDetailsSharedPrefs.saveElectionDesc(mElectionDescription)
        if (mCandidates.isNotEmpty()) {
          HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
          electionDetailsSharedPrefs.saveCandidates(mCandidates)
          mFinalIsInvite = rootView.checkbox_invite.isChecked
          mFinalIsRealTime = rootView.checkbox_real_time.isChecked
          voterListVisibility = rootView.checkbox_voter_visibility.isChecked
          electionDetailsSharedPrefs.saveIsInvite(mFinalIsInvite)
          electionDetailsSharedPrefs.saveIsRealTime(mFinalIsRealTime)
          electionDetailsSharedPrefs.saveVoterListVisibility(voterListVisibility)
          electionDetailsSharedPrefs.saveVotingAlgo(votingAlgorithm)
          electionDetailsSharedPrefs.saveBallotVisibility(ballotVisibility)
          createElectionViewModel.createElection()
        } else {
          rootView.snackbar("Please Add At least One Candidate")
        }
      }
    }

    candidateRecyclerAdapter = CandidateRecyclerAdapter(mCandidates)
    rootView.names_rv.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rootView.names_rv)
    rootView.names_rv.adapter = candidateRecyclerAdapter

    rootView.et_candidate_name.addTextChangedListener(candidateTextWatcher)

    rootView.add_candidate_btn.setOnClickListener {
      val name = rootView.candidate_til.editText?.text.toString().trim { it <= ' ' }
      addCandidate(name)
    }

    algorithmsAdapter = ArrayAdapter.createFromResource(
        requireContext(), array.algorithms,
        android.R.layout.simple_spinner_item
    )
    algorithmsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    rootView.spinner_algorithm.adapter = algorithmsAdapter

    rootView.spinner_algorithm.onItemSelectedListener = object : OnItemSelectedListener {
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

    ballotVisibilityAdapter = ArrayAdapter.createFromResource(
        requireContext(), array.ballot_visibility,
        android.R.layout.simple_spinner_item
    )
    ballotVisibilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    rootView.spinner_ballot_visibility.adapter = ballotVisibilityAdapter

    rootView.spinner_ballot_visibility.onItemSelectedListener = object : OnItemSelectedListener {
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

    return rootView
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
      }
    }

  private fun addCandidate(cName: String) {
    mCandidates.add(cName)
    candidateRecyclerAdapter!!.notifyDataSetChanged()
    rootView.candidate_til.editText?.setText("")
  }

  private val candidateTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val candidateNameInput: String = rootView.et_candidate_name.text
          .toString()
          .trim()
      rootView.add_candidate_btn.isEnabled = candidateNameInput.isNotEmpty()
    }
  }
  private fun validateInputs(): Boolean{
    if (mElectionName!!.isEmpty()) {
      rootView.election_name_til.error = "Please enter Election Name"
    } else {
      rootView.election_name_til.error = null
    }
    if (mElectionDescription!!.isEmpty()) {
      rootView.election_description_til.error = "Please enter description"
    } else {
      rootView.election_description_til.error = null
    }
    if (mStartDate!!.isEmpty()) {
      rootView.start_date_til.error = "Please enter start date"
    } else {
      rootView.start_date_til.error = null
    }
    if (mEndDate!!.isEmpty()) {
      rootView.end_date_til.error = "Please enter end date"
    }else{
      rootView.end_date_til.error = null
    }
    val isValid : Boolean = if(calendar3!!.before(calendar2)){
      rootView.errorDialog("End date should be after starting date and time i.e. $mStartDate")
      false
    }else{
      true
    }
    return isValid
  }

  private fun handleStartDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR]
    val MINUTE = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        mStartDate = "$year/$month/$dayOfMonth"
        sDay = dayOfMonth
        sMonth = month
        sYear = year
      }, YEAR, MONTH, DATE).apply { datePicker.minDate = System.currentTimeMillis() - 1000 }
    val timePickerDialog = TimePickerDialog(context,
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
          mStartDate = if (hourOfDay < 10 && minute < 10) "$mStartDate at 0$hourOfDay:0$minute"
          else if (hourOfDay < 10) "$mStartDate at 0$hourOfDay:$minute"
          else if (minute < 10) "$mStartDate at $hourOfDay:0$minute"
          else "$mStartDate at $hourOfDay:$minute"
          //Formatting the starting date in Date-Time format
          calendar2 = Calendar.getInstance()
          calendar2?.set(Calendar.HOUR_OF_DAY, hourOfDay)
          calendar2?.set(Calendar.MINUTE, minute)
          calendar2?.set(Calendar.YEAR, sYear)
          calendar2?.set(Calendar.MONTH, sMonth)
          calendar2?.set(Calendar.DAY_OF_MONTH, sDay)
          if (calendar2 != null && calendar2!!.before(Calendar.getInstance())) {
            rootView.errorDialog("Start date should be after current date and time")
          } else {
            rootView.start_date_til.editText?.setText(mStartDate)
            val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
            electionDetailsSharedPrefs.saveStartTime(charSequence.toString())
          }
        }, HOUR, MINUTE, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

  private fun handleEndDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR]
    val MINUTE = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        mEndDate = "$year/$month/$dayOfMonth"
        eYear = year
        eMonth = month
        eDay = dayOfMonth
      }, YEAR, MONTH, DATE).apply { datePicker.minDate = System.currentTimeMillis() - 1000 }
    val timePickerDialog = TimePickerDialog(requireContext(),
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
          //Formatting the ending date in Date-Time format
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
            rootView.errorDialog("End date should be after current date and time")
          } else {
            rootView.end_date_til.editText?.setText(mEndDate)
            val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
            electionDetailsSharedPrefs.saveEndTime(charSequence2.toString())
          }
        }, HOUR, MINUTE, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

  private val textWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val electionNameInput: String = rootView.et_election_name.text
          .toString()
          .trim()
      val electionDescriptionInput: String = rootView.et_election_description.text
          .toString()
          .trim()
      val startDateInput: String = rootView.et_start_date.text
          .toString()
          .trim()
      val endDateInput: String = rootView.et_end_date.text
          .toString()
          .trim()
      rootView.submit_details_btn.isEnabled = electionNameInput.isNotEmpty()
          && electionDescriptionInput.isNotEmpty()
          && startDateInput.isNotEmpty()
          && endDateInput.isNotEmpty()
    }
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    if(message!=null) rootView.snackbar(message)
    prefs.setUpdateNeeded(true)
    electionDetailsSharedPrefs.clearElectionData()
    Navigation.findNavController(rootView)
        .navigate(CreateElectionFragmentDirections.actionCreateElectionFragmentToHomeFragment())
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
  }

}
