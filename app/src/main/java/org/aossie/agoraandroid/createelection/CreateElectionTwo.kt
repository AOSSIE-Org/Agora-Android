package org.aossie.agoraandroid.createelection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.createelection.CreateElectionTwo
import org.aossie.agoraandroid.utilities.TinyDB
import java.util.*

class CreateElectionTwo : AppCompatActivity() {
    private val mCandidates = ArrayList<String>()
    private var tinydb: TinyDB? = null
    private var candidateRecyclerAdapter: CandidateRecyclerAdapter? = null
    private val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            mCandidates.removeAt(viewHolder.adapterPosition)
            candidateRecyclerAdapter!!.notifyDataSetChanged()
        }
    }
    private var mAddCandidateTextInput: TextInputLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_election_two)
        val mAddCandidateButton = findViewById<Button>(R.id.add_candidate_btn)
        tinydb = TinyDB(application)
        val mNextButton = findViewById<Button>(R.id.submit_details_btn)
        mAddCandidateTextInput = findViewById(R.id.candidate_til)
        val mRecyclerView = findViewById<RecyclerView>(R.id.names_rv)
        candidateRecyclerAdapter = CandidateRecyclerAdapter(mCandidates)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView)
        mRecyclerView.adapter = candidateRecyclerAdapter
        mNextButton.setOnClickListener {
            if (mCandidates.size != 0) {
                tinydb!!.putListString("Candidates", mCandidates)
                startActivity(Intent(this@CreateElectionTwo, CreateElectionThree::class.java))
            } else {
                Toast.makeText(this@CreateElectionTwo, "Please Add At least One Candidate",
                        Toast.LENGTH_SHORT).show()
            }
        }

        mAddCandidateButton.setOnClickListener {
            val name = mAddCandidateTextInput?.getEditText()!!.text.toString().trim { it <= ' ' }
            addCandidate(name)
        }
    }

    private fun addCandidate(cName: String) {
        mCandidates.add(cName)
        candidateRecyclerAdapter!!.notifyDataSetChanged()
        mAddCandidateTextInput!!.editText!!.setText("")
    }
}