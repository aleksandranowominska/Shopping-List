package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_POSITION;

/**
 * Created by Aleksandra Kusiak on 27.09.2017.
 */

public class ShoppingListArrayAdapter extends ArrayAdapter<Item> {
    public ShoppingListArrayAdapter(@NonNull Context context, @NonNull List<Item> objects) {
        super(context, 0, objects);
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_item_cell, null);
        }

        final MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.notification_sound);

        Item item = getItem(position);
        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.item_name);
        CheckBox deleteItemCheckbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);

        itemNameTextView.setText(item.itemName);
        deleteItemCheckbox.setOnCheckedChangeListener(null);
        deleteItemCheckbox.setChecked(false);
        deleteItemCheckbox.setTag(position);
        deleteItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton myCheckbox, boolean b) {
                cancelAlarm(position);
                mediaPlayer.start();
                DataManager dataManager = DataManager.getInstance(getContext());
                dataManager.removeItem(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    public void cancelAlarm(int position){
        Intent myIntent = new Intent(getContext() , ReminderService.class);
        myIntent.putExtra(ITEM_POSITION, position);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getContext(), position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
