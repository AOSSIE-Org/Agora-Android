package org.aossie.agoraandroid.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import org.aossie.agoraandroid.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareWithOthersFragment extends Fragment {

  public ShareWithOthersFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_share_with_others, container, false);
    Button shareBtn = view.findViewById(R.id.button_share_now);
    shareBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        //Get the app link in the Play Store
        final String appPackageName = getApplicationContext().getPackageName();
        String strAppLink;
        try {
          strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        } catch (android.content.ActivityNotFoundException activityNotFound) {
          strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }

        // This is the sharing part
        shareIntent.setType("text/link");
        String shareBody =
            "Hey! Download Agora Vote application for Free and create Elections right now" +
                "\n" + "" + strAppLink;
        String shareSub = "APP NAME/TITLE";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share Agora Vote Using"));
      }
    });
    return view;
  }
}
