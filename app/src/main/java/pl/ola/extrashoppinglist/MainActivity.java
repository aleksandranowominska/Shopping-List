package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Olka";

    DataManager dataManager;
    SharedPreferencesManager sharedPreferencesManager;
    RecyclerView itemsRecyclerView;
    RecyclerView doneItemsRecyclerView;
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
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dataManager = DataManager.getInstance(this);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        itemsRecyclerView = (RecyclerView) findViewById(R.id.shopping_list_recycler_view);
        doneItemsRecyclerView = (RecyclerView) findViewById(R.id.done_items_recycler_view);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Item itemToRemove = shoppingList.get(position);
                dataManager.removeItem(itemToRemove);
                updateData();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(itemsRecyclerView);

        LinearLayoutManager shoppingListLinearLayoutManager = new LinearLayoutManager(this);
        shoppingListLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager doneListLinearLayoutManager = new LinearLayoutManager(this);
        doneListLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        itemsRecyclerView.setLayoutManager(shoppingListLinearLayoutManager);
        doneItemsRecyclerView.setLayoutManager(doneListLinearLayoutManager);

        shoppingListArrayAdapter = new ShoppingListArrayAdapter(this, shoppingList, new OnItemListener() {
            @Override
            public void onItemDeleted(long itemId) {
                updateData();
            }

            @Override
            public void onItemClick(long itemId) {
                Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
                intent.putExtra(ITEM_ID, itemId);
                startActivity(intent);
            }
        }){

        };


        itemsRecyclerView.setAdapter(shoppingListArrayAdapter);

        doneListArrayAdapter = new DoneListArrayAdapter(this, doneList, new OnItemListener() {
            @Override
            public void onItemDeleted(long itemId) {
                updateData();
            }

            @Override
            public void onItemClick(long itemId) {

            }
        });
        doneItemsRecyclerView.setAdapter(doneListArrayAdapter);
        doneItemsRecyclerView.setVisibility(View.GONE);

        deletedItemsTextView = (TextView) findViewById(R.id.done_items_text_view);
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
            case R.id.done_items_text_view:
                if (doneItemsRecyclerView.getVisibility() == View.VISIBLE) {
                    doneItemsRecyclerView.setVisibility(View.GONE);
                } else {
                    doneItemsRecyclerView.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }
}