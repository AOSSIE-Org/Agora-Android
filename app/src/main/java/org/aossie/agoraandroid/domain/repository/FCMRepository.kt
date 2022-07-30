package org.aossie.agoraandroid.domain.repository

interface FCMRepository {
  suspend fun sendFCM( topicId: String, title: String, body: String, electionId: String )
}