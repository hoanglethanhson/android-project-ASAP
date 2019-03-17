package com.example.assignment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.assignment.DBHandler.DatabaseHandler;
import com.example.assignment.Entity.LongTermNote;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AddShortOfLongPlanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;
    Bundle bundle;
    private static final int NOT_COMPLETE = 0;
    private static final int NOT_DELETED = -1;
    private String longPlanTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_short_of_long_plan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add new Short Term Plan");
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        longPlanTitle = bundle.getString("longplan");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void onDeadTimeClick(View view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        TimePickerDialog dialog = new TimePickerDialog(this, this,
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    public void onDeadlineClick(View view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        DatePickerDialog dialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        //update date text view
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("/").append(month + 1).append("/").append(day);
        TextView deadline = findViewById(R.id.editTextDeadline);
        deadline.setText(builder);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.min = minute;
        TextView deadTime = findViewById(R.id.editTextTime);
        //update textView
        StringBuilder builder = new StringBuilder();
        builder.append(hour).append(":").append(min);
        deadTime.setText(builder);
    }

    public void onCreateClick(View view) {
        ShortTermNote note = new ShortTermNote();
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        EditText title = findViewById(R.id.editTextTitle);
        EditText deadLine = findViewById(R.id.editTextDeadline);
        EditText time = findViewById(R.id.editTextTime);
        EditText content = findViewById(R.id.editTextContent);

        note.setTitle(title.getText().toString());

        String date = deadLine.getText().toString() + " " + time.getText().toString();
        note.setDeadline(date);

        note.setContent(content.getText().toString());
        note.setIsComplete(NOT_COMPLETE);
        note.setIsDeleted(NOT_DELETED);
        LongTermNote longNote = databaseHandler.findLongTermNote(longPlanTitle);
        note.setLongNoteId(longNote.getId());

        long result = databaseHandler.addShortTermNoteOfLongTerm(note);
        if (result != -1) {
            Toast.makeText(this.getApplicationContext(), "Create note successfully!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddShortOfLongPlanActivity.this,ViewLongPlanDetail.class);
            intent.putExtra("longplan",longPlanTitle);
            startActivity(intent);
        } else {
            Toast.makeText(this.getApplicationContext(), "Create note failed!", Toast.LENGTH_LONG).show();

        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
