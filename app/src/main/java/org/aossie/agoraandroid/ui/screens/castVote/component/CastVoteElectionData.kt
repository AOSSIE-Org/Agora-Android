package org.aossie.agoraandroid.ui.screens.castVote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.ui.screens.electionDetails.component.TimeItem
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.AppConstants.Status.ACTIVE
import org.aossie.agoraandroid.utilities.AppConstants.Status.FINISHED
import org.aossie.agoraandroid.utilities.AppConstants.Status.PENDING
import org.aossie.agoraandroid.utilities.ElectionUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastVoteElectionData(election: ElectionDtoModel, electionStatus: (AppConstants.Status) -> Unit) {
  val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  val formattedStartingDate: Date? = formatter.parse(election.startingDate!!)
  val formattedEndingDate: Date? = formatter.parse(election.endingDate!!)
  val currentDate = Calendar.getInstance().time
  val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)
  val endDate = outFormat.format(formattedEndingDate!!)
  val startDate = outFormat.format(formattedStartingDate!!)
  val status = ElectionUtils.getEventStatus(currentDate, formattedStartingDate, formattedEndingDate)
  val icon:Int
  val iconColor: Color
  val colorContainer: Color
  val colorContent: Color

  when(status){
    PENDING -> {
      icon = R.drawable.ic_election_pending
      iconColor = MaterialTheme.colorScheme.error
      colorContainer = MaterialTheme.colorScheme.tertiaryContainer
      colorContent = MaterialTheme.colorScheme.onTertiaryContainer
    }
    ACTIVE -> {
      icon = R.drawable.ic_election_active
      iconColor = Color(0xff00C537)
      colorContainer = MaterialTheme.colorScheme.primaryContainer
      colorContent = MaterialTheme.colorScheme.onPrimaryContainer
    }
    FINISHED -> {
      icon = R.drawable.ic_election_finished
      iconColor = Color(0xff1877F2)
      colorContainer = MaterialTheme.colorScheme.secondaryContainer
      colorContent = MaterialTheme.colorScheme.onSecondaryContainer
    }
    null -> {
      icon = R.drawable.ic_election_active
      iconColor = MaterialTheme.colorScheme.primary
      colorContainer = MaterialTheme.colorScheme.primaryContainer
      colorContent = MaterialTheme.colorScheme.onPrimaryContainer
    }
  }
  LaunchedEffect(key1 = true) {
    status?.let(electionStatus)
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(15.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = election.name!!,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Box(
        modifier = Modifier
          .size(40.dp)
          .background(
            color = colorContainer,
            shape = RoundedCornerShape(8.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          modifier = Modifier.size(20.dp),
          painter = painterResource(id = icon),
          tint = iconColor,
          contentDescription = "Election Icons"
        )
      }
    }
    Column(
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = stringResource(id = string.description) + " :-",
        style = MaterialTheme.typography.labelLarge)
      Text(
        text = election.description!!,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
    TimeItem(
      label = stringResource(id = string.start_at),
      text = startDate
    )
    TimeItem(
      label = stringResource(id = string.end_at),
      text = endDate
    )
  }
}