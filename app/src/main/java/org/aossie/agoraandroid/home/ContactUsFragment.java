package org.aossie.agoraandroid.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.aossie.agoraandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("SpellCheckingInspection")
public class ContactUsFragment extends Fragment {

  public ContactUsFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_contact_us, null);
    Button gitlabBtn = view.findViewById(R.id.button_gitlab);
    Button gitterBtn = view.findViewById(R.id.button_gitter);
    gitterBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitter.im/aossie/home")));
      }
    });
    gitlabBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/aossie")));
      }
    });

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
