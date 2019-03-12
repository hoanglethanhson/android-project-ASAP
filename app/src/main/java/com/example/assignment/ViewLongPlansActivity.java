package com.example.assignment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.assignment.Adapter.ExpandableLongPlanAdapter;
import com.example.assignment.DBHandler.DatabaseHandler;
import com.example.assignment.Entity.LongTermNote;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewLongPlansActivity extends AppCompatActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView listView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_long_plans);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.lvExpLongPlan);
        initData();
        listAdapter = new ExpandableLongPlanAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();


        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        /*LongTermNote dummyLong1 = new LongTermNote("Thi 7.5 Ielts");
        LongTermNote dummyLong2 = new LongTermNote("Take a tour to HCM City");
        LongTermNote dummyLong3 = new LongTermNote("Go to the universe");
        databaseHandler.addLongTermNote(dummyLong1);
        databaseHandler.addLongTermNote(dummyLong2);
        databaseHandler.addLongTermNote(dummyLong3);

        ShortTermNote dummyShort1 = new ShortTermNote("Child1 of Long1", "Do sth1", "2019", 1, 1, 1);
        ShortTermNote dummyShort2 = new ShortTermNote("Child2 of Long1", "Do sth2", "2019", 1, 1, 1);
        ShortTermNote dummyShort3 = new ShortTermNote("Child1 of Long2", "Do sth3", "2019", 1, 1, 2);
        ShortTermNote dummyShort4 = new ShortTermNote("Child1 of Long3", "Do sth4", "2019", 1, 1, 3);
        ShortTermNote dummyShort5 = new ShortTermNote("Child2 of Long3", "Do sth5", "2019", 1, 1, 3);
        ShortTermNote dummyShort6 = new ShortTermNote("Child3 of Long3", "Do sth6", "2019", 1, 1, 3);

        databaseHandler.addShortTermNoteOfLongTerm(dummyShort1);
        databaseHandler.addShortTermNoteOfLongTerm(dummyShort2);
        databaseHandler.addShortTermNoteOfLongTerm(dummyShort3);
        databaseHandler.addShortTermNoteOfLongTerm(dummyShort4);
        databaseHandler.addShortTermNoteOfLongTerm(dummyShort5);
        databaseHandler.addShortTermNoteOfLongTerm(dummyShort6);*/


        List<LongTermNote> longTermNoteList = databaseHandler.findAllLongNotes();
        int i = 0;
        for (LongTermNote longNote : longTermNoteList) {
            listDataHeader.add(longNote.getTitle());
            List<ShortTermNote> shortNoteList = databaseHandler.findShortByLong(longNote.getId());
            List<String> shortNoteListToString = new ArrayList<>();
            for (ShortTermNote shortTermNote : shortNoteList) {
                shortNoteListToString.add(shortTermNote.toString());
            }
            listHash.put(listDataHeader.get(i), shortNoteListToString);
            i++;
        }


//        listDataHeader.add("Java");
//        listDataHeader.add("C#");
//        listDataHeader.add("Python");
//        listDataHeader.add("Something");
//
//        List<String> list1 = new ArrayList<>();
//        list1.add("This is Expandable List");
//        List<String> list2 = new ArrayList<>();
//        list2.add("Blablabla");
//        list2.add("Blablabla");
//        list2.add("Blablabla");
//        List<String> list3 = new ArrayList<>();
//        list3.add("This is Expandable List Of list 3");
//        list3.add("This is Expandable List Of list 3");
//        list3.add("This is Expandable List Of list 3");
//        list3.add("This is Expandable List Of list 3");
//        List<String> list4 = new ArrayList<>();
//        list4.add("Putang Inamo bobo");
//        list4.add("Putang Inamo bobo");
//        list4.add("Putang Inamo bobo");
//
//        listHash.put(listDataHeader.get(0), list1);
//        listHash.put(listDataHeader.get(1), list2);
//        listHash.put(listDataHeader.get(2), list3);
//        listHash.put(listDataHeader.get(3), list4);


    }


}
