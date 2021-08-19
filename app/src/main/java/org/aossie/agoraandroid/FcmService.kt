package org.aossie.agoraandroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.aossie.agoraandroid.utilities.AppConstants
import timber.log.Timber

class FcmService : FirebaseMessagingService() {
  private lateinit var notificationManager: NotificationManager
  private lateinit var builder: Notification.Builder
  private val CHANNEl_ID = "12"

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    Timber.tag("fcm").d(remoteMessage.toString())
    remoteMessage.notification?.let { notification ->
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEl_ID, AppConstants.NOTIFICATION_DESCRIPTION, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(this, CHANNEl_ID)
          .setContentTitle(notification.title)
          .setContentText(notification.body)
          .setSmallIcon(R.mipmap.ic_launcher_round)
      } else {

        builder = Notification.Builder(this)
          .setContentTitle(notification.title)
          .setContentText(notification.body)
          .setSmallIcon(R.mipmap.ic_launcher_round)
      }
      with(NotificationManagerCompat.from(this)) {
        notify(CHANNEl_ID.toInt(), builder.build())
      }
    }
  }
}
