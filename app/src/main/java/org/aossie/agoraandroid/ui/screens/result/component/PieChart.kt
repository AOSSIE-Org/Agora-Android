package org.aossie.agoraandroid.ui.screens.result.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.network.dto.CandidateDto
import org.aossie.agoraandroid.data.network.dto.ScoreDto
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import java.text.DecimalFormat

@Composable
fun PieChart(winnerDto: WinnerDtoModel) {

  val score = winnerDto.score?: ScoreDto(0,0)

  val total = (score.numerator ?: 0) + (score.denominator ?: 0)
  val denominatorProgress1 = ((score.denominator ?: 0).toDouble() / total.toDouble() * 100.0).roundTo()
  val numeratorProgress1 = ((score.numerator ?: 0).toDouble() / total.toDouble() * 100.0).roundTo()

  var denominatorProgress by remember {
    mutableStateOf(0.0)
  }

  var numeratorProgress by remember {
    mutableStateOf(0.0)
  }

  LaunchedEffect(key1 = denominatorProgress1){
    denominatorProgress = 0.0
    while(denominatorProgress.roundTo() != denominatorProgress1){
      denominatorProgress += 0.1
      delay(1)
    }
  }

  LaunchedEffect(key1 = numeratorProgress1){
    numeratorProgress = 0.0
    while(numeratorProgress.roundTo() != numeratorProgress1){
      numeratorProgress += 0.1
      delay(1)
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
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
      Column( modifier = Modifier.align(Alignment.CenterHorizontally)) {
        Text(
          text = stringResource(id = string.pie_chart),
          modifier = Modifier.align(Alignment.CenterHorizontally),
          fontWeight = FontWeight.SemiBold,
          style = MaterialTheme.typography.titleLarge
        )
        Text(
          text = stringResource(id = string.election_result_pie_chart),
          modifier = Modifier.align(Alignment.CenterHorizontally),
          style = MaterialTheme.typography.bodyLarge
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .weight(0.5f)
            .height(160.dp)
          ,
          contentAlignment = Alignment.Center
        ) {
          CicularProgress(numeratorProgress)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = stringResource(id = string.total_votes),
              style = MaterialTheme.typography.bodyMedium
            )
            Text(
              text = "${(score.denominator?:0 )+ (score.numerator?:0)}",
              style = MaterialTheme.typography.titleLarge
            )
          }
        }
        DataNamesColumn(
          winnerDto.candidate,
          denominatorProgress1,
          numeratorProgress1
        )
      }
    }
  }
}

@Composable
fun CicularProgress(numeratorProgress: Double) {
  val color1 = MaterialTheme.colorScheme.onTertiaryContainer
  val color2 = MaterialTheme.colorScheme.primary
  Canvas(
    modifier = Modifier
      .size(150.dp)
      .progressSemantics(numeratorProgress.toFloat())
  ) {
    val startAngle = 270f
    val sweep = (numeratorProgress.toFloat() * 360f) / 100f

    drawCircularIndicator(
      startAngle,
      360f,
      color1,
      Stroke(
        width = 20.dp.toPx(),
        cap = StrokeCap.Round
      )
    )
    drawCircularIndicator(
      startAngle,
      sweep,
      color2,
      Stroke(
        width = 20.dp.toPx(),
        cap = StrokeCap.Round
      )
    )
  }
}

private fun DrawScope.drawCircularIndicator(
  startAngle: Float,
  sweep: Float,
  color: Color,
  stroke: Stroke
) {
  val diameterOffset = stroke.width / 2
  val arcDimen = size.width - 2 * diameterOffset
  drawArc(
    color = color,
    startAngle = startAngle,
    sweepAngle = sweep,
    useCenter = false,
    topLeft = Offset(diameterOffset, diameterOffset),
    size = Size(arcDimen, arcDimen),
    style = stroke
  )
}

@Composable
fun DataNamesColumn(candidate: CandidateDto?, denominatorProgress: Double, numeratorProgress: Double) {
  Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
    Text(
      text = "${denominatorProgress}%",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onTertiaryContainer
    )
    CandidateRow(stringResource(id = string.others), MaterialTheme.colorScheme.onTertiaryContainer)
    Spacer(modifier = Modifier.height(20.dp))
    Text(
      text = "${numeratorProgress}%",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.primary
    )
    CandidateRow(candidate?.name, MaterialTheme.colorScheme.primary)
  }
}


fun Double.roundTo(): Double {
  val df =  DecimalFormat("#.#");
  return df.format(this).toDouble()
}