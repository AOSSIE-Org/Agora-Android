package org.aossie.agoraandroid.utilities;

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("SpellCheckingInspection")
public class SharedPrefs2 {
    private static final String myPrefs = "myPrefs";
    private static final String NameKey = "electionName";
    private static final String StartTimeKey = "startTime";
    private static final String EndTimeKey = "endTime";
    private static final String IsInvitedKey = "isInvited";
    private static final String IsRealTimeKey = "isRealTime";
    private static final String VoterListVisibilityKey = "voterListVisibility";
    private static final String DescriptionKey = "electionDescription";
    private static final String VotingAlgoKey = "votingAlgorithm";
    private static final String BallotVisibilityKey = "ballotVisibility";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;


    public SharedPrefs2(Context context) {
        sharedPreferences = context.getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    //Saving name of election
    public void saveElectionName(String name) {
        editor.putString(NameKey, name);
        editor.commit();
    }

    public String getElectionName() {
        return sharedPreferences.getString(NameKey, null);
    }

    //Saving Election Description
    public void saveElectionDesc(String desc) {
        editor.putString(DescriptionKey, desc);
        editor.commit();

    }

    public String getElectionDesc() {
        return sharedPreferences.getString(DescriptionKey, null);
    }


    //Saving Start Time
    public void saveStartTime(String desc) {
        editor.putString(StartTimeKey, desc);
        editor.commit();

    }

    public String getStartTime() {
        return sharedPreferences.getString(StartTimeKey, null);
    }

    //Saving End Time
    public void saveEndTime(String desc) {
        editor.putString(EndTimeKey, desc);
        editor.commit();

    }

    public String getEndTime() {
        return sharedPreferences.getString(EndTimeKey, null);
    }

    //Save voters visibility
    public void saveIsRealTime(Boolean isRealTime) {
        editor.putBoolean(IsRealTimeKey, isRealTime);
        editor.commit();

    }

    public Boolean getIsRealTime() {
        return sharedPreferences.getBoolean(IsRealTimeKey, false);
    }

    //Save Real Time Results or not
    public void saveVoterListVisibility(Boolean voterListVisibility) {
        editor.putBoolean(VoterListVisibilityKey, voterListVisibility);
        editor.commit();

    }

    public Boolean getVoterListVisibility() {
        return sharedPreferences.getBoolean(VoterListVisibilityKey, false);
    }

    //Voters are invited or not
    public void saveIsInvite(Boolean isInvited) {
        editor.putBoolean(IsInvitedKey, isInvited);
        editor.commit();

    }
    public Boolean getIsInvite() {
        return sharedPreferences.getBoolean(IsInvitedKey, false);
    }


    public void saveVotingAlgo(String algo) {
        editor.putString(VotingAlgoKey, algo);
        editor.commit();

    }

    public String getVotingAlgo() {
        return sharedPreferences.getString(VotingAlgoKey, null);
    }


    public void saveBallotVisibility(String ballotVisibility) {
        editor.putString(BallotVisibilityKey, ballotVisibility);
        editor.commit();

    }

    public String getBallotVisibility() {
        return sharedPreferences.getString(BallotVisibilityKey, null);
    }


}
