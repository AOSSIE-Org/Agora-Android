package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.aossie.agoraandroid.R.string

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryElectionSearchBar(
  searchText: String,
  onSearch: (String) -> Unit,
  content: @Composable() (ColumnScope.() -> Unit)? = null
) {
  var text by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }
  LaunchedEffect(key1 = searchText){
    if(searchText.isNotEmpty()){
      text = searchText
      onSearch(text)
    }
  }
  SearchBar(
    modifier = Modifier.fillMaxWidth(),
    query = text,
    onQueryChange = {
      text = it
      onSearch(it)
    },
    onSearch = {
      active = false
      onSearch(it)
    },
    active = active,
    onActiveChange = {
      active = it
    },
    placeholder = {
      Text(text = stringResource(id = string.search_hint))
    },
    leadingIcon = {
      Icon(
        imageVector = Rounded.Search,
        contentDescription = "Search Icons")
    },
    trailingIcon = {
      if(active){
        IconButton(onClick = {
          if(text.isNotEmpty()){
            text = ""
            onSearch("")
          }else{
            active = false
          }
        }) {
          Icon(
            imageVector = Rounded.Close,
            contentDescription = "Close Icons")
        }
      }
    },
    content = {
      content?.let { it ->
        it()
      }
    }
  )
}