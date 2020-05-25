package org.aossie.agoraandroid.ui.fragments.moreOptions.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R.layout

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(layout.fragment_about, container, false)
  }
}