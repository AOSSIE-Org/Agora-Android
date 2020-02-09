package org.aossie.agoraandroid.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import org.aossie.agoraandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

  public AboutFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_about, container, false);
  }
}
