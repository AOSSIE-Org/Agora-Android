package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.aossie.agoraandroid.R

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_ballot, container, false)
  }

}
