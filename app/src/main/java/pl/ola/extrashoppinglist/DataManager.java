package pl.ola.extrashoppinglist;

import android.content.Context;
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
    private static List<DeletedItem> deletedItems = new ArrayList<DeletedItem>();
    private static int lastItemId = 0;
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
        items = readItemsDataFromSharedPreferences();
        deletedItems = readDeletedItemsDataFromSharedPreferences();

    }

    public List<Item> getItems() {
        return items;
    }

    public List<DeletedItem> getDeletedItems(){
        return deletedItems;
    }

    public void updateItem(Item item){
        int position = getItemPositionById(item.itemId);
        if (position != -1) {
            items.set(position, item);
            writeItemsToSharedPreferences();
            writeDeletedItemsToSharedPreferences();
        }
    }

    public int getItemPositionById(int itemId){
        for (int i = 0; i < items.size(); i++){
            if (items.get(i).itemId == itemId){
                return i;
            }
        }
        return  -1;
    }

    public Item getItemById(int itemId){
       for (Item item: items){
           if (item.itemId == itemId){
               return item;
           }
       }
       return null;
    }

    public DeletedItem getDeletedItemById(int itemId){
        for (DeletedItem item: deletedItems){
            if (item.itemId == itemId){
                return item;
            }
        }
        return null;
    }

    public void addItem(Item item) {
        lastItemId = lastItemId + 1;
        item.itemId = lastItemId;
        items.add(item);
        writeItemsToSharedPreferences();
    }

    public void removeItem(int itemId){
        Item itemToRemove = getItemById(itemId);
        DeletedItem deletedItem = new DeletedItem(itemToRemove);
        deletedItems.add(deletedItem);
        items.remove(itemToRemove);
        writeItemsToSharedPreferences();
        writeDeletedItemsToSharedPreferences();
    }

    public void undoDeletedItem(int itemId){
        DeletedItem deletedItem = getDeletedItemById(itemId);
        Item revealedItem = new Item(deletedItem);
        items.add(revealedItem);
        deletedItems.remove(deletedItem);
    }

    public void writeItemsToSharedPreferences(){
        String itemsToSave = gson.toJson(items);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("itemsList", itemsToSave);
        editor.putInt("latsItemId", lastItemId);
        editor.apply();
    }

    private void writeDeletedItemsToSharedPreferences(){
        String deletedItemsToSave = gson.toJson(deletedItems);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deletedItemsList", deletedItemsToSave);
        editor.apply();
    }

    private List<Item> readItemsDataFromSharedPreferences(){
        String itemsToRead = sharedPreferences.getString("itemsList", null);
        lastItemId = sharedPreferences.getInt("lastItemId", 0);
        if (itemsToRead == null){
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Item>>() {}.getType();
        List<Item> savedItems = gson.fromJson(itemsToRead, type);
        return savedItems;
    }

    private List<DeletedItem> readDeletedItemsDataFromSharedPreferences(){
        String itemsToRead = sharedPreferences.getString("deletedItemsList", null);
        if (itemsToRead == null){
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<DeletedItem>>() {}.getType();
        List<DeletedItem> savedDeletedItems = gson.fromJson(itemsToRead, type);
        return savedDeletedItems;
    }

}
