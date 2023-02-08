package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import org.aossie.agoraandroid.databinding.FragmentIntroBinding

private const val TEXT_RESOURCE = "textResource"
private const val DRAWABLE_RESOURCE = "drawableResource"

class IntroFragment : Fragment() {

  private var textResource: Int? = null
  private var drawableResource: Int? = null
  private lateinit var binding: FragmentIntroBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      textResource = it.getInt(TEXT_RESOURCE)
      drawableResource = it.getInt(DRAWABLE_RESOURCE)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentIntroBinding.inflate(layoutInflater)
    drawableResource?.let {
      Picasso.get()
        .load(it)
        .placeholder(it)
        .into(binding.imageView)
    }
    binding.textView.text = textResource?.let { getString(it) }
    return binding.root
  }

  companion object {
    fun newInstance(
      textResource: Int,
      drawableResource: Int
    ) = IntroFragment().apply {
      arguments = Bundle().apply {
        putInt(TEXT_RESOURCE, textResource)
        putInt(DRAWABLE_RESOURCE, drawableResource)
      }
    }
  }
}
