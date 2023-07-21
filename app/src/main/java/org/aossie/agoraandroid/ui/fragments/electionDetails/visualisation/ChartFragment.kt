package org.aossie.agoraandroid.ui.fragments.electionDetails.visualisation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.databinding.FragmentChartBinding
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.utilities.AppConstants.GRAPH_ANIMATION_DURATION
import java.util.ArrayList
import kotlin.math.roundToInt

private const val CHART_TYPE = "chartType"
private const val WINNER_DTO = "winnerDto"

class ChartFragment : Fragment() {

  private var binding: FragmentChartBinding? = null
  private var chartType: Int? = null
  private var winnerDto: WinnerDtoModel? = null
  private val colorList = ColorTemplate.MATERIAL_COLORS.toList().subList(0, 2)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      winnerDto = it.getParcelable(WINNER_DTO)
      chartType = it.getInt(CHART_TYPE)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentChartBinding.inflate(layoutInflater)
    winnerDto?.let {
      when (chartType) {
        string.bar_chart -> initBarChart(it)
        string.pie_chart -> initPieChart(it)
      }
    }
    return binding?.root
  }

  private fun initBarChart(winner: WinnerDtoModel) {
    binding?.let {
      val barChart = it.horizontalBarChart
      barChart.visibility = View.VISIBLE

      val description = Description()
      description.text = getString(string.election_result_bar_chart)
      description.textColor = ContextCompat.getColor(requireContext(), color.colorPrimary)
      barChart.description = description

      val value: ArrayList<BarEntry> = ArrayList()
      value.add(BarEntry(0f, winner.score?.denominator?.toFloat()!!))
      value.add(BarEntry(1f, winner.score?.numerator?.toFloat()!!))

      val barDataSet = BarDataSet(value, "")
      barDataSet.colors = colorList.asReversed()
      val textColorList = listOf(colorList[1], colorList[1], colorList[0], colorList[0])
      barDataSet.setValueTextColors(textColorList)
      barDataSet.valueTextSize = 14f
      val barData = BarData(barDataSet)
      barData.setValueFormatter(object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
          return value.roundToInt().toString()
        }
      })

      barChart.data = barData
      barChart.xAxis.isEnabled = false
      barChart.axisLeft.axisMinimum = 0f
      barChart.axisRight.axisMinimum = 0f
      barChart.axisLeft.isEnabled = false
      barChart.axisRight.isEnabled = false

      val entries: ArrayList<LegendEntry> = arrayListOf(
        LegendEntry(
          winner.candidate?.name.toString(),
          Legend.LegendForm.CIRCLE,
          Float.NaN,
          Float.NaN,
          null,
          colorList[0]
        ),
        LegendEntry(
          getString(string.others),
          Legend.LegendForm.CIRCLE,
          Float.NaN,
          Float.NaN,
          null,
          colorList[1]
        ),
      )
      val legend = barChart.legend
      legend.textColor = ContextCompat.getColor(requireContext(), color.colorPrimary)
      legend.setCustom(entries)

      barChart.setTouchEnabled(false)
      barChart.animateXY(GRAPH_ANIMATION_DURATION, GRAPH_ANIMATION_DURATION)
    }
  }

  private fun initPieChart(winner: WinnerDtoModel) {
    binding?.let {
      val pieChart = it.pieChart
      pieChart.visibility = View.VISIBLE
      pieChart.setUsePercentValues(true)

      val description = Description()
      description.text = getString(string.election_result_pie_chart)
      description.textColor = ContextCompat.getColor(requireContext(), color.colorPrimary)
      pieChart.description = description

      val value: ArrayList<PieEntry> = ArrayList()
      value.add(PieEntry(winner.score?.numerator?.toFloat()!!, winner.candidate?.name))
      value.add(PieEntry(winner.score.denominator?.toFloat()!!, getString(string.others)))

      val pieDataSet = PieDataSet(value, "")
      pieDataSet.colors = colorList
      pieDataSet.valueTextSize = 12f
      val pieData = PieData(pieDataSet)
      pieData.setValueFormatter(object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
          return ("$value%")
        }
      })
      pieChart.data = pieData
      pieChart.setEntryLabelColor(ContextCompat.getColor(requireContext(), color.black))

      val legend = pieChart.legend
      legend.textColor = ContextCompat.getColor(requireContext(), color.colorPrimary)

      pieChart.animateXY(GRAPH_ANIMATION_DURATION, GRAPH_ANIMATION_DURATION)
    }
  }

  companion object {
    fun newInstance(
      chartType: Int,
      winnerDto: WinnerDtoModel,
    ) = ChartFragment().apply {
      arguments = Bundle().apply {
        putInt(CHART_TYPE, chartType)
        putParcelable(WINNER_DTO, winnerDto)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
  }
}
