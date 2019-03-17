package com.example.assignment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.assignment.DBHandler.DatabaseHandler;
import com.example.assignment.Entity.ShortTermNote;

import java.util.Calendar;
import java.util.TimeZone;

public class ViewShortPlanDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String dateTime;

    Bundle bundle;

    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;

    private int id;
    private int receiveRequestCode;

    //relationship constants
    private static final int NOT_BELONG = -1;
    private static final int NOT_COMPLETE = 0;
    private static final int NOT_DELETED = -1;

    private static final int COMPLETE = 1;
    private static final int DELETED = 1;

    private static final int MAIN_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_short_plan_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String id = bundle.getString("id");
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        ShortTermNote note = databaseHandler.findShortById(Integer.parseInt(id));
        this.id = note.getId();

        TextView title = findViewById(R.id.textViewShortTitle);
        TextView deadline = findViewById(R.id.textView8);
        TextView deadTime = findViewById(R.id.textViewShortTime);
        TextView content = findViewById(R.id.textViewShortPlanContent);
        CheckBox checkBox = findViewById(R.id.chkComplete);
        //checkBox.setEnabled(false);


        dateTime = note.getDeadline();
        String[] words = dateTime.split("\\s");
        title.setText(note.getTitle());
        setTitle(title.getText().toString());
        try {
            deadline.setText(words[0]);
            deadTime.setText(words[1]);
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(this, "Wrong number format", Toast.LENGTH_LONG).show();
            deadline.setText("Wrong format");
            deadTime.setText("Wrong format");
        }

        content.setText(note.getContent());
        if (note.getIsComplete() == 1) {
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        //setResult(Activity.RESULT_OK);
        super.onBackPressed();


    }

    public void onTitleClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input);
        //Set up the button
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView title = findViewById(R.id.textViewShortTitle);
                title.setText(input.getText().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void onDeadlineClick(View view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        DatePickerDialog dialog = new DatePickerDialog(this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    public void onDeadTimeClick(View view) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        TimePickerDialog dialog = new TimePickerDialog(this, this,
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.min = minute;
        TextView deadTime = findViewById(R.id.textViewShortTime);
        //update textView
        StringBuilder builder = new StringBuilder();
        builder.append(hour).append(":").append(min);
        deadTime.setText(builder);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        //update date text view
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("/").append(month + 1).append("/").append(day);
        TextView deadline = findViewById(R.id.textView8);
        deadline.setText(builder);
    }

    public void onSaveModClick(View view) {
        TextView title = findViewById(R.id.textViewShortTitle);
        TextView deadline = findViewById(R.id.textView8);
        TextView deadTime = findViewById(R.id.textViewShortTime);
        TextView content = findViewById(R.id.textViewShortPlanContent);
        CheckBox checkBox = findViewById(R.id.chkComplete);

        ShortTermNote note = new ShortTermNote();
        note.setTitle(title.getText().toString());
        note.setContent(content.getText().toString());
        note.setIsDeleted(NOT_DELETED);
        note.setDeadline(deadline.getText().toString().trim() + " " + deadTime.getText().toString().trim());
        if (checkBox.isChecked()) {
            note.setIsComplete(COMPLETE);
        }

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        int result = databaseHandler.updateShortNote(this.id, note);

        //notice if succeeded
        if (result != 0) {

            Toast.makeText(this, "Update successfully", Toast.LENGTH_LONG).show();
            //restart the activity
            recreate();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
        }
    }


    public void onDeleteShortClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");

        // Set up the input
        final TextView message = new TextView(this);
        message.setText("Do you want to move this note to Recycle Bin?");
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(message);
        //Set up the button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHandler databaseHandler = new DatabaseHandler(ViewShortPlanDetail.this);
                databaseHandler.moveTrashShort(id);

                String previousActivity = bundle.getString("from_activity");
                if (previousActivity.equalsIgnoreCase("viewlongplandetail")) {
                    backToLongPlanDetail();
                }
                else if(previousActivity.equalsIgnoreCase("viewshortplanactivity")){
                    backToShortPlanActivity();
                }
                else {
                    backToMain();
                }
                Toast.makeText(getApplicationContext(), "Move to Recycle Bin successfully", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void backToShortPlanActivity(){

        Intent intent = new Intent(ViewShortPlanDetail.this, ViewShortPlansActivity.class);

        startActivity(intent);
    }

    private void backToLongPlanDetail() {
        String longPlanTitle = bundle.getString("longplan");
        Intent intent = new Intent(ViewShortPlanDetail.this, ViewLongPlanDetail.class);
        intent.putExtra("longplan", longPlanTitle);
        startActivity(intent);
    }

    //go back to MainActivity
    private void backToMain() {

        Intent intent = new Intent(ViewShortPlanDetail.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        receiveRequestCode = requestCode;
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        if (receiveRequestCode == MAIN_REQUEST_CODE) {
            backToMain();
        }
        super.finish();
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
