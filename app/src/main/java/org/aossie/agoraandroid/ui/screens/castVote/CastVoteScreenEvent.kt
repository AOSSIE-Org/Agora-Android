package org.aossie.agoraandroid.ui.screens.castVote

sealed class CastVoteScreenEvent{
  data class CastVoteClick(val candidates: List<String>):CastVoteScreenEvent()
}
