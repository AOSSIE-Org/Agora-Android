package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.adapters.ResultViewpagerAdapter
import org.aossie.agoraandroid.databinding.FragmentResultBinding
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import javax.inject.Inject

const val STORAGE_PERMISSION_REQUEST_CODE = 2

/**
 * A simple [Fragment] subclass.
 */
class ResultFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null
  private lateinit var winnerDto: WinnerDtoModel
  private val chartType: Array<Int> = arrayOf(
    string.bar_chart,
    string.pie_chart
  )
  private lateinit var binding: FragmentResultBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentResultBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {

    binding.tvNoResult.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getResult(id)
    initListeners()
    observeResult()
  }

  private fun observeResult() {

    electionDetailsViewModel.getResultResponseLiveData.observe(
      viewLifecycleOwner,
      { responseUI ->
        when (responseUI.status) {
          ResponseUI.Status.LOADING -> {
            binding.resultView.visibility = View.GONE
            binding.progressBar.show()
          }
          ResponseUI.Status.SUCCESS -> {
            notify(responseUI.message ?: "")
            binding.progressBar.hide()
            responseUI.data?.let {
              winnerDto = it
              initResultView(it)
            } ?: run {
              binding.resultView.visibility = View.GONE
              binding.tvNoResult.text = resources.getString(string.no_result)
              binding.tvNoResult.show()
            }
          }
          ResponseUI.Status.ERROR -> {
            notify(responseUI.message)
            binding.progressBar.hide()
            binding.tvNoResult.text = resources.getString(R.string.fetch_result_failed)
            binding.tvNoResult.show()
          }
        }
      }
    )

    electionDetailsViewModel.getShareResponseLiveData.observe(
      viewLifecycleOwner,
      { responseUI ->
        when (responseUI.status) {
          ResponseUI.Status.LOADING -> {
            // Do Nothing
          }
          ResponseUI.Status.SUCCESS -> {
            responseUI.data?.let { uri ->
              val shareIntent = Intent()
              shareIntent.let {
                it.action = Intent.ACTION_SEND
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                it.setDataAndType(uri, requireContext().contentResolver.getType(uri))
                it.putExtra(Intent.EXTRA_STREAM, uri)
              }
              startActivity(Intent.createChooser(shareIntent, getString(string.share_result)))
            }
          }
          ResponseUI.Status.ERROR -> {
            notify(responseUI.message)
          }
        }
      }
    )
  }

  private fun initListeners() {
    binding.shareResult.setOnClickListener {
      createImageAndShare()
    }
    binding.exportResult.setOnClickListener {
      if (ActivityCompat.checkSelfPermission(
          requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        createExcelFileAndShare()
      } else {
        askReadStoragePermission()
      }
    }
  }

  private fun askReadStoragePermission() {
    requestPermissions(
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE
    )
  }

  private fun createImageAndShare() {
    binding.resultView.drawToBitmap()
      .let {
        electionDetailsViewModel.createImage(requireContext(), it)
      }
  }

  private fun createExcelFileAndShare() {
    if (!this::winnerDto.isInitialized) {
      notify(getString(string.something_went_wrong_please_try_again_later))
      return
    }
    id?.let {
      electionDetailsViewModel.createExcelFile(requireContext(), winnerDto, it)
    }
  }

  private fun initResultView(winner: WinnerDtoModel) {
    binding.tvNoResult.hide()
    binding.resultView.visibility = View.VISIBLE
    binding.textViewWinnerName.text = winner.candidate?.name
    binding.viewPager.adapter = ResultViewpagerAdapter(this, chartType, winnerDto)
    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
      tab.text = getString(chartType[position])
    }.attach()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        createExcelFileAndShare()
      } else {
        notify(getString(string.permission_denied))
      }
    }
  }
}
