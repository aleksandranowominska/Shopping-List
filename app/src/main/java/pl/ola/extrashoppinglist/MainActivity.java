package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Olka";


    DataManager dataManager;
    ListView itemsListView;
    ShoppingListArrayAdapter adapter;
    ImageView sortUpButton;
    ImageView sortDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = DataManager.getInstance(this);

        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.shopping_list);
        adapter = new ShoppingListArrayAdapter(this, dataManager.getItems());
        itemsListView.setAdapter(adapter);
        sortUpButton = (ImageView) findViewById(R.id.sort_up);
        sortDownButton = (ImageView) findViewById(R.id.sort_down);
        sortUpButton.setClickable(true);
        sortDownButton.setClickable(true);
        sortUpButton.setOnClickListener(this);
        sortDownButton.setOnClickListener(this);



        // Enter click
        final EditText addItem = (EditText) findViewById(R.id.add_item);

        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String itemToAdd = addItem.getText().toString();
                    dataManager.addItem(new Item(itemToAdd));
                    adapter.notifyDataSetChanged();
                    addItem.setText("");

                    return true;
                }
                return false;
            }
        });

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                Item item = adapter.getItem(i);
                intent.putExtra(ITEM_ID, item.itemId);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }



    public void sortUp(){
        adapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item object1, Item object2) {
                return object1.itemName.compareTo(object2.itemName);
            }
        });
    }
    public void sortDown(){
        adapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item object1, Item object2) {
                return object2.itemName.compareTo(object1.itemName);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sort_up:
                sortUp();
                break;

            case R.id.sort_down:
                sortDown();
                break;

            default:
                break;
        }
    }
}