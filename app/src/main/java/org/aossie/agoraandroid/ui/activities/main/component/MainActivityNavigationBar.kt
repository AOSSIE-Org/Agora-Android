package org.aossie.agoraandroid.ui.activities.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainActivityNavigationBar(
  currentSelected: Int,
  itemsList: List<Pair<String, Int>>,
  onNavigate: (Int) -> Unit
){

  NavigationBar(
    modifier = Modifier.clip(
      RoundedCornerShape(30.dp)
    )
  ) {
    itemsList.forEachIndexed { index, item ->
      NavigationBarItem(
        icon = {
          Icon(
            painter = painterResource(id = item.second),
            contentDescription = item.first,
            modifier = Modifier.size(24.dp)
          )
        },
        selected = currentSelected == index,
        onClick = {
          if(currentSelected!=index) {
            onNavigate(index)
          }
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.onPrimary,
          indicatorColor = MaterialTheme.colorScheme.primary,
        )
      )
    }
  }

}

@Composable
fun BottomBarItem(item: Pair<String, Int>, selected: Boolean, onClick: () -> Unit) {
  var color = MaterialTheme.colorScheme.inversePrimary
  if(!selected) {
    color = MaterialTheme.colorScheme.outline
  }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(10.dp)
      .clip(RoundedCornerShape(5.dp))
      .clickable { onClick.invoke() }
  ) {
    Icon(
      painter = painterResource(id = item.second),
      contentDescription = "Icon",
      tint = color,
      modifier = Modifier.size(30.dp)
    )
    if(selected){
      Text(
        text = "●",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall,
        color = color
      )
    }
  }
}