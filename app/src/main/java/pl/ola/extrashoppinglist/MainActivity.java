package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Comparator;
import java.util.List;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Olka";


    DataManager dataManager;
    ListView itemsListView;
    ListView deletedItemsListView;
    ShoppingListArrayAdapter shoppingListArrayAdapter;
    DoneListArrayAdapter doneListArrayAdapter;
    TextView deletedItemsTextView;
    List<Item> shoppingList = new ArrayList<Item>();
    List<Item> doneList = new ArrayList<Item>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_list:
                return true;
            case R.id.sort_up:
                sortUp();
//                dataManager.writeItemsToSharedPreferences();
                return true;
            case R.id.sort_down:
                sortDown();
//                dataManager.writeItemsToSharedPreferences();
                return true;
            case R.id.sort_with_stars:
                sortWithStars();
//                dataManager.writeItemsToSharedPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = DataManager.getInstance(this);

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


    public void updateData(){
        shoppingList.clear();
        doneList.clear();
        shoppingList.addAll(dataManager.getItems());
        doneList.addAll(dataManager.getDoneItems());
        shoppingListArrayAdapter.notifyDataSetChanged();
        doneListArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
       updateData();
    }



    public void sortUp(){
        shoppingListArrayAdapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item object1, Item object2) {
                return object1.itemName.compareTo(object2.itemName);
            }
        });
    }
    public void sortDown(){
        shoppingListArrayAdapter.sort(new Comparator<Item>() {
            @Override
            public int compare(Item object1, Item object2) {
                return object2.itemName.compareTo(object1.itemName);
            }
        });
    }

    public void sortWithStars(){
        shoppingListArrayAdapter.sort(new StarredComparator<Item>());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleted_items_text_view:
                if (deletedItemsListView.getVisibility() == View.VISIBLE){
                    deletedItemsListView.setVisibility(View.GONE);
                }
                else {
                    deletedItemsListView.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }
}