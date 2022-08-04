package org.aossie.agoraandroid.utilities

interface EntityMapper<Entity, DomainModel> {

  fun mapFromEntity(entity: Entity): DomainModel
  fun mapToEntity(domainModel: DomainModel): Entity

}
