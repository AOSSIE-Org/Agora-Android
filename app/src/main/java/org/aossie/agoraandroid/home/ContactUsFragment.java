package org.aossie.agoraandroid.home;

import android.app.Activity;
import android.content.Context;
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

import com.thefinestartist.finestwebview.FinestWebView;

import org.aossie.agoraandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("SpellCheckingInspection")
public class ContactUsFragment extends Fragment {
  private Activity mActivity;

  public ContactUsFragment() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    if (context instanceof Activity){
      mActivity=(Activity) context;
    }
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
        new FinestWebView.Builder(mActivity).show("https://gitter.im/aossie/home");
      }
    });
    gitlabBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new FinestWebView.Builder(mActivity).show("https://gitlab.com/aossie");
      }
    });

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
