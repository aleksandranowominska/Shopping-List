package pl.ola.extrashoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Olka";


    ListView itemsListView;
    ShoppingListArrayAdapter adapter;
    public static final String MY_PREFS_NAME = "ItemsToBuy";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    public static List<Item> items = new ArrayList<Item>();
//    void setupTestData() {
//        items.add(new Item("apple"));
//        items.add(new Item("orange"));
//        items.add(new Item("flour"));
//        items.add(new Item("mango"));
//        items.add(new Item("banana"));
//    }

    public void writeToSharedPreferences(){
        editor.clear();
        editor.commit();
        editor.putInt("size", items.size());
        for (int i = 0; i < (items.size()-1); i++ ) {
            editor.putString(i + "", items.get(i).itemName + "");
        }
        editor.commit();
    }

    public List<Item> readDataFromSharedPreferences(){
        int size = sharedpreferences.getInt("size", 0);
        List<Item> savedItems = new ArrayList<Item>();
        for (int i=0; i<size; i++){
            String s = sharedpreferences.getString(i+"", null);
            savedItems.add(new Item(s));
        }
        return savedItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        editor = sharedpreferences.edit();

        items = readDataFromSharedPreferences();

//        editor.putString("0", "apple");
//        editor.putString("1", "orange");
//        editor.putString("2", "flour");
//        editor.putString("3", "mango");
//        editor.putString("4", "banana");

        Boolean commitResult = editor.commit();

        Log.d(TAG, "onCreate: result: "+commitResult); //true or false

        String restoredText = sharedpreferences.getString("0", null);
        Log.d(TAG, "onCreate: read result:"+restoredText);

//        items.add(new Item(sharedpreferences.getString("0", null)));
//        items.add(new Item(sharedpreferences.getString("1", null)));
//        items.add(new Item(sharedpreferences.getString("2", null)));
//        items.add(new Item(sharedpreferences.getString("3", null)));
//        items.add(new Item(sharedpreferences.getString("4", null)));

        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.shopping_list);
        adapter = new ShoppingListArrayAdapter(this, R.layout.single_item_cell, items);
        itemsListView.setAdapter(adapter);
//        setupTestData();
        // Enter click
        final EditText addItem = (EditText) findViewById(R.id.add_item);

        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String itemToAdd = addItem.getText().toString();
                    Toast.makeText(MainActivity.this, addItem.getText(), Toast.LENGTH_LONG).show();
                    items.add(new Item(itemToAdd));
                    adapter.notifyDataSetChanged();
                    editor.putString(items.size()-1 +"", itemToAdd);
                    addItem.setText("");
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        writeToSharedPreferences();
    }
}