package org.aossie.agoraandroid.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R

/**
 * A placeholder fragment containing a simple view.
 */
class CreateElectionFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.demo_create_election, container, false)
  }
}
