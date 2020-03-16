package org.aossie.agoraandroid.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.aossie.agoraandroid.createelection.NewElectionActivity
import org.aossie.agoraandroid.createelection.NewElectionFourFragment
import org.aossie.agoraandroid.createelection.NewElectionOneFragment
import org.aossie.agoraandroid.createelection.NewElectionThreeFragment
import org.aossie.agoraandroid.createelection.NewElectionTwoFragment

class NewElectionAdapter(fm: FragmentManager, private val activity: NewElectionActivity, private val size: Int): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  override fun getItem(position: Int): Fragment {
    return when(position) {
      0 -> NewElectionOneFragment(activity)
      1 -> NewElectionTwoFragment(activity)
      2 -> NewElectionThreeFragment(activity)
      else -> NewElectionFourFragment(activity)
    }
  }

  override fun getCount(): Int {
    return size
  }

}
