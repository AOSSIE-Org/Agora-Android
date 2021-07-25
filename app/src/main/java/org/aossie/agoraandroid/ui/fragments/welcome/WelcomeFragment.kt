package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

  private lateinit var binding: FragmentWelcomeBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentWelcomeBinding.inflate(layoutInflater)

    binding.viewPager.adapter = MyPagerAdapter(this)
    TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
    }.attach()
    binding.btnLogin.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
    }

    binding.btnSignup.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment())
    }
    return binding.root
  }

  private class MyPagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
    override fun createFragment(pos: Int): Fragment {
      return when (pos) {
        0 -> IntroFragment.newInstance(R.string.first_welcome_message, R.drawable.boy_ship)
        1 -> IntroFragment.newInstance(R.string.second_welcome_message, R.drawable.election)
        2 -> IntroFragment.newInstance(R.string.third_welcome_message, R.drawable.vote)
        else -> IntroFragment.newInstance(R.string.fourth_welcome_message, R.drawable.visualize)
      }
    }

    override fun getItemCount(): Int {
      return 4
    }
  }
}
