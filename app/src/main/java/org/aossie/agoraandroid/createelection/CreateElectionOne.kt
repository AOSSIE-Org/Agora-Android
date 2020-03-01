package org.aossie.agoraandroid.createelection

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create_election_one.*
import okhttp3.internal.Internal.instance
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.ActivityCreateElectionOneBinding
import org.aossie.agoraandroid.utilities.toast
import java.util.*

class CreateElectionOne : AppCompatActivity(),ElectionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_election_one)

        val factory = CreateElectionOneViewModelFactory(this)
        val binding :ActivityCreateElectionOneBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_election_one)
        val viewModel =ViewModelProviders.of(this,factory).get(CreateElectionOneViewModel::class.java)
        binding.electiononeviewmodel = viewModel

        viewModel.electionListener =this
        viewModel.electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(application)

    }



    override fun onSuccess() {

        startActivity(Intent(this,CreateElectionTwo::class.java))
    }

    override fun onFailure(message: String) {

            toast(message)
    }

    override fun nameError(message: String) {

        election_name_til.error = message

    }

    override fun descError(message: String) {

        election_description_til.error = message

    }

    override fun startDateError(message: String) {

        start_date_til.error = message

    }

    override fun endDateError(message: String) {

        end_date_til.error   =message

    }
}