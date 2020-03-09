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
public class ReportBugFragment extends Fragment {
  public static int opt;

  public ReportBugFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_report_bug, null);
    Button openConsole = view.findViewById(R.id.button_report_bug);
    openConsole.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       opt=3;
        startActivity(new Intent(getActivity(), webview.class));
      }
    });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
