package org.aossie.agoraandroid.ui.screens.castVote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.utilities.AppConstants.Status
import org.aossie.agoraandroid.utilities.AppConstants.Status.ACTIVE

@Composable
fun CandidatesData(
  candidates: List<String>?,
  status: Status?,
  checkedCandidates: MutableState<List<String>>
) {
  candidates?.let {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = stringResource(id = if(status == ACTIVE) string.select_candidate else string.candidates) + " :-",
        style = MaterialTheme.typography.labelLarge)

      it.forEachIndexed { index, candidate ->
        CandidateItem(
          candidateName = candidate,
          enableCheckBox = status == ACTIVE,
          checked = checkedCandidates.value.contains(candidate),
          onCheckedChange = {
            if(it) {
              checkedCandidates.value += candidate
            }else {
              checkedCandidates.value -= candidate
            }
          }
        )
      }
    }
  }
}

@Composable
fun CandidateItem(
  candidateName: String,
  enableCheckBox: Boolean,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit) {

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(
        shape = RoundedCornerShape(10.dp)
      )
      .shadow(
        elevation = if(checked) 10.dp else 1.dp,
      )
      .border(
        color = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.outline,
        width = 2.dp,
        shape = RoundedCornerShape(10.dp)
      )
      .background(
        color = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
      )
      .clickable {
        if(enableCheckBox) {
          onCheckedChange(!checked)
        }
      }
      .padding(horizontal = 15.dp, vertical = if(enableCheckBox) 6.dp else 12.dp)
    ,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = if(enableCheckBox) Arrangement.SpaceBetween else Arrangement.Center
  ) {
    Text(
      text = candidateName,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.SemiBold
    )
    if(enableCheckBox) {
      Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange
      )
    }
  }
}