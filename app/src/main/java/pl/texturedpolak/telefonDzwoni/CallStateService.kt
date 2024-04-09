package pl.texturedpolak.telefonDzwoni

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class CallStateService : Service() {
    private var telephonyManager: TelephonyManager? = null
    private var phoneStateListener: PhoneStateListener? = null
    var Channel_ID = "Podtrzymanie"
    var Channel_Name = "ForeGroundService"
    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                Channel_ID, Channel_Name,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(notificationChannel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, Channel_ID)
            .setContentTitle("Dbam o dobry kontakt ;)") //.setContentText("TelefonDzwoni")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        checkPermissionAndInitializeListener()
        notificationChannel()
        //Notification notification = createNotification();
        //startForeground(NOTIFICATION_ID, notification);
        //Notification notification = buildNotification();
        //startForeground(NOTIFICATION_ID, notification);
        return START_STICKY
    }

    private fun checkPermissionAndInitializeListener() {
        // Check for permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
        } else {
            initializePhoneStateListener()
        }
    }

    private fun initializePhoneStateListener() {
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                super.onCallStateChanged(state, phoneNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_IDLE -> Log.d("CallState", "IDLE")
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Log.d("CallState", "RINGING")
                        Log.d("CallState", phoneNumber)
                        if (phoneNumber == "") {
                            Log.d("CallState", "UNMUTE")
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_RING,
                                maxVolume,
                                AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND
                            )
                        } else if (phoneNumber == "") {
                            Log.d("CallState", "UNMUTE")
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_RING,
                                maxVolume,
                                AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND
                            )
                        } else if (phoneNumber == "") {
                            Log.d("CallState", "UNMUTE")
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_RING,
                                maxVolume,
                                AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND
                            )
                        } else if (phoneNumber == "") {
                            Log.d("CallState", "UNMUTE")
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL)
                            audioManager.setStreamVolume(
                                AudioManager.STREAM_RING,
                                maxVolume,
                                AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND
                            )
                        }
                    }

                    TelephonyManager.CALL_STATE_OFFHOOK -> Log.d("CallState", "OFFHOOK")
                }
            }
        }
        telephonyManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (telephonyManager != null && phoneStateListener != null) {
            telephonyManager!!.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 12345
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
