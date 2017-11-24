package pl.ola.extrashoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aleksandra Kusiak on 08.11.2017.
 */

public class DoneListArrayAdapter extends RecyclerView.Adapter<DoneListArrayAdapter.ViewHolder> {

    DataManager dataManager;
    OnItemListener onItemListener;
    private Context context;
    private List<Item> doneItems;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        CheckBox doneItemCheckbox;

        public ViewHolder(View doneItemView) {
            super(doneItemView);
            itemNameTextView = (TextView) doneItemView.findViewById(R.id.deleted_item_name);
            doneItemCheckbox = (CheckBox) doneItemView.findViewById(R.id.done_item_checkbox);
        }
    }

    public DoneListArrayAdapter(Context context, List<Item> doneItems, OnItemListener listener){
        this.context = context;
        this.doneItems = doneItems;
        onItemListener = listener;
        dataManager = DataManager.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View doneItemView = inflater.inflate(R.layout.single_done_item_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(doneItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item doneItem = doneItems.get(position);
        holder.itemNameTextView.setText(doneItem.itemName);
        holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        // check box
        holder.doneItemCheckbox.setOnCheckedChangeListener(null);
        holder.doneItemCheckbox.setChecked(false);
        holder.doneItemCheckbox.setTag(position);
        holder.doneItemCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton myCheckbox, boolean b) {

                doneItem.isItemDone = false;
                dataManager.updateItem(doneItem);
                if (onItemListener != null) {
                    onItemListener.onItemDeleted(doneItem.id);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return doneItems.size();
    }
}
