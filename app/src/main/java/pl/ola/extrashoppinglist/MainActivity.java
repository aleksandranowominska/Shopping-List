package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Comparator;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Olka";


    DataManager dataManager;
    ListView itemsListView;
    ListView deletedItemsListView;
    ShoppingListArrayAdapter shoppingListArrayAdapter;
    DoneListArrayAdapter doneListArrayAdapter;
    TextView deletedItemsTextView;

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
                dataManager.writeItemsToSharedPreferences();
                return true;
            case R.id.sort_down:
                sortDown();
                dataManager.writeItemsToSharedPreferences();
                return true;
            case R.id.sort_with_stars:
                sortWithStars();
                dataManager.writeItemsToSharedPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setTint(getResources().getDrawable(R.drawable.ic_sort_by_alpha), getResources().getColor(R.color.lightGray));

        dataManager = DataManager.getInstance(this);

        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.shopping_list);
        deletedItemsListView = (ListView) findViewById(R.id.deleted_list);

        shoppingListArrayAdapter = new ShoppingListArrayAdapter(this, dataManager.getItems());
        itemsListView.setAdapter(shoppingListArrayAdapter);
        doneListArrayAdapter = new DoneListArrayAdapter(this, dataManager.getDeletedItems());
        deletedItemsListView.setAdapter(doneListArrayAdapter);
        deletedItemsListView.setVisibility(View.GONE);

        deletedItemsTextView = (TextView) findViewById(R.id.deleted_items_text_view);
        deletedItemsTextView.setClickable(true);
        deletedItemsTextView.setOnClickListener(this);



        // Enter click
        final EditText addItem = (EditText) findViewById(R.id.add_item);

        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String itemToAdd = addItem.getText().toString();
                    dataManager.addItem(new Item(itemToAdd));
                    shoppingListArrayAdapter.notifyDataSetChanged();
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
                Item item = shoppingListArrayAdapter.getItem(i);
                intent.putExtra(ITEM_ID, item.itemId);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        shoppingListArrayAdapter.notifyDataSetChanged();
        doneListArrayAdapter.notifyDataSetChanged();
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

            case R.id.sort_up:

                break;

            case R.id.sort_down:

                break;

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

//    public static Drawable setTint(Drawable d, int color) {
//        Drawable wrappedDrawable = DrawableCompat.wrap(d);
//        DrawableCompat.setTint(wrappedDrawable, color);
//        return wrappedDrawable;
//    }
}