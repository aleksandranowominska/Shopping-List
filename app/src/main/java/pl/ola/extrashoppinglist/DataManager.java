package pl.ola.extrashoppinglist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandra Kusiak on 28.09.2017.
 */

public class DataManager {
    private static DataManager instance;
    private SharedPreferences sharedPreferences;
    private static List<Item> items = new ArrayList<Item>();

    public static DataManager getInstance(Context context){
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }
        return instance;
    }

    private DataManager(Context context) {
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        items = readDataFromSharedPreferences();

    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
        writeToSharedPreferences();
    }

    public void removeItem(int position){
        items.remove(position);
        writeToSharedPreferences();
    }

    private void writeToSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("size", items.size());
        for (int i = 0; i < items.size(); i++ ) {
            editor.putString(i + "", items.get(i).itemName + "");
        }
        editor.apply();
    }

    private List<Item> readDataFromSharedPreferences(){
        int size = sharedPreferences.getInt("size", 0);
        List<Item> savedItems = new ArrayList<Item>();
        for (int i=0; i<size; i++){
            String s = sharedPreferences.getString(i+"", "EMPTY ITEM");
            savedItems.add(new Item(s));
        }
        return savedItems;
    }
}
