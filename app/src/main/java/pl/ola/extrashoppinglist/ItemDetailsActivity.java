package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ItemDetailsActivity extends AppCompatActivity {
    public static final String ITEM_POSITION = "ITEM_POSITION";

    EditText itemNameEditText;
    DataManager dataManager;
    Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        itemNameEditText = (EditText) findViewById(R.id.item_name_to_edit);
        dataManager = DataManager.getInstance(this);
        Intent intent = getIntent();
        int position = intent.getIntExtra(ITEM_POSITION, 0);
        item = dataManager.getItemFromPosition(position);

        itemNameEditText.setText(item.itemName);
    }
}
