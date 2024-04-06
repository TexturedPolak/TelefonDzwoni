package pl.texturedpolak.telefonDzwoni;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class CallStateService extends Service {

    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    String Channel_ID = "Podtrzymanie";
    String Channel_Name = "ForeGroundService";
    private static final int NOTIFICATION_ID = 12345;

    private static final int PERMISSION_REQUEST_CODE = 1;

    private void notificationChannel(){

        if (Build.VERSION.SDK_INT>= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(Channel_ID, Channel_Name,
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, Channel_ID)
                .setContentTitle("Dbam o dobry kontakt ;)")
                //.setContentText("TelefonDzwoni")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkPermissionAndInitializeListener();
        notificationChannel();
        //Notification notification = createNotification();
        //startForeground(NOTIFICATION_ID, notification);
        //Notification notification = buildNotification();
        //startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    private void checkPermissionAndInitializeListener() {
        // Check for permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            stopSelf();
        } else {
            initializePhoneStateListener();
        }
    }

    private void initializePhoneStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("CallState", "IDLE");
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        Log.d("CallState", "RINGING");
                        Log.d("CallState",phoneNumber);
                        if (phoneNumber.equals("")){
                            Log.d("CallState","UNMUTE");
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
                        }
                        else if (phoneNumber.equals("")){
                            Log.d("CallState","UNMUTE");
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
                        }
                        else if (phoneNumber.equals("")){
                            Log.d("CallState","UNMUTE");
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
                        }
                        else if (phoneNumber.equals("")){
                            Log.d("CallState","UNMUTE");
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        Log.d("CallState", "OFFHOOK");
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    ;




    @Override
    public void onDestroy() {
        super.onDestroy();
        if (telephonyManager != null && phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
    }

}




