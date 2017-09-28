package pl.ola.extrashoppinglist;

import android.content.Context;
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

/**
 * Created by Aleksandra Kusiak on 27.09.2017.
 */

public class ShoppingListArrayAdapter extends ArrayAdapter<Item> {
    public ShoppingListArrayAdapter(@NonNull Context context, @IdRes int textViewResourceId, @NonNull List<Item> objects) {
        super(context, textViewResourceId, objects);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_item_cell, null);
        }

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
//                Log.d("olka", "onCheckedChanged: " + MainActivity.items.get(position).itemName + " " + position + " mycheckbox tag " + myCheckbox.getTag());
                Toast.makeText(getContext(), MainActivity.items.get(position).itemName + " has been removed", Toast.LENGTH_SHORT).show();
                MainActivity.items.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
