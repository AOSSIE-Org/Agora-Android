package org.aossie.agoraandroid.data.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.ui.fragments.electionDetails.visualisation.ChartFragment

class ResultViewpagerAdapter(
  f: Fragment,
  private val chartType: Array<Int>,
  private val winnerDto: WinnerDtoModel
) : FragmentStateAdapter(f) {
  override fun createFragment(pos: Int): Fragment {
    return ChartFragment.newInstance(chartType[pos], winnerDto)
  }

  override fun getItemCount(): Int {
    return chartType.size
  }
}
