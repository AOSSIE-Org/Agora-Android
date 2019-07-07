package org.aossie.agoraandroid.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.createElection.CreateElectionFour;
import org.aossie.agoraandroid.createElection.CreateElectionOne;
import org.aossie.agoraandroid.createElection.CreateElectionThree;
import org.aossie.agoraandroid.createElection.CreateElectionTwo;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home,null);
        Button createElection=view.findViewById(R.id.button_create_election);

        createElection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateElectionTwo.class));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
