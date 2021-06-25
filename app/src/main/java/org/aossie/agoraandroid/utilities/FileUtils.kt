package org.aossie.agoraandroid.utilities

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore.Audio
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns
import android.provider.MediaStore.Video
import android.provider.OpenableColumns
import android.text.TextUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min

object FileUtils {

  private var contentUri: Uri? = null

  fun getPathFromUri(
    context: Context,
    uri: Uri
  ): String? {
    // check here to is it KITKAT or new version
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex())
          .toTypedArray()
        val type = split[0]
        val fullPath = getPathFromExtSD(split)
        return if (fullPath !== "") {
          fullPath
        } else {
          null
        }
      } else if (isDownloadsDocument(uri)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          var cursor: Cursor? = null
          try {
            cursor = context.getContentResolver()
              .query(uri, arrayOf(MediaColumns.DISPLAY_NAME), null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
              val fileName: String = cursor.getString(0)
              val path: String = Environment.getExternalStorageDirectory()
                .toString()
                .toString() + "/Download/" + fileName
              if (!TextUtils.isEmpty(path)) {
                return path
              }
            }
          } finally {
            cursor?.close()
          }
          val id: String = DocumentsContract.getDocumentId(uri)
          if (!TextUtils.isEmpty(id)) {
            if (id.startsWith("raw:")) {
              return id.replaceFirst("raw:".toRegex(), "")
            }
            val contentUriPrefixesToTry = arrayOf(
              "content://downloads/public_downloads",
              "content://downloads/my_downloads"
            )
            for (contentUriPrefix in contentUriPrefixesToTry) {
              return try {
                val contentUri: Uri = ContentUris.withAppendedId(
                  Uri.parse(contentUriPrefix), java.lang.Long.valueOf(id)
                )
                getDataColumn(context, contentUri, null, null)
              } catch (e: NumberFormatException) {
                //In Android 8 and Android P the id is not a number
                uri.path
                  ?.replaceFirst("^/document/raw:", "")
                  ?.replaceFirst("^raw:", "")
              }
            }
          }
        } else {
          val id = DocumentsContract.getDocumentId(uri)
          if (id.startsWith("raw:")) {
            return id.replaceFirst("raw:".toRegex(), "")
          }
          try {
            contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
          } catch (e: NumberFormatException) {
            e.printStackTrace()
          }
          contentUri?.let {
            return getDataColumn(context, it, null, null)
          }
        }
      } else if (isMediaDocument(uri)) {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":".toRegex())
          .toTypedArray()
        val type = split[0]
        var contentUri: Uri? = null
        if ("image" == type) {
          contentUri = Media.EXTERNAL_CONTENT_URI
        } else if ("video" == type) {
          contentUri = Video.Media.EXTERNAL_CONTENT_URI
        } else if ("audio" == type) {
          contentUri = Audio.Media.EXTERNAL_CONTENT_URI
        }
        selection = "_id=?"
        selectionArgs = arrayOf(split[1])
        return contentUri?.let {
          getDataColumn(
            context, it, selection,
            selectionArgs
          )
        }
      } else if (isGoogleDriveUri(uri)) {
        return getDriveFilePath(uri, context)
      }
    } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
      if (isGooglePhotosUri(uri)) {
        return uri.getLastPathSegment()
      }
      if (isGoogleDriveUri(uri)) {
        return getDriveFilePath(uri, context)
      }
      return if (Build.VERSION.SDK_INT === Build.VERSION_CODES.N) {
        getMediaFilePathForN(uri, context)
      } else {
        getDataColumn(context, uri, null, null)
      }
    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
      return uri.getPath()
    }
    return null
  }

  private fun fileExists(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists()
  }

  private fun getPathFromExtSD(pathData: Array<String>): String {
    val type = pathData[0]
    val relativePath = "/" + pathData[1]
    var fullPath = ""
    if ("primary".equals(type, ignoreCase = true)) {
      fullPath = Environment.getExternalStorageDirectory()
        .toString() + relativePath
      if (fileExists(fullPath)) {
        return fullPath
      }
    }
    fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
    if (fileExists(fullPath)) {
      return fullPath
    }
    fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
    return if (fileExists(fullPath)) {
      fullPath
    } else fullPath
  }

  private fun getDriveFilePath(
    uri: Uri,
    context: Context
  ): String? {
    val returnUri: Uri = uri
    val returnCursor: Cursor =
      context.contentResolver.query(returnUri, null, null, null, null) ?: return null
    val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    val file = File(context.cacheDir, name)
    try {
      val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
      val outputStream = FileOutputStream(file)
      var read = 0
      val maxBufferSize = 1 * 1024 * 1024
      val bytesAvailable: Int? = inputStream?.available()

      //int bufferSize = 1024;
      val bufferSize = bytesAvailable?.let { min(it, maxBufferSize) }
      val buffers = bufferSize?.let { ByteArray(it) }
      while (inputStream?.read(buffers)
          .also {
            if (it != null) {
              read = it
            }
          } != -1
      ) {
        outputStream.write(buffers, 0, read)
      }
      inputStream?.close()
      outputStream.close()
    } catch (e: Exception) {
    }
    return file.path
  }

  private fun getMediaFilePathForN(
    uri: Uri,
    context: Context
  ): String? {
    val returnUri: Uri = uri
    val returnCursor: Cursor = context.contentResolver.query(returnUri, null, null, null, null)
      ?: return null
    val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex: Int = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    val file = File(context.filesDir, name)
    try {
      val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
      val outputStream = FileOutputStream(file)
      var read = 0
      val maxBufferSize = 1 * 1024 * 1024
      val bytesAvailable: Int? = inputStream?.available()

      //int bufferSize = 1024;
      val bufferSize = bytesAvailable?.let { min(it, maxBufferSize) }
      val buffers = bufferSize?.let { ByteArray(it) }
      while (inputStream?.read(buffers)
          .also {
            if (it != null) {
              read = it
            }
          } != -1
      ) {
        outputStream.write(buffers, 0, read)
      }
      inputStream?.close()
      outputStream.close()
    } catch (e: Exception) {
    }
    return file.path
  }

  private fun getDataColumn(
    context: Context,
    uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
  ): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
      cursor = context.contentResolver
        .query(
          uri, projection,
          selection, selectionArgs, null
        )
      if (cursor != null && cursor.moveToFirst()) {
        val index: Int = cursor.getColumnIndexOrThrow(column)
        return cursor.getString(index)
      }
    } finally {
      cursor?.close()
    }
    return null
  }

  private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.getAuthority()
  }

  private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.getAuthority()
  }

  private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
  }

  private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
  }

  private fun isGoogleDriveUri(uri: Uri): Boolean {
    return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
  }
}