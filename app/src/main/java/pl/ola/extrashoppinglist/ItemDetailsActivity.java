package pl.ola.extrashoppinglist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String ITEM_ID = "ITEM_ID";
    private static final String TAG = "olka";

    EditText itemNameEditText;
    EditText itemDescriptionEditText;
    TextView itemReminderDateTextView;
    TextView itemReminderHourTextView;
    DataManager dataManager;
    Item item;
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
        long itemId = intent.getLongExtra(ITEM_ID, 0);
        item = dataManager.getItemById(itemId);
        Log.d(TAG, "activity onCreate: id "+itemId +" item" + item);
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
        item.itemName = newItemName;

        String newItemDescription = itemDescriptionEditText.getText().toString();
        item.itemDescription = newItemDescription;

        dataManager.updateItem(item);

    }

    private void setupListeners(){
        saveEditedItemName();
        saveEditedItemDescription();
        saveReminderDate();
        saveReminderHour();
    }

    private void saveEditedItemName(){

        itemNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveItemDetails();
            }
        });

    }

    private void saveEditedItemDescription(){

        itemDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveItemDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        dataManager.updateItem(item);
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
        reminderCalendar.set(Calendar.SECOND, 0);
        item.itemReminderDate = reminderCalendar.getTime();
        dataManager.updateItem(item);
        updateUI();
        setAlarm(item.itemReminderDate);
    }


    public void setAlarm(Date date){
        Intent myIntent = new Intent(this , ReminderService.class);
        long itemId = dataManager.getItemId(item);
        myIntent.putExtra(ITEM_ID, itemId);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, (int) itemId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        Toast.makeText(this, "Alarm set on "+ dateFormat.format(date)+" for "+ timeFormat.format(date), Toast.LENGTH_SHORT).show();
        Log.d("olka", "Alarm set on "+ dateFormat.format(date)+" for "+ timeFormat.format(date)+ " for ID: " + itemId);
    }
}
