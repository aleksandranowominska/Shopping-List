package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Olka";

    DataManager dataManager;
    SharedPreferencesManager sharedPreferencesManager;
    ListView itemsListView;
    ListView deletedItemsListView;
    ShoppingListArrayAdapter shoppingListArrayAdapter;
    DoneListArrayAdapter doneListArrayAdapter;
    TextView deletedItemsTextView;
    List<Item> shoppingList = new ArrayList<Item>();
    List<Item> doneList = new ArrayList<Item>();
    String sortingStyle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sort_up:
                sharedPreferencesManager.setSortingStyle(SharedPreferencesManager.SORTED_ASCENDING);
                updateData();
                return true;
            case R.id.sort_down:
                sharedPreferencesManager.setSortingStyle(SharedPreferencesManager.SORTED_DESCENDING);
                updateData();
                return true;
            case R.id.sort_with_stars:
                sharedPreferencesManager.setSortingStyle(SharedPreferencesManager.SORTED_WITH_PRIORITY);
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = DataManager.getInstance(this);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.shopping_list);
        deletedItemsListView = (ListView) findViewById(R.id.deleted_list);

        shoppingListArrayAdapter = new ShoppingListArrayAdapter(this, shoppingList, new OnItemDeletedListener() {
            @Override
            public void onItemDeleted(long itemId) {
                updateData();
            }
        });
        itemsListView.setAdapter(shoppingListArrayAdapter);
        doneListArrayAdapter = new DoneListArrayAdapter(this, doneList, new OnItemDeletedListener() {
            @Override
            public void onItemDeleted(long itemId) {
                updateData();
            }
        });
        deletedItemsListView.setAdapter(doneListArrayAdapter);
        deletedItemsListView.setVisibility(View.GONE);

        deletedItemsTextView = (TextView) findViewById(R.id.deleted_items_text_view);
        deletedItemsTextView.setClickable(true);
        deletedItemsTextView.setOnClickListener(this);
        updateData();

        // Enter click
        final EditText addItemTextView = (EditText) findViewById(R.id.add_item);

        addItemTextView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String typedItemNameToAdd = addItemTextView.getText().toString();
                    Item item = new Item(typedItemNameToAdd);
                    dataManager.updateItem(item);
                    updateData();
                    addItemTextView.setText("");

                    return true;
                }
                return false;
            }
        });

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                Item item = shoppingListArrayAdapter.getItem(i);
                long itemId = dataManager.getItemId(item);
                intent.putExtra(ITEM_ID, itemId);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void updateData() {

        sortingStyle = sharedPreferencesManager.getSortingStyle();
        shoppingList.clear();
        doneList.clear();

        if (sortingStyle.equals(SharedPreferencesManager.SORTED_ASCENDING)){
            shoppingList.addAll(dataManager.getAscendingItems());
        }
        else if (sortingStyle.equals(SharedPreferencesManager.SORTED_DESCENDING)){
            shoppingList.addAll(dataManager.getDescendingItems());
        }
        else if (sortingStyle.equals(SharedPreferencesManager.SORTED_WITH_PRIORITY)){
            shoppingList.addAll(dataManager.getPrioritySortedItems());
        }
        else {
            shoppingList.addAll(dataManager.getItems());
        }
        doneList.addAll(dataManager.getDoneItems());
        shoppingListArrayAdapter.notifyDataSetChanged();
        doneListArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleted_items_text_view:
                if (deletedItemsListView.getVisibility() == View.VISIBLE) {
                    deletedItemsListView.setVisibility(View.GONE);
                } else {
                    deletedItemsListView.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }
}