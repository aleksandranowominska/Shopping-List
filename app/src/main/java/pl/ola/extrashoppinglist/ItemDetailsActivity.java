package pl.ola.extrashoppinglist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final String ITEM_POSITION = "ITEM_POSITION";

    EditText itemNameEditText;
    EditText itemDescriptionEditText;
    TextView itemReminderDateTextView;
    DataManager dataManager;
    Item item;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        itemNameEditText = (EditText) findViewById(R.id.item_name_to_edit);
        itemDescriptionEditText = (EditText) findViewById(R.id.item_description);
        itemReminderDateTextView = (TextView) findViewById(R.id.item_reminder_date);

        dataManager = DataManager.getInstance(this);
        Intent intent = getIntent();
        position = intent.getIntExtra(ITEM_POSITION, 0);
        item = dataManager.getItemFromPosition(position);
        updateUI();
        setupListeners();
    }

    private void updateUI(){
        itemNameEditText.setText(item.itemName);
        itemDescriptionEditText.setText(item.itemDescription);
        if (item.itemReminderDate != null){
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
            String itemReminderDateString = simpleDate.format(item.itemReminderDate);
            itemReminderDateTextView.setText(itemReminderDateString);

        }
    }

    public void saveItemDetails(){
        String newItemName = itemNameEditText.getText().toString();
        itemNameEditText.setText(newItemName);
        item.itemName = newItemName;

        String newItemDescription = itemDescriptionEditText.getText().toString();
        itemDescriptionEditText.setText(newItemDescription);
        item.itemDescription = newItemDescription;

        dataManager.updateItem(position, item);

    }

    private void setupListeners(){
        saveEditedItemName();
        saveEditedItemDescription();
        saveReminderDate();
    }

    private void saveEditedItemName(){
        itemNameEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveItemDetails();

                    return true;
                }
                return false;
            }
        });
        itemNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                saveItemDetails();
            }
        });
    }

    private void saveEditedItemDescription(){
        itemDescriptionEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    saveItemDetails();
                    return true;
                }
                return false;
            }
        });
        itemDescriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               saveItemDetails();
            }
        });
    }

    private void saveReminderDate(){
        itemReminderDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int startYear = Calendar.getInstance().get(Calendar.YEAR);
                int startMonth = Calendar.getInstance().get(Calendar.MONTH);
                int startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemDetailsActivity.this, ItemDetailsActivity.this, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date itemReminderDateFromDataPicker = calendar.getTime();
        item.itemReminderDate = itemReminderDateFromDataPicker;
        dataManager.updateItem(position, item);
        updateUI();

    }
}
