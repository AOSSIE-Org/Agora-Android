package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.first
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.dto.CastVoteDto
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.data.dto.WinnerDto
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ElectionsRepository
@Inject
constructor(
  private val api: Api,
  private val db: AppDatabase,
  private val prefs: PreferenceProvider
) : ApiRequest() {

  private val elections = MutableLiveData<List<ElectionResponse>>()

  init {
    elections.observeForever {
      saveElections(it)
    }
  }

  suspend fun fetchAndSaveElections() = fetchElections()

  fun getElections(): LiveData<List<Election>> {
    return db.getElectionDao()
      .getElections()
  }

  fun getFinishedElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getFinishedElectionsCount(currentDate)

  fun getPendingElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getPendingElectionsCount(currentDate)

  fun getTotalElectionsCount(): LiveData<Int> =

    db.getElectionDao()
      .getTotalElectionsCount()

  fun getActiveElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getActiveElectionsCount(currentDate)

  private fun saveElections(elections: List<ElectionResponse>) {
    Coroutines.io {
      val electionEntity = mutableListOf<Election>()
      elections.forEach {
        electionEntity.add(
          Election(it.id, it.name, it.description, it.electionType, it.creatorName, it.creatorEmail, it.start, it.end, it.realtimeResult.toString(), it.votingAlgo, it.candidates, it.ballotVisibility, it.voterListVisibility.toString(), it.isInvite, it.isCompleted, it.isStarted, it.createdTime, it.adminLink, it.inviteCode, it.ballot, it.voterList, it.winners)
        )
      }
      prefs.setUpdateNeeded(false)
      db.getElectionDao()
        .deleteAllElections()
      db.getElectionDao()
        .saveElections(electionEntity)
    }
  }

  fun getPendingElections(currentDate: String): LiveData<List<Election>> =

    db.getElectionDao()
      .getPendingElections(currentDate)

  private suspend fun fetchElections() {
    val isNeeded = prefs.getUpdateNeeded().first()
    if (isNeeded) {
      try {
        val response = apiRequest { api.getAllElections() }
        elections.postValue(response.elections)
        Timber.d(response.toString())
      } catch (e: NoInternetException) {
      } catch (e: ApiException) {
      } catch (e: SessionExpirationException) {
      } catch (e: IOException) {
      }
    }
  }

  fun getFinishedElections(currentDate: String): LiveData<List<Election>> =

    db.getElectionDao()
      .getFinishedElections(currentDate)

  fun getActiveElections(currentDate: String): LiveData<List<Election>> =

    db.getElectionDao()
      .getActiveElections(currentDate)

  fun getElectionById(id: String): LiveData<Election> =

    db.getElectionDao()
      .getElectionById(id)

  suspend fun deleteElection(
    id: String
  ): List<String> {
    return apiRequest { api.deleteElection(id) }
  }

  suspend fun getVoters(
    id: String
  ): List<VotersDto> {
    return apiRequest { api.getVoters(id) }.voterList
  }

  suspend fun getBallots(
    id: String
  ): Ballots {
    return apiRequest { api.getBallot(id) }
  }

  suspend fun sendVoters(
    id: String,
    votersData: List<VotersDto>
  ): List<String> {
    return apiRequest { api.sendVoters(id, votersData) }
  }

  suspend fun createElection(
    electionData: ElectionDto
  ): List<String> {
    return apiRequest { api.createElection(electionData) }
  }

  suspend fun verifyVoter(
    id: String
  ): ElectionDto {
    return apiRequest { api.verifyVoter(id) }
  }

  suspend fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ): List<String> {

    return apiRequest { api.castVote(id, CastVoteDto(ballotInput, passCode)) }
  }

  suspend fun getResult(
    id: String
  ): List<WinnerDto>? {
    val response = api.getResult(id)
    return if (response.message() == AppConstants.ok) {
      apiRequest { response }
    } else {
      null
    }
  }
}
