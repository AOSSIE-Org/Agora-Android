package org.aossie.agoraandroid.ui.screens.welcome.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.utilities.AppConstants

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ImageSlider(
  images: Array<Pair<Int, Int>>,
  modifier: Modifier = Modifier,
  scrollDelay:Long = AppConstants.WELCOME_SCREEN_SCROLL_DELAY
) {
  val pagerState = rememberPagerState()
  val coroutineScope = rememberCoroutineScope()

  val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
  if (!isDragged) {
    LaunchedEffect(key1 = true) {
      while (true) {
        delay(scrollDelay)
          val nextIndex = (pagerState.currentPage + 1) % images.size
        try{
            pagerState.animateScrollToPage(nextIndex)
        }catch(e:Exception){

        }
      }
    }
  }

  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly
  ) {
    AsyncImage(
      model = Builder(LocalContext.current)
        .data(images[pagerState.currentPage].second)
        .dispatcher(Dispatchers.IO)
        .crossfade(500)
        .build(),
      contentDescription = "Image",
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxWidth().height(260.dp)
    )
    HorizontalPager(
      modifier = Modifier.fillMaxWidth(),
      pageCount = images.size,
      state = pagerState,
      key = { "${images[it]}$it" },
    ) { index ->
      Text(
        text = stringResource(id = images[index].first),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
      )
    }

  DotsIndicator(
    totalDots = images.size,
    selectedIndex = pagerState.currentPage,
    dotSize = 9.dp,
    onDotClicked = {
      if (it != pagerState.currentPage) {
        coroutineScope.launch {
          pagerState.animateScrollToPage(it)
        }
      }
    }
  )
  }
}

@Composable
fun DotsIndicator(
  modifier: Modifier = Modifier,
  totalDots: Int,
  selectedIndex: Int,
  selectedColor: Color = MaterialTheme.colorScheme.onBackground,
  unSelectedColor: Color = MaterialTheme.colorScheme.onBackground,
  dotSize: Dp,
  onDotClicked:(Int) -> Unit
) {
  LazyRow(
    modifier = modifier
      .wrapContentWidth()
      .wrapContentHeight(),
    horizontalArrangement = Arrangement.spacedBy(5.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    items(totalDots) { index ->
      IndicatorDot(
        color = if (index == selectedIndex) selectedColor else unSelectedColor,
        size = dotSize,
        selected = index == selectedIndex,
        onClick = {
          onDotClicked(index)
        }
      )
      if (index != totalDots - 1) {
        Spacer(modifier = Modifier.padding(horizontal = 2.dp))
      }
    }
  }
}
@Composable
fun IndicatorDot(
  modifier: Modifier = Modifier,
  size: Dp,
  color: Color,
  onClick: () -> Unit,
  selected: Boolean
) {
  if(selected){
    Box(
      modifier = modifier
        .size(size)
        .clip(CircleShape)
        .background(color)
        .clickable { onClick() }
    )
  }else{
    Box(
      modifier = modifier
        .size(size+1.dp)
        .border(
          color = color,
          shape = CircleShape,
          width = 1.dp
        )
        .clickable { onClick() }
    )
  }
}