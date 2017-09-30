package pl.ola.extrashoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailsActivity extends AppCompatActivity {
    public static final String ITEM_POSITION = "ITEM_POSITION";

    EditText itemNameEditText;
    EditText itemDescriptionEditText;
    DataManager dataManager;
    Item item;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        itemNameEditText = (EditText) findViewById(R.id.item_name_to_edit);
        itemDescriptionEditText = (EditText) findViewById(R.id.item_description);

        dataManager = DataManager.getInstance(this);
        Intent intent = getIntent();
        position = intent.getIntExtra(ITEM_POSITION, 0);
        item = dataManager.getItemFromPosition(position);

        itemNameEditText.setText(item.itemName);
        itemDescriptionEditText.setText(item.itemDescription);
        saveEditedItem();

    }

    private void saveEditedItem(){
        saveEditedItemName();
        saveEditedItemDescription();
    }

    private void saveEditedItemName(){
        itemNameEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newItemName = itemNameEditText.getText().toString();
                    itemNameEditText.setText(newItemName);
                    item.itemName = newItemName;

                    dataManager.updateItem(position, item);

                    return true;
                }
                return false;
            }
        });
    }

    private void saveEditedItemDescription(){
        itemDescriptionEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newItemDescription = itemDescriptionEditText.getText().toString();
                    itemDescriptionEditText.setText(newItemDescription);
                    item.itemDescription = newItemDescription;

                    dataManager.updateItem(position, item);

                    return true;
                }
                return false;
            }
        });

    }
}
