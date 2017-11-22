package pl.ola.extrashoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;

/**
 * Created by Aleksandra Kusiak on 28.09.2017.
 */

public class DataManager {
    private static DataManager instance;
    Box itemBox;

    public static DataManager getInstance(Context context){
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    private DataManager(Context context) {
        itemBox = ((MyApplication) context.getApplicationContext()).getBoxStore().boxFor(Item.class);
    }


    public List<Item> getItems() {
        return itemBox.query().equal(Item_.isItemDone, false).build().find();
    }

    public List<Item> getDoneItems(){
        return itemBox.query().equal(Item_.isItemDone, true).build().find();
    }

    public void updateItem(Item item){
       itemBox.put(item);
    }

    public long getItemId(Item item){
        return itemBox.getId(item);
    }

    public Item getItemById(long itemId) {
        return (Item) itemBox.get(itemId);
    }

}
