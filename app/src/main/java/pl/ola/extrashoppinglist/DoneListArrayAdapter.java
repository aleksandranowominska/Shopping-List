package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
 * Created by Aleksandra Kusiak on 08.11.2017.
 */

public class DoneListArrayAdapter extends ArrayAdapter {

    public DoneListArrayAdapter(@NonNull Context context, @NonNull List<DeletedItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_deleted_item_cell, null);
        }


        final DeletedItem deletedItem = (DeletedItem) getItem(position);
        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.deleted_item_name);
        CheckBox deleteItemCheckbox = (CheckBox) convertView.findViewById(R.id.deleted_item_checkbox);


        itemNameTextView.setText(deletedItem.itemName);
        itemNameTextView.setPaintFlags(itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // check box
        deleteItemCheckbox.setOnCheckedChangeListener(null);
        deleteItemCheckbox.setChecked(false);
        deleteItemCheckbox.setTag(position);
        deleteItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton myCheckbox, boolean b) {
                DataManager dataManager = DataManager.getInstance(getContext());
                dataManager.revealDeletedItem(deletedItem.itemId);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


}
