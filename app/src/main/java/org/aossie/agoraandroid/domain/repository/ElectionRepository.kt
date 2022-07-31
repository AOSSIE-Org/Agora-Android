package org.aossie.agoraandroid.domain.repository

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.remote.dto.ElectionDto
import org.aossie.agoraandroid.data.remote.dto.VotersDto
import org.aossie.agoraandroid.data.remote.dto.WinnerDto
import org.aossie.agoraandroid.data.remote.models.Ballots
import org.aossie.agoraandroid.data.remote.models.ElectionResponse

interface ElectionRepository {
  fun getElections(): LiveData<List<Election>>
  fun getFinishedElectionsCount(currentDate: String): LiveData<Int>
  fun getPendingElectionsCount(currentDate: String): LiveData<Int>
  fun getTotalElectionsCount(): LiveData<Int>
  fun getActiveElectionsCount(currentDate: String): LiveData<Int>
  suspend fun saveElections(elections: List<ElectionResponse>)
  fun getPendingElections(currentDate: String): LiveData<List<Election>>
  suspend fun fetchElections()
  fun getFinishedElections(currentDate: String): LiveData<List<Election>>
  fun getActiveElections(currentDate: String): LiveData<List<Election>>
  fun getElectionById(id: String): LiveData<Election>
  suspend fun deleteElection(id: String): List<String>
  suspend fun getVoters(id: String): List<VotersDto>
  suspend fun getBallots(id: String): Ballots
  suspend fun sendVoters(id: String, votersData: List<VotersDto>): List<String>
  suspend fun createElection(electionData: ElectionDto): List<String>
  suspend fun verifyVoter(id: String): ElectionDto
  suspend fun castVote(id: String, ballotInput: String, passCode: String): List<String>
  suspend fun getResult(id: String): List<WinnerDto>?
}
