package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_ballot.view.progress_bar
import kotlinx.android.synthetic.main.fragment_result.view.pie_chart
import kotlinx.android.synthetic.main.fragment_result.view.result_view
import kotlinx.android.synthetic.main.fragment_result.view.text_view_winner_name
import kotlinx.android.synthetic.main.fragment_result.view.tv_no_result
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.model.Winner
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ResultFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(),
    DisplayElectionListener {

  private lateinit var rootView: View

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_result, container, false)

    rootView.tv_no_result.hide()
    electionDetailsViewModel.displayElectionListener = this

    id = VotersFragmentArgs.fromBundle(
        requireArguments()
    ).id
    electionDetailsViewModel.getResult(id)

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {
      electionDetailsViewModel.resultResponse.observe(
          requireActivity(), Observer {
        if (it != null) {
          initResultView(it)
        } else {
          rootView.result_view.visibility = View.GONE
          rootView.tv_no_result.text = resources.getString(R.string.no_result)
          rootView.tv_no_result.show()
        }
      })
      electionDetailsViewModel.notConnected.observe(
          requireActivity(), Observer {
        if (it) {
          rootView.result_view.visibility = View.GONE
          rootView.tv_no_result.text = resources.getString(R.string.fetch_result_failed)
          rootView.tv_no_result.show()
        }
      })
    }
  }

  private fun initResultView(winner: Winner) {
    rootView.tv_no_result.hide()
    rootView.result_view.visibility = View.VISIBLE
    val pieChart = rootView.pie_chart
    pieChart.setUsePercentValues(true)

    val description = Description()
    description.text = getString(R.string.election_result)
    description.textColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
    pieChart.description = description

    val value: ArrayList<PieEntry> = ArrayList()
    value.add(PieEntry(winner.score?.numerator?.toFloat()!!, winner.candidate?.name))
    value.add(PieEntry(winner.score?.denominator?.toFloat()!!, resources.getString(R.string.others)))

    val pieDataSet = PieDataSet(value, "")
    val pieData = PieData(pieDataSet)
    pieChart.data = pieData
    val legend = pieChart.legend
    legend.textColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
    pieDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
    pieChart.animateXY(1400, 1400)
    rootView.text_view_winner_name.text = winner.candidate?.name
  }

  override fun onDeleteElectionSuccess() {
    //do nothing
  }

  override fun onSuccess(message: String?) {
    if (message != null) rootView.snackbar(message)
    rootView.progress_bar.hide()
  }

  override fun onStarted() {
    rootView.result_view.visibility = View.GONE
    rootView.progress_bar.show()
  }

  override fun onFailure(message: String) {
    rootView.snackbar(message)
    rootView.progress_bar.hide()
    rootView.tv_no_result.text = resources.getString(R.string.fetch_result_failed)
    rootView.tv_no_result.show()
  }
}
