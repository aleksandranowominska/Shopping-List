package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShoppingListArrayAdapter extends RecyclerView.Adapter<ShoppingListArrayAdapter.ViewHolder> {

    DataManager dataManager;
    OnItemListener onItemListener;
    private Context context;
    private List<Item> items;
    final MediaPlayer mediaPlayer;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        CheckBox doneItemCheckbox;
        ImageView isItemStarred;

        public ViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = (TextView) itemView.findViewById(R.id.item_name);
            doneItemCheckbox = (CheckBox) itemView.findViewById(R.id.item_checkbox);
            isItemStarred = (ImageView) itemView.findViewById(R.id.item_star);
        }
    }


    public ShoppingListArrayAdapter(Context context, List<Item> items, OnItemListener listener){
        this.context = context;
        this.items = items;
        onItemListener = listener;
        mediaPlayer = MediaPlayer.create(context, R.raw.notification_sound);
        dataManager = DataManager.getInstance(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.single_item_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.itemNameTextView.setText(item.itemName);
        holder.itemNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener != null) {
                    onItemListener.onItemClick(item.id);
                }
            }
        });

        setStarIcon(item, holder.isItemStarred);


        // check box
        holder.doneItemCheckbox.setTag(position);
        holder.doneItemCheckbox.setOnCheckedChangeListener(null);
        holder.doneItemCheckbox.setChecked(false);
        holder.doneItemCheckbox.setTag(position);
        holder.doneItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton myCheckbox, boolean b) {
                int position = (int) myCheckbox.getTag();

                cancelAlarm(item.id);
                mediaPlayer.start();
                item.isItemDone = true;
                dataManager.updateItem(item);
                if (onItemListener != null) {
                    onItemListener.onItemDeleted(item.id);
                }
            }
        });

        holder.isItemStarred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStarIconWhenClicked(item, holder.isItemStarred);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setStarIcon(Item item, ImageView isItemStarred) {
        if (item.isItemStarred == true) {
            isItemStarred.setImageResource(R.drawable.star_on);
        } else {
            isItemStarred.setImageResource(R.drawable.star_off);
        }
    }

    public void setStarIconWhenClicked(Item item, ImageView isItemStarred) {
        if (item.isItemStarred == false) {
            isItemStarred.setImageResource(R.drawable.star_on);
            item.isItemStarred = true;
            dataManager.updateItem(item);
        } else {
            isItemStarred.setImageResource(R.drawable.star_off);
            item.isItemStarred = false;
            dataManager.updateItem(item);
        }
    }

    public void cancelAlarm(long itemId) {
        Intent myIntent = new Intent(context, ReminderService.class);
        myIntent.putExtra(ITEM_ID, itemId);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, (int) itemId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
