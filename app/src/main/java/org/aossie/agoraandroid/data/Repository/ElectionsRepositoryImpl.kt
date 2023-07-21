package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.network.api.Api
import org.aossie.agoraandroid.data.network.api.ApiRequest
import org.aossie.agoraandroid.data.network.dto.CastVoteDto
import org.aossie.agoraandroid.data.network.dto.ElectionDto
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ElectionsRepositoryImpl
@Inject
constructor(
  private val api: Api,
  private val db: AppDatabase,
  private val prefs: PreferenceProvider
) : ApiRequest(), ElectionsRepository {

  private val elections = MutableLiveData<List<ElectionResponse>>()

  override suspend fun fetchAndSaveElections() = fetchElections()

  override fun getElections(): Flow<List<Election>> {
    return db.getElectionDao()
      .getElections()
  }

  override fun getFinishedElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getFinishedElectionsCount(currentDate)

  override fun getPendingElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getPendingElectionsCount(currentDate)

  override fun getTotalElectionsCount(): LiveData<Int> =

    db.getElectionDao()
      .getTotalElectionsCount()

  override fun getActiveElectionsCount(currentDate: String): LiveData<Int> =

    db.getElectionDao()
      .getActiveElectionsCount(currentDate)

  override suspend fun saveElections(elections: List<ElectionResponse>) {
    val electionEntity = mutableListOf<Election>()
    withContext(Dispatchers.Default) {
      elections.forEach {
        electionEntity.add(
          Election(
            it.id,
            it.name,
            it.description,
            it.electionType,
            it.creatorName,
            it.creatorEmail,
            it.start,
            it.end,
            it.realtimeResult.toString(),
            it.votingAlgo,
            it.candidates,
            it.ballotVisibility,
            it.voterListVisibility.toString(),
            it.isInvite,
            it.isCompleted,
            it.isStarted,
            it.createdTime,
            it.adminLink,
            it.inviteCode,
            it.ballot,
            it.voterList,
            it.winners
          )
        )
      }
    }
    prefs.setUpdateNeeded(false)
    db.getElectionDao()
      .deleteAllElections()
    db.getElectionDao()
      .saveElections(electionEntity)
  }

  override fun getPendingElections(currentDate: String): Flow<List<Election>> =

    db.getElectionDao()
      .getPendingElections(currentDate)

  override suspend fun fetchElections() {
    val isNeeded = prefs.getUpdateNeeded().first()
    if (isNeeded) {
      try {
        val response = apiRequest { api.getAllElections() }
        response.elections.let {
          saveElections(it)
          elections.postValue(it)
        }
        Timber.d(response.toString())
      } catch (e: NoInternetException) {
      } catch (e: ApiException) {
      } catch (e: SessionExpirationException) {
      } catch (e: IOException) {
      }
    }
  }

  override fun getFinishedElections(currentDate: String): Flow<List<Election>> =

    db.getElectionDao()
      .getFinishedElections(currentDate)

  override fun getActiveElections(currentDate: String): Flow<List<Election>> =

    db.getElectionDao()
      .getActiveElections(currentDate)

  override fun getElectionById(id: String): Flow<Election> =

    db.getElectionDao()
      .getElectionById(id)

  override suspend fun deleteElection(
    id: String
  ): List<String> {
    return apiRequest { api.deleteElection(id) }
  }

  override suspend fun getVoters(
    id: String
  ): List<VotersDto> {
    return apiRequest { api.getVoters(id) }.voterList
  }

  override suspend fun getBallots(
    id: String
  ): Ballots {
    return apiRequest { api.getBallot(id) }
  }

  override suspend fun sendVoters(
    id: String,
    votersData: List<VotersDto>
  ): List<String> {
    return apiRequest { api.sendVoters(id, votersData) }
  }

  override suspend fun createElection(
    electionData: ElectionDto
  ): List<String> {
    return apiRequest { api.createElection(electionData) }
  }

  override suspend fun verifyVoter(
    id: String
  ): ElectionDto {
    return apiRequest { api.verifyVoter(id) }
  }

  override suspend fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ): List<String> {

    return apiRequest { api.castVote(id, CastVoteDto(ballotInput, passCode)) }
  }

  override suspend fun getResult(
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
