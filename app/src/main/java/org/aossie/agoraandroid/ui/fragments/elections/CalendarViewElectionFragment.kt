package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.aossie.agoraandroid.R

class CalendarViewElectionFragment : Fragment() {

  lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_calendar_view_election, container, false)



    return rootView
  }
}