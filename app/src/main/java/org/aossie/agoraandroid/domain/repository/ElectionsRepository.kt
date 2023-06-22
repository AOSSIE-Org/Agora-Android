package org.aossie.agoraandroid.domain.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.network.dto.ElectionDto
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionListResponse
import org.aossie.agoraandroid.data.network.responses.ElectionResponse

interface ElectionsRepository {
  suspend fun fetchAndSaveElections(): ElectionListResponse
  fun getElections(): Flow<List<Election>>
  fun getFinishedElectionsCount(currentDate: String): LiveData<Int>
  fun getPendingElectionsCount(currentDate: String): LiveData<Int>
  fun getTotalElectionsCount(): LiveData<Int>
  fun getActiveElectionsCount(currentDate: String): LiveData<Int>
  suspend fun saveElections(elections: List<ElectionResponse>)
  fun getPendingElections(currentDate: String): Flow<List<Election>>
  suspend fun fetchElections(): ElectionListResponse
  fun getFinishedElections(currentDate: String): Flow<List<Election>>
  fun getActiveElections(currentDate: String): Flow<List<Election>>
  fun getElectionById(id: String): Flow<Election>
  suspend fun deleteElection(id: String): List<String>
  suspend fun getVoters(id: String): List<VotersDto>
  suspend fun getBallots(id: String): Ballots
  suspend fun sendVoters(id: String, votersData: List<VotersDto>): List<String>
  suspend fun createElection(electionData: ElectionDto): List<String>
  suspend fun verifyVoter(id: String): ElectionDto
  suspend fun castVote(id: String, ballotInput: String, passCode: String): List<String>
  suspend fun getResult(id: String): List<WinnerDto>?
}
