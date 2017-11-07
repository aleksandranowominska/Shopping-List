package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;

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

        final Item item = getItem(position);
        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.item_name);
        CheckBox deleteItemCheckbox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
        final ImageView isItemStarred = (ImageView) convertView.findViewById(R.id.item_star);



        itemNameTextView.setText(item.itemName);
        setStarIcon(item, isItemStarred);
        // check box
        deleteItemCheckbox.setOnCheckedChangeListener(null);
        deleteItemCheckbox.setChecked(false);
        deleteItemCheckbox.setTag(position);
        deleteItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton myCheckbox, boolean b) {
                cancelAlarm(item.itemId);
                mediaPlayer.start();
                DataManager dataManager = DataManager.getInstance(getContext());
                dataManager.removeItem(item.itemId);
                notifyDataSetChanged();
            }
        });

        isItemStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarIconWhenClicked(item, isItemStarred);
            }
        });

        return convertView;
    }

    public void setStarIcon(Item item, ImageView isItemStarred) {
        if (item.isItemStarred == true) {
            isItemStarred.setImageResource(R.drawable.star_on);
        } else {
            isItemStarred.setImageResource(R.drawable.star_off);
        }
    }

    public void setStarIconWhenClicked(Item item, ImageView isItemStarred){
        DataManager dataManager = DataManager.getInstance(getContext());
        if (item.isItemStarred == false){
            isItemStarred.setImageResource(R.drawable.star_on);
            item.isItemStarred = true;
            dataManager.updateItem(item);
        }
        else {
            isItemStarred.setImageResource(R.drawable.star_off);
            item.isItemStarred = false;
            dataManager.updateItem(item);
        }
    }

    public void cancelAlarm(int itemId){
        Intent myIntent = new Intent(getContext() , ReminderService.class);
        myIntent.putExtra(ITEM_ID, itemId);
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getContext(), itemId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
