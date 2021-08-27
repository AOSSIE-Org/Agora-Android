package org.aossie.agoraandroid.utilities

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.updateLayoutParams
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import org.aossie.agoraandroid.databinding.SpotlightLayoutBinding
import org.aossie.agoraandroid.utilities.AppConstants.SPOTLIGHT_SCROLL_DURATION

data class TargetData(
  val targetView: View,
  val title: String,
  val description: String,
  val isRectangle: Boolean = true,
)

fun getTarget(
  activity: Activity,
  targetData: TargetData,
  dismissSpotlight: () -> Unit,
  skipSpotlight: () -> Unit,
): Target {
  val overlayView = initOverlayView(
    activity, targetData, dismissSpotlight, skipSpotlight
  )
  val padding = 25f
  return Target.Builder()
    .setAnchor(targetData.targetView)
    .setShape(
      if (targetData.isRectangle) {
        RoundedRectangle(
          targetData.targetView.height.toFloat() + padding,
          targetData.targetView.width.toFloat() + padding, 20f
        )
      } else {
        Circle(targetData.targetView.height.toFloat())
      }
    )
    .setOverlay(overlayView)
    .build()
}

private fun initOverlayView(
  activity: Activity,
  targetData: TargetData,
  dismissSpotlight: () -> Unit,
  skipSpotlight: () -> Unit,
): View {
  val rootView = FrameLayout(activity.baseContext)
  val binding = SpotlightLayoutBinding.inflate(
    activity.layoutInflater, rootView, false
  )

  val targetWidth: Float = targetData.targetView.width.toFloat()
  val targetHeight: Float = targetData.targetView.height.toFloat()

  val location = IntArray(2)
  targetData.targetView.getLocationOnScreen(location)
  val targetLeft: Float = location[0].toFloat()
  val targetTop: Float = location[1].toFloat()
  val targetRight: Float = targetLeft + targetWidth
  val targetBottom: Float = targetTop + targetHeight

  binding.title.also {
    if (targetData.title.isEmpty()) {
      it.visibility = View.GONE
    } else {
      it.text = targetData.title
    }
  }
  binding.description.also {
    if (targetData.description.isEmpty()) {
      it.visibility = View.GONE
    } else {
      it.text = targetData.description
    }
  }

  binding.skipTop.setOnClickListener {
    skipSpotlight.invoke()
  }
  binding.skipBottom.setOnClickListener {
    skipSpotlight.invoke()
  }

  binding.root.setOnClickListener {
    dismissSpotlight.invoke()
  }

  binding.contentLayout.updateLayoutParams<FrameLayout.LayoutParams> {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val containerHeight = displayMetrics.heightPixels
    val containerWidth = displayMetrics.widthPixels
    val containerWidthCenter = containerWidth / 2
    val targetWidthCenterFromLeft: Float = targetLeft + targetWidth / 2
    val targetWidthCenterFromRight: Float = containerWidth - targetWidthCenterFromLeft
    val bottomFreeSpace: Float = containerHeight - targetBottom
    val approxSpotlightSize = 350
    val padding = 80
    val skipHorizontalMargin = 20
    val skipVerticalMargin = 5
    val skipParams = LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.WRAP_CONTENT,
      LinearLayout.LayoutParams.WRAP_CONTENT
    )
    if (bottomFreeSpace > approxSpotlightSize) {
      this.topMargin = targetBottom.toInt() + 50
      if (targetWidthCenterFromLeft >= containerWidthCenter) {
        binding.description.setPadding(0, 0, padding, 0)
        binding.arrowUpRight.visibility = View.VISIBLE
        this.gravity = Gravity.END
        this.rightMargin = targetWidthCenterFromRight.toInt()
        skipParams.apply {
          gravity = Gravity.START
          leftMargin = skipHorizontalMargin
          topMargin = skipVerticalMargin
        }
      } else {
        binding.description.setPadding(padding, 0, 0, 0)
        binding.arrowUpLeft.visibility = View.VISIBLE
        this.gravity = Gravity.START
        this.leftMargin = targetWidthCenterFromLeft.toInt()
        skipParams.apply {
          gravity = Gravity.END
          rightMargin = skipHorizontalMargin
          topMargin = skipVerticalMargin
        }
      }
      binding.skipBottom.apply {
        visibility = View.VISIBLE
        layoutParams = skipParams
      }
    } else {
      this.topMargin = targetTop.toInt() - approxSpotlightSize + 50
      if (targetWidthCenterFromLeft >= containerWidthCenter) {
        binding.title.setPadding(0, 0, padding, 0)
        binding.arrowDownRight.visibility = View.VISIBLE
        this.gravity = Gravity.END
        this.rightMargin = targetWidthCenterFromRight.toInt()
        skipParams.apply {
          gravity = Gravity.START
          leftMargin = skipHorizontalMargin
          bottomMargin = skipVerticalMargin
        }
      } else {
        binding.title.setPadding(padding, 0, 0, 0)
        binding.arrowDownLeft.visibility = View.VISIBLE
        this.gravity = Gravity.START
        this.leftMargin = targetWidthCenterFromLeft.toInt()
        skipParams.apply {
          gravity = Gravity.END
          rightMargin = skipHorizontalMargin
          bottomMargin = skipVerticalMargin
        }
      }
      binding.skipTop.apply {
        visibility = View.VISIBLE
        layoutParams = skipParams
      }
    }
  }
  return binding.root
}

fun scrollToView(
  scrollViewParent: ScrollView,
  view: View
) {
  val childOffset = Point()
  getDeepChildOffset(scrollViewParent, view.parent, view, childOffset)
  val rHeight = scrollViewParent.height
  scrollViewParent.scrollBy(0, 1)
  ObjectAnimator.ofInt(scrollViewParent, "scrollY", childOffset.y - (rHeight / 3))
    .setDuration(SPOTLIGHT_SCROLL_DURATION)
    .start()
}

private fun getDeepChildOffset(
  mainParent: ViewGroup,
  parent: ViewParent,
  child: View,
  accumulatedOffset: Point
) {
  val parentGroup = parent as ViewGroup
  accumulatedOffset.x += child.left
  accumulatedOffset.y += child.top
  if (parentGroup == mainParent) {
    return
  }
  getDeepChildOffset(mainParent, parentGroup.parent, parentGroup, accumulatedOffset)
}
