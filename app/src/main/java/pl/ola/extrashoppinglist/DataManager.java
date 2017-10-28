package pl.ola.extrashoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandra Kusiak on 28.09.2017.
 */

public class DataManager {
    private static DataManager instance;
    private SharedPreferences sharedPreferences;
    private static List<Item> items = new ArrayList<Item>();
    Gson gson;

    public static DataManager getInstance(Context context){
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }
        return instance;
    }

    private DataManager(Context context) {
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        items = readDataFromSharedPreferences();

    }

    public List<Item> getItems() {
        return items;
    }

    public void updateItem(int position, Item item){
        items.set(position, item);
        writeToSharedPreferences();
    }

    public Item getItemFromPosition(int position){
        return items.get(position);
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
        String itemsToSave = gson.toJson(items);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("itemsList", itemsToSave);
        editor.apply();
    }

    private List<Item> readDataFromSharedPreferences(){
        String itemsToRead = sharedPreferences.getString("itemsList", null);
        if (itemsToRead == null){
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Item>>() {}.getType();
        List<Item> savedItems = gson.fromJson(itemsToRead, type);
        return savedItems;
    }

}
