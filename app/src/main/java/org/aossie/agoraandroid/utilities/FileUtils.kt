package org.aossie.agoraandroid.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.net.Uri
import android.text.TextUtils
import androidx.core.content.FileProvider
import org.apache.poi.util.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

object FileUtils {

  fun getPathFromUri(
    context: Context,
    contentUri: Uri?
  ): String? {
    val fileName = getFileName(contentUri)
    if (!TextUtils.isEmpty(fileName)) {
      val copyFile = File(context.cacheDir.toString() + File.separator + fileName)
      copyToFile(context, contentUri, copyFile)
      return copyFile.absolutePath
    }
    return null
  }

  private fun getFileName(
    uri: Uri?
  ): String? {
    if (uri == null) return null
    var fileName: String? = null
    val path = uri.path
    val cut = path!!.lastIndexOf('/')
    if (cut != -1) {
      fileName = path.substring(cut + 1)
    }
    return fileName
  }

  private fun copyToFile(
    context: Context,
    srcUri: Uri?,
    dstFile: File?
  ) {
    try {
      val inputStream = context.contentResolver.openInputStream(
        srcUri!!
      ) ?: return
      val outputStream: OutputStream = FileOutputStream(dstFile)
      IOUtils.copy(inputStream, outputStream)
      inputStream.close()
      outputStream.close()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun saveBitmap(
    context: Context,
    bitmap: Bitmap
  ): Uri? {
    return try {
      val cachePath = File(context.externalCacheDir, "images")
      cachePath.mkdirs()
      val file = File(cachePath, "election_result.png")
      val stream = FileOutputStream(file)
      bitmap.compress(PNG, 100, stream)
      stream.flush()
      stream.close()
      FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: IOException) {
      null
    }
  }
}
