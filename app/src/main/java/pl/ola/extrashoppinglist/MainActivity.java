package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import static pl.ola.extrashoppinglist.ItemDetailsActivity.ITEM_POSITION;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Olka";


    DataManager dataManager;
    ListView itemsListView;
    ShoppingListArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = DataManager.getInstance(this);

        setContentView(R.layout.activity_main);
        itemsListView = (ListView) findViewById(R.id.shopping_list);
        adapter = new ShoppingListArrayAdapter(this, dataManager.getItems());
        itemsListView.setAdapter(adapter);

        // Enter click
        final EditText addItem = (EditText) findViewById(R.id.add_item);

        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String itemToAdd = addItem.getText().toString();
                    Toast.makeText(MainActivity.this, addItem.getText(), Toast.LENGTH_LONG).show();
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
                intent.putExtra(ITEM_POSITION, i);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}