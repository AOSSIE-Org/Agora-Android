package org.aossie.agoraandroid.ui.fragments.createelection

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class ElectionDetailsSharedPrefs(context: Context) {
  private val sharedPreferences: SharedPreferences
  private val editor: Editor

  //Saving name of election
  fun saveElectionName(name: String?) {
    editor.putString(NameKey, name)
    editor.commit()
  }

  val electionName: String?
    get() = sharedPreferences.getString(NameKey, null)

  //Saving Election Description
  fun saveElectionDesc(desc: String?) {
    editor.putString(DescriptionKey, desc)
    editor.commit()
  }

  val electionDesc: String?
    get() = sharedPreferences.getString(DescriptionKey, null)

  //Saving Start Time
  fun saveStartTime(desc: String?) {
    editor.putString(StartTimeKey, desc)
    editor.commit()
  }

  val startTime: String?
    get() = sharedPreferences.getString(StartTimeKey, null)

  //Saving End Time
  fun saveEndTime(desc: String?) {
    editor.putString(EndTimeKey, desc)
    editor.commit()
  }

  val endTime: String?
    get() = sharedPreferences.getString(EndTimeKey, null)

  //Save voters visibility
  fun saveIsRealTime(isRealTime: Boolean?) {
    editor.putBoolean(IsRealTimeKey, isRealTime!!)
    editor.commit()
  }

  val isRealTime: Boolean
    get() = sharedPreferences.getBoolean(IsRealTimeKey, false)

  //Save Real Time Results or not
  fun saveVoterListVisibility(voterListVisibility: Boolean?) {
    editor.putBoolean(
        VoterListVisibilityKey, voterListVisibility!!
    )
    editor.commit()
  }

  val voterListVisibility: Boolean
    get() = sharedPreferences.getBoolean(
        VoterListVisibilityKey, false
    )

  //Voters are invited or not
  fun saveIsInvite(isInvited: Boolean?) {
    editor.putBoolean(IsInvitedKey, isInvited!!)
    editor.commit()
  }

  val isInvite: Boolean
    get() = sharedPreferences.getBoolean(IsInvitedKey, false)

  fun saveVotingAlgo(algo: String?) {
    editor.putString(VotingAlgoKey, algo)
    editor.commit()
  }

  val votingAlgo: String?
    get() = sharedPreferences.getString(VotingAlgoKey, null)

  fun saveBallotVisibility(ballotVisibility: String?) {
    editor.putString(BallotVisibilityKey, ballotVisibility)
    editor.commit()
  }

  val ballotVisibility: String?
    get() = sharedPreferences.getString(
        BallotVisibilityKey, null
    )

  fun saveElectionDetails(electionDetails: String?) {
    editor.putString(ElectionDetailsKey, electionDetails)
    editor.commit()
  }

  val electionDetails: String?
    get() = sharedPreferences.getString(
        ElectionDetailsKey, null
    )

  fun clearElectionData() {
    editor.putString(NameKey, null)
    editor.putString(StartTimeKey, null)
    editor.putString(EndTimeKey, null)
    editor.putString(IsInvitedKey, null)
    editor.putString(IsRealTimeKey, null)
    editor.putString(VoterListVisibilityKey, null)
    editor.putString(DescriptionKey, null)
    editor.putString(VotingAlgoKey, null)
    editor.putString(BallotVisibilityKey, null)
    editor.commit()
  }

  companion object {
    private const val myPrefs = "myPrefs"
    private const val NameKey = "electionName"
    private const val StartTimeKey = "startTime"
    private const val EndTimeKey = "endTime"
    private const val IsInvitedKey = "isInvited"
    private const val IsRealTimeKey = "isRealTime"
    private const val VoterListVisibilityKey = "voterListVisibility"
    private const val DescriptionKey = "electionDescription"
    private const val VotingAlgoKey = "votingAlgorithm"
    private const val BallotVisibilityKey = "ballotVisibility"
    private const val ElectionDetailsKey = "electionDetails"
  }

  init {
    sharedPreferences = context.getSharedPreferences(
        myPrefs, Context.MODE_PRIVATE
    )
    editor = sharedPreferences.edit()
  }
}