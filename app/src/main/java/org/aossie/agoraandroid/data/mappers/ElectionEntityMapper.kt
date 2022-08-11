package org.aossie.agoraandroid.data.mappers

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.utilities.EntityMapper

class ElectionEntityMapper : EntityMapper<Election, ElectionModel> {
  override fun mapFromEntity(entity: Election): ElectionModel {
    return ElectionModel(
      _id = entity._id,
      name = entity.name,
      description = entity.description,
      electionType = entity.electionType,
      creatorName = entity.creatorName,
      creatorEmail = entity.creatorEmail,
      start = entity.start,
      end = entity.end,
      realtimeResult = entity.realtimeResult,
      votingAlgo = entity.votingAlgo,
      candidates = entity.candidates,
      ballotVisibility = entity.ballotVisibility,
      voterListVisibility = entity.voterListVisibility,
      isInvite = entity.isInvite,
      isCompleted = entity.isCompleted,
      isStarted = entity.isStarted,
      createdTime = entity.createdTime,
      adminLink = entity.adminLink,
      inviteCode = entity.inviteCode,
      ballot = entity.ballot,
      isCounted = entity.isCounted,
      noVacancies = entity.noVacancies
    )
  }

  override fun mapToEntity(domainModel: ElectionModel): Election {
    TODO("Not yet implemented")
  }

  private fun mapFromEntityList(listElection: List<Election>): List<ElectionModel> {
    return listElection.map { mapFromEntity(it) }
  }

  fun mapFromEntityLiveDataList(electionLiveDataList: LiveData<List<Election>>): LiveData<List<ElectionModel>> {
    return Transformations.map(
      electionLiveDataList
    ) { mapFromEntityList(it) }
  }
}
