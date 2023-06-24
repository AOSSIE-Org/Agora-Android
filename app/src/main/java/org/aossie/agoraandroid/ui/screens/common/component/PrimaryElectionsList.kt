package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionModel

@Composable
fun PrimaryElectionsList(
  elections: List<ElectionModel>,
  onItemClicked: (String) -> Unit
) {
  if(elections.isNotEmpty()){
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
      contentPadding = PaddingValues(20.dp)
    ) {
      itemsIndexed(elections) { index, item ->
        PrimaryElectionCard(
          election = item,
          onClick = onItemClicked
        )
      }
    }
  }else{
    Box(modifier = Modifier.fillMaxSize()) {
      Text(text = stringResource(id = string.empty_elections), modifier = Modifier.align(Alignment.Center))
    }
  }
}