package org.aossie.agoraandroid.utilities

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class SwipeDetector(context: Context) : OnTouchListener {
  private val gestureDetector: GestureDetector
  override fun onTouch(
    v: View,
    event: MotionEvent
  ): Boolean {
    when (event.action) {
      MotionEvent.ACTION_DOWN ->
        showRipple()
      else ->
        hideRipple()
    }
    return gestureDetector.onTouchEvent(event)
  }

  private inner class GestureListener : SimpleOnGestureListener() {
    override fun onDown(e: MotionEvent): Boolean {
      return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
      onClick()
      return super.onSingleTapConfirmed(e)
    }

    override fun onFling(
      e1: MotionEvent,
      e2: MotionEvent,
      velocityX: Float,
      velocityY: Float
    ): Boolean {
      val diffY = e2.y - e1.y
      val diffX = e2.x - e1.x
      if (abs(diffX) > abs(diffY)) {
        if (abs(diffX) > SWIPE_THRESHOLD && abs(
            velocityX
          ) > SWIPE_VELOCITY_THRESHOLD
        ) {
          if (diffX > 0) {
            onSwipeRight()
          } else {
            onSwipeLeft()
          }
          return true
        }
      }
      return false
    }
  }

  companion object {
    private const val SWIPE_THRESHOLD = 100
    private const val SWIPE_VELOCITY_THRESHOLD = 100
  }

  open fun onSwipeRight() {}
  open fun onSwipeLeft() {}
  open fun onClick() {}
  open fun showRipple() {}
  open fun hideRipple() {}

  init {
    gestureDetector = GestureDetector(context, GestureListener())
  }
}
