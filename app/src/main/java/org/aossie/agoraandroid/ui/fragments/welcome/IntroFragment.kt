package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.databinding.FragmentIntroBinding

private const val ARG_PARAM1 = "textResource"
private const val ARG_PARAM2 = "drawableResource"

class IntroFragment : Fragment() {

  private var textResource: Int? = null
  private var drawableResource: Int? = null
  private lateinit var binding: FragmentIntroBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      textResource = it.getInt(ARG_PARAM1)
      drawableResource = it.getInt(ARG_PARAM2)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentIntroBinding.inflate(layoutInflater)
    binding.imageView.setImageDrawable(
      drawableResource?.let { ContextCompat.getDrawable(requireContext(), it) }
    )
    binding.textView.text = textResource?.let { getString(it) }
    return binding.root
  }

  companion object {
    fun newInstance(
      param1: Int,
      param2: Int
    ) = IntroFragment().apply {
      arguments = Bundle().apply {
        putInt(ARG_PARAM1, param1)
        putInt(ARG_PARAM2, param2)
      }
    }
  }
}
