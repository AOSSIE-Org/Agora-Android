package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.IntroViewpagerAdapter
import org.aossie.agoraandroid.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

  private var binding: FragmentWelcomeBinding? = null
  private val data: Array<Pair<Int, Int>> = arrayOf(
    Pair(R.string.first_welcome_message, R.drawable.boy_ship),
    Pair(R.string.second_welcome_message, R.drawable.election),
    Pair(R.string.third_welcome_message, R.drawable.vote),
    Pair(R.string.fourth_welcome_message, R.drawable.visualize)
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentWelcomeBinding.inflate(layoutInflater)
    binding?.let { _binding ->
      _binding.viewPager.adapter = IntroViewpagerAdapter(this, data)
      TabLayoutMediator(_binding.tabLayout, _binding.viewPager) { _, _ ->
      }.attach()

      _binding.btnLogin.setOnClickListener {
        Navigation.findNavController(_binding.root)
          .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
      }
      _binding.btnSignup.setOnClickListener {
        Navigation.findNavController(_binding.root)
          .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment())
      }
    }
    return binding?.root
  }
}
