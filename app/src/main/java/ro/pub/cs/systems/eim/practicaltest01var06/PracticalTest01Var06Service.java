package ro.pub.cs.systems.eim.practicaltest01var06;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class PracticalTest01Var06Service extends Service {
    private static final String TAG = "PracticalTest01Var06Service";
    private ProcessingThread processingThread = null;

    public PracticalTest01Var06Service() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SERVICE onCreate() method was invoked");

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SERVICE onStartCommand() method was invoked");
        int score = intent.getIntExtra("score", -3);
        processingThread = new ProcessingThread(this, score);
        processingThread.start();
        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() method was invoked");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() method was invoked");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind() method was invoked");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() method was invoked");
        super.onDestroy();
    }
}