package pl.ola.extrashoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ReminderService extends Service {
    private static final int NOTIFICATION = 2;

    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("blabla", "onCreate: ");
        Notification myNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Warning")
                .setContentText("Remember to buy" + "item")
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent myIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        myNotification.contentIntent = contentIntent;
        notificationManager.notify(NOTIFICATION, myNotification);


    }
}
