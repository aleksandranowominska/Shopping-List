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

import static android.content.ContentValues.TAG;
import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;

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

        Log.d("olka", "onStartCommand: started service");

        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }

        DataManager dataManager = DataManager.getInstance(this);
        int itemId = intent.getIntExtra(ITEM_ID, 0);
        Item item = dataManager.getItemById(itemId);

        Log.d("olka", "id: "+ itemId + " item: "+ item);



        Notification myNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Item to buy: " + item.itemName)
                .setContentText(item.itemDescription)
                .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent myIntent = new Intent(this, ItemDetailsActivity.class);
        myIntent.putExtra(ITEM_ID, itemId);
        Log.d("olka", "onStartCommand: intent extra: "+myIntent.getIntExtra(ITEM_ID, -1));

        PendingIntent contentIntent = PendingIntent.getActivity(this, itemId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        myNotification.contentIntent = contentIntent;
        notificationManager.notify(itemId, myNotification);

        return super.onStartCommand(intent, flags, startId);
    }
}
