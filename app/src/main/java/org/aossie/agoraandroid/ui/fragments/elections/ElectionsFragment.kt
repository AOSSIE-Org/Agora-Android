package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.aossie.agoraandroid.R

/**
 * A simple [Fragment] subclass.
 */
class ElectionsFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_elections, container, false)
  }

}
