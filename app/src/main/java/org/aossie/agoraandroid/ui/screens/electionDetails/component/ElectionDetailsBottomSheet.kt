package org.aossie.agoraandroid.ui.screens.electionDetails.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.common.component.BottomSheetIconActionButton
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent

@Composable
fun ElectionDetailsBottomSheet(
  onEvent: (ElectionDetailsScreenEvent) -> Unit
): @Composable() (ColumnScope.() -> Unit) {
  return {
    Box {
      Column() {
        Box(
          modifier = Modifier
            .height(30.dp)
            .fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Divider(
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
              .width(32.dp)
              .clip(RoundedCornerShape(2.dp))
          )
        }
        BottomSheetIconActionButton(
          iconStart = Rounded.Delete,
          text = stringResource(id = string.delete_election),
          iconColor = MaterialTheme.colorScheme.error
        ) {
          onEvent(ElectionDetailsScreenEvent.DeleteElectionClick)
        }
        BottomSheetIconActionButton(
          iconStartPainter = painterResource(id = drawable.ic_invite_voters),
          text = stringResource(id = string.invite_voter),
          iconColor = Color(0xff007BFF)
        ) {
          onEvent(ElectionDetailsScreenEvent.InviteVotersClick)
        }
        BottomSheetIconActionButton(
          iconStartPainter = painterResource(id = drawable.ic_voters),
          text = stringResource(id = string.voters)
        ) {
          onEvent(ElectionDetailsScreenEvent.ViewVotersClick)
        }
        BottomSheetIconActionButton(
          iconStartPainter = painterResource(id = drawable.ic_ballot),
          text = stringResource(id = string.ballot),
          iconColor = Color(0xff6F42C1)
        ) {
          onEvent(ElectionDetailsScreenEvent.BallotClick)
        }
        BottomSheetIconActionButton(
          iconStartPainter = painterResource(id = drawable.ic_result),
          text = stringResource(id = string.result),
          iconColor = Color(0xff28A745)
        ) {
          onEvent(ElectionDetailsScreenEvent.ResultClick)
        }
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}