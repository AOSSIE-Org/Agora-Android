package org.aossie.agoraandroid.ui.screens.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.network.dto.CandidateDto
import org.aossie.agoraandroid.data.network.dto.ScoreDto
import org.aossie.agoraandroid.domain.model.WinnerDtoModel

@Composable
fun BarGraph(winnerDto: WinnerDtoModel) {

  val score = winnerDto.score?: ScoreDto(0,0)

  val denominatorProgress1 = ((score.denominator?: 0).toDouble() / ((score.numerator?:0) + (score.denominator?:0)).toDouble() * 100).toInt()
  val numeratorProgress1 = ((score.numerator?: 0).toDouble() / ((score.numerator?:0) + (score.denominator?:0)).toDouble() * 100).toInt()

  var denominatorProgress by remember {
    mutableStateOf(0)
  }

  var numeratorProgress by remember {
    mutableStateOf(0)
  }

  LaunchedEffect(key1 = denominatorProgress1){
    denominatorProgress = 0
    while(denominatorProgress!=denominatorProgress1){
      denominatorProgress++
      delay(5)
    }
  }

  LaunchedEffect(key1 = numeratorProgress1){
    numeratorProgress = 0
    while(numeratorProgress!=numeratorProgress1){
      numeratorProgress++
      delay(5)
    }
  }

  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 15.dp
    ),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer,
      contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
  ) {
    Column(
      modifier = Modifier.padding(20.dp)
    ) {
      Text(
        text = stringResource(id = string.bar_chart),
        modifier = Modifier.align(Alignment.CenterHorizontally),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleLarge
      )
      Text(
        text = stringResource(id = string.election_result_bar_chart),
        modifier = Modifier.align(Alignment.CenterHorizontally),
        style = MaterialTheme.typography.bodyLarge
      )
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ) {
          CustomVerticalProgress(
            progress = denominatorProgress,
            votes = score.denominator?:0,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            textColor = MaterialTheme.colorScheme.tertiaryContainer
          )
          Spacer(modifier = Modifier.width(50.dp))
          CustomVerticalProgress(
            progress = numeratorProgress,
            votes = score.numerator?:0,
            color = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary
          )
        }
        Divider(
          thickness = 1.dp,
          color = MaterialTheme.colorScheme.outline
        )
      }
      Spacer(modifier = Modifier.height(10.dp))
      DataNamesRow(winnerDto.candidate)
    }
  }
}

@Composable
fun DataNamesRow(candidate: CandidateDto?) {
  Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
    CandidateRow(stringResource(id = string.others),MaterialTheme.colorScheme.onTertiaryContainer)
    Spacer(modifier = Modifier.width(20.dp))
    CandidateRow(candidate?.name,MaterialTheme.colorScheme.primary)
  }
}

@Composable
fun CandidateRow(
  name: String?,
  color: Color
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Box(
      modifier = Modifier
        .size(12.dp)
        .clip(CircleShape)
        .background(
          color = color
        )
    )
    Text(
      text = name?:"",
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@Composable
fun CustomVerticalProgress(
  progress: Int,
  votes: Int,
  color: Color,
  textColor: Color
) {

  val width = 40.dp
  val height = 200.dp
  var progressHeight = height * progress / 100
  if(progressHeight < 5.dp){
    progressHeight = 5.dp
  }
  Box(
    modifier = Modifier
      .width(width)
      .height(height),
    contentAlignment = Alignment.BottomCenter
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if(progressHeight == 5.dp) {
        Text(
          text = "$votes",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
        )
      }
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .width(width)
          .background(
            color = color
          )
          .height(progressHeight)
      ) {
        Text(
          text = "$votes",
          modifier = Modifier.align(Alignment.Center),
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = textColor
        )
      }
    }
  }
}