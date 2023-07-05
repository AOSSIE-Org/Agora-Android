package org.aossie.agoraandroid.ui.screens.createElection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CandidateEditField(
  text: String,
  onTextChange: (String) -> Unit,
  candidatesList: List<String> = emptyList(),
  addCandidateClick: (String) -> Unit,
  deleteCandidateClick: (String) -> Unit,
  openCsvFileClick: () -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = stringResource(id = string.candidates),
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      candidatesList.forEach {
        CandidateItem(text = it,onDeleteClick = deleteCandidateClick)
      }
      OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = {
          Text(text = stringResource(id = string.enter_candidate_s_name))
        },
        shape = TextFieldDefaults.outlinedShape,
        trailingIcon = {
          if(text.isNotEmpty()){
            IconButton(onClick = {
              if(text.trim().isNotEmpty()){
                addCandidateClick(text)
                onTextChange("")
              }
            }) {
              Icon(imageVector = Rounded.PersonAdd, contentDescription = "")
            }
          }
        }
      )
      Box(
        modifier = Modifier
          .size(44.dp)
          .background(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp)
          )
          .clip(RoundedCornerShape(12.dp))
          .border(
            width = 1.dp,
            color = Color(0xff34A853),
            shape = RoundedCornerShape(12.dp)
          )
          .clickable { openCsvFileClick.invoke() },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          painter = painterResource(id = drawable.ic_csv_upload),
          contentDescription = "",
          tint = Color(0xff34A853)
        )
      }
    }
  }
}