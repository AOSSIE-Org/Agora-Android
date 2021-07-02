package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.dto.WinnerDto
import org.aossie.agoraandroid.databinding.FragmentResultBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.ResponseUI
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
  SessionExpiredListener {
  lateinit var binding: FragmentResultBinding

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
    binding.tvNoResult.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getResult(id)

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {
      observeResult()
      observeConnectivity()
    }
  }

  private fun observeConnectivity() {
    electionDetailsViewModel.notConnected.observe(
      viewLifecycleOwner,
      {
        if (it) {
          binding.resultView.visibility = View.GONE
          binding.tvNoResult.text = resources.getString(string.fetch_result_failed)
          binding.tvNoResult.show()
        }
      }
    )
  }

  private fun observeResult() {

electionDetailsViewModel.getResultResponseLiveData.observe(viewLifecycleOwner, { responseUI ->
  when (responseUI.status) {
    ResponseUI.Status.LOADING -> {binding.resultView.visibility = View.GONE
      binding.progressBar.show()
    }
    ResponseUI.Status.SUCCESS -> {
      if (responseUI.message.isNullOrBlank()) binding.root.snackbar(responseUI.message?:"")
      binding.progressBar.hide()
      responseUI.data?.let {
        initResultView(it)
      }?: kotlin.run {
        binding.resultView.visibility = View.GONE
        binding.tvNoResult.text = resources.getString(string.no_result)
        binding.tvNoResult.show()
      }
    }
    ResponseUI.Status.ERROR -> {
      binding.root.snackbar(responseUI.message?:"")
      binding.progressBar.hide()
      binding.tvNoResult.text = resources.getString(R.string.fetch_result_failed)
      binding.tvNoResult.show()
    }
  }
})


}

private fun initResultView(winner: WinnerDto) {
binding.tvNoResult.hide()
binding.resultView.visibility = View.VISIBLE
val pieChart = binding.pieChart
pieChart.setUsePercentValues(true)

val description = Description()
description.text = getString(R.string.election_result)
description.textColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
pieChart.description = description

val value: ArrayList<PieEntry> = ArrayList()
value.add(PieEntry(winner.score?.numerator?.toFloat()!!, winner.candidate?.name))
value.add(
 PieEntry(winner.score.denominator?.toFloat()!!, resources.getString(R.string.others))
)

val pieDataSet = PieDataSet(value, "")
val pieData = PieData(pieDataSet)
pieChart.data = pieData
val legend = pieChart.legend
legend.textColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
pieDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
pieChart.animateXY(1400, 1400)
binding.textViewWinnerName.text = winner.candidate?.name
}





override fun onSessionExpired() {
hostViewModel.setLogout(true)
}
}
