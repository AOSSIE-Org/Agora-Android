package org.aossie.agoraandroid.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.aossie.agoraandroid.data.remote.dto.WinnerDto
import org.aossie.agoraandroid.ui.fragments.electionDetails.visualisation.ChartFragment

class ResultViewpagerAdapter(
  f: Fragment,
  private val chartType: Array<Int>,
  private val winnerDto: WinnerDto
) : FragmentStateAdapter(f) {
  override fun createFragment(pos: Int): Fragment {
    return ChartFragment.newInstance(chartType[pos], winnerDto)
  }

  override fun getItemCount(): Int {
    return chartType.size
  }
}
