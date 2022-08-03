package org.aossie.agoraandroid.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.aossie.agoraandroid.ui.fragments.welcome.IntroFragment

class IntroViewpagerAdapter(
  f: Fragment,
  private val data: Array<Pair<Int, Int>>
) : FragmentStateAdapter(f) {
  override fun createFragment(pos: Int): Fragment {
    return IntroFragment.newInstance(data[pos].first, data[pos].second)
  }

  override fun getItemCount(): Int {
    return data.size
  }
}
