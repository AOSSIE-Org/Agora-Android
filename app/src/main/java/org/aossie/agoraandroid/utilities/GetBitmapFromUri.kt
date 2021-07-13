@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SameParameterValue")

package org.aossie.agoraandroid.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build.VERSION
import org.aossie.agoraandroid.BuildConfig
import java.io.IOException
import kotlin.math.roundToInt

object GetBitmapFromUri {

  @Throws(IOException::class)
  fun handleSamplingAndRotationBitmap(
    context: Context,
    selectedImage: Uri
  ): Bitmap? {
    val MAX_HEIGHT = 1024
    val MAX_WIDTH = 1024

    // First decode with inJustDecodeBounds=true to check dimensions
    val options = Options()
    options.inJustDecodeBounds = true
    var imageStream = context.contentResolver
      .openInputStream(selectedImage)
    BitmapFactory.decodeStream(imageStream, null, options)
    imageStream?.close()

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    imageStream = context.contentResolver
      .openInputStream(selectedImage)
    var img = BitmapFactory.decodeStream(imageStream, null, options)
    img = rotateImageIfRequired(context, img, selectedImage)
    return img
  }

  private fun calculateInSampleSize(
    options: Options,
    reqWidth: Int,
    reqHeight: Int
  ): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and width
      val heightRatio =
        (height.toFloat() / reqHeight.toFloat()).roundToInt()
      val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

      // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
      // with both dimensions larger than or equal to the requested height and width.
      inSampleSize = heightRatio.coerceAtMost(widthRatio)

      // This offers some additional logic in case the image has a strange
      // aspect ratio. For example, a panorama may have a much larger
      // width than height. In these cases the total pixels might still
      // end up being too large to fit comfortably in memory, so we should
      // be more aggressive with sample down the image (=larger inSampleSize).
      val totalPixels = width * height.toFloat()

      // Anything more than 2x the requested pixels we'll sample down further
      val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
      while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++
      }
    }
    return inSampleSize
  }

  @Throws(IOException::class) private fun rotateImageIfRequired(
    context: Context,
    img: Bitmap?,
    selectedImage: Uri
  ): Bitmap? {
    val input = context.contentResolver
      .openInputStream(selectedImage)
    val ei = if (VERSION.SDK_INT > 23) {
      if (BuildConfig.DEBUG && input == null) {
        error("Assertion failed")
      }
      input?.let { ExifInterface(it) }
    } else selectedImage.path?.let {
      ExifInterface(it)
    }
    val orientation = ei?.getAttributeInt(
      ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
    )
    return when (orientation) {
      ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
        img, 90
      )
      ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
        img, 180
      )
      ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
        img, 270
      )
      else -> img
    }
  }

  private fun rotateImage(
    img: Bitmap?,
    degree: Int
  ): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    val rotatedImg =
      Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
  }
}
