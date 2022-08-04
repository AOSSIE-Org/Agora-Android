package org.aossie.agoraandroid.utilities

import androidx.lifecycle.LiveData

interface EntityMapper <Entity , DomainModel> {

  fun mapFromEntity(entity: Entity) : DomainModel
  fun mapToEntity(domainModel: DomainModel) : Entity

}