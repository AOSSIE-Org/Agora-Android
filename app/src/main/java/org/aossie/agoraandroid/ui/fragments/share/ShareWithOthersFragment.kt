package org.aossie.agoraandroid.ui.fragments.share

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.FacebookSdk
import kotlinx.android.synthetic.main.fragment_share_with_others.view.btn_share
import org.aossie.agoraandroid.R.layout

/**
 * A simple [Fragment] subclass.
 */
class ShareWithOthersFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view =
      inflater.inflate(layout.fragment_share_with_others, container, false)
    view.btn_share.setOnClickListener {
      val shareIntent = Intent(Intent.ACTION_SEND)

      // Get the app link in the Play Store
      val appPackageName = FacebookSdk.getApplicationContext()
        .packageName
      val strAppLink: String
      strAppLink = try {
        "https://play.google.com/store/apps/details?id=$appPackageName"
      } catch (activityNotFound: ActivityNotFoundException) {
        "https://play.google.com/store/apps/details?id=$appPackageName"
      }

      // This is the sharing part
      shareIntent.type = "text/link"
      val shareBody =
        """
        Hey! Download Agora Vote application for Free and create Elections right now
        $strAppLink
        """.trimIndent()
      val shareSub = "APP NAME/TITLE"
      shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
      shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
      startActivity(Intent.createChooser(shareIntent, "Share Agora Vote Using"))
    }
    return view
  }
}
