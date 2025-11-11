package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.sql.Time;
import java.util.Date;

public class ProcessingThread extends Thread {

    public static final String BROADCAST_RECEIVER_EXTRA = "ro.pub.cs.systems.eim.practicaltest01var06.BROADCAST_RECEIVER_EXTRA";
    private Context context;

    int score;

    public ProcessingThread(Context context, int score) {
        this.context = context;
        this.score = score;
    }

    @Override
    public void run() {
        Log.d("[Processing Thread]", "Thread started! PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());

        sleep();
        sendMessage();

        Log.d("[Processing Thread]", "Thread stopped!");
    }

    private void sendMessage() {

        Intent intent = new Intent();
        intent.setAction("ACTION");
        intent.putExtra("score", new Date(System.currentTimeMillis()) + " " + score);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);

        Log.d("Victory", "Sent message with action " + intent.getAction() + " and data " + intent.getStringExtra(BROADCAST_RECEIVER_EXTRA));
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            Log.e("[Processing Thread]", "Thread sleep interrupted: " + interruptedException.getMessage());
        }
    }

}
