package org.aossie.agoraandroid.ui.screens.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerEditField(
  list:List<String>,
  selectedIndex:Int,
  onItemSelected:(Int) -> Unit,
  modifier: Modifier=Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.background,
  borderColor: Color = MaterialTheme.colorScheme.outline
) {
  val expanded = remember { mutableStateOf(false) }
  val selectedOption = list[selectedIndex]
  Column {
    Row(
      modifier = modifier
        .border(
          border = BorderStroke(
            width = TextFieldDefaults.UnfocusedBorderThickness,
            color = borderColor
          ),
          shape = TextFieldDefaults.outlinedShape
        )
        .background(
          color = backgroundColor
        )
        .clickable {
          expanded.value= !expanded.value
        }
        .padding(vertical = 5.dp)
        .padding(start = 10.dp)
      ,
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(text = selectedOption)
      IconButton(onClick = {
        expanded.value= !expanded.value
      }) {
        Icon(
          imageVector = if(expanded.value) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
          contentDescription = ""
        )
      }
    }
    DropdownMenu(
      expanded = expanded.value,
      onDismissRequest = { expanded.value = false },
    ) {
      list.forEachIndexed { index, item ->
        DropdownMenuItem(
          text = {Text(text = item)},
          onClick = {
            onItemSelected(index)
            expanded.value = false
                    },
        )
      }
    }
  }
}