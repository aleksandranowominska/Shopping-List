package pl.ola.extrashoppinglist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
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
    public int onStartCommand(Intent intent, int flags, int startId) {

        String itemName = (String) intent.getExtras().get("itemName");
        String itemDescription = (String) intent.getExtras().get("itemDescription");

        Notification myNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Item to buy: " + itemName)
                .setContentText(itemDescription)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent myIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        myNotification.contentIntent = contentIntent;
        notificationManager.notify(NOTIFICATION, myNotification);

        return super.onStartCommand(intent, flags, startId);
    }
}
