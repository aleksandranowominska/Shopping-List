package pl.ola.extrashoppinglist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String ITEM_POSITION = "ITEM_POSITION";

    EditText itemNameEditText;
    EditText itemDescriptionEditText;
    TextView itemReminderDateTextView;
    TextView itemReminderHourTextView;
    DataManager dataManager;
    Item item;
    int position;
    Calendar reminderCalendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        itemNameEditText = (EditText) findViewById(R.id.item_name_to_edit);
        itemDescriptionEditText = (EditText) findViewById(R.id.item_description);
        itemReminderDateTextView = (TextView) findViewById(R.id.item_reminder_date);
        itemReminderHourTextView = (TextView) findViewById(R.id.item_reminder_hour);
        Intent intent = getIntent();
        dataManager = DataManager.getInstance(this);
        position = intent.getIntExtra(ITEM_POSITION, 0);
        item = dataManager.getItemFromPosition(position);
        reminderCalendar = Calendar.getInstance();
        if (item.itemReminderDate != null){
            reminderCalendar.setTime(item.itemReminderDate);
        }



        updateUI();
        setupListeners();
    }

    private void updateUI(){
        itemNameEditText.setText(item.itemName);
        itemDescriptionEditText.setText(item.itemDescription);
        if (item.itemReminderDate != null){
            SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
            String itemReminderDateString = dateFormat.format(item.itemReminderDate);
            itemReminderDateTextView.setText(itemReminderDateString);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String itemReminderHourString = timeFormat.format(item.itemReminderDate);
            itemReminderHourTextView.setText(itemReminderHourString);
        }

        if (item.itemReminderDate != null){
            itemReminderHourTextView.setVisibility(View.VISIBLE);
        }
        else {
            itemReminderHourTextView.setVisibility(View.GONE);
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
        saveReminderHour();
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
                int startYear = reminderCalendar.get(Calendar.YEAR);
                int startMonth = reminderCalendar.get(Calendar.MONTH);
                int startDay = reminderCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemDetailsActivity.this, ItemDetailsActivity.this, startYear, startMonth, startDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        reminderCalendar.set(year, month, dayOfMonth);
        item.itemReminderDate = reminderCalendar.getTime();
        dataManager.updateItem(position, item);
        updateUI();

    }

    private void saveReminderHour(){
        itemReminderHourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int startHour = reminderCalendar.get(Calendar.HOUR_OF_DAY);
                int startMinute = reminderCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ItemDetailsActivity.this, ItemDetailsActivity.this, startHour, startMinute, true);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        reminderCalendar.set(Calendar.HOUR_OF_DAY, hour);
        reminderCalendar.set(Calendar.MINUTE, minute);
        item.itemReminderDate = reminderCalendar.getTime();
        dataManager.updateItem(position, item);
        updateUI();
    }
}
