package com.example.assignment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

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
    private HashMap<String, List<ShortTermNote>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_long_plans);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("List of long term tasks");
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

                final Dialog dialog = new Dialog(ViewLongPlansActivity.this);
                dialog.setTitle("Add new Long Plan");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_long_plan);
                final EditText txtLongPlanTitle = dialog.findViewById(R.id.editAddLong);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = txtLongPlanTitle.getText().toString();
                        DatabaseHandler databaseHandler = new DatabaseHandler(ViewLongPlansActivity.this);
                        LongTermNote longTermNote = new LongTermNote(title);
                        databaseHandler.addLongTermNote(longTermNote);
                        dialog.cancel();
                        startActivity(getIntent());


                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();

            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(ViewLongPlansActivity.this, ViewLongPlanDetail.class);
                intent.putExtra("longplan",listDataHeader.get(groupPosition));
                startActivity(intent);
                return false;
            }
        });
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(listHash.get(listDataHeader.get(groupPosition)).size()==0){
                    Intent intent = new Intent(ViewLongPlansActivity.this, ViewLongPlanDetail.class);
                    intent.putExtra("longplan",listDataHeader.get(groupPosition));
                    startActivity(intent);
                }
                return  false;
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(); // your code
                pullToRefresh.setRefreshing(false);
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
      /*  LongTermNote dummyLong1 = new LongTermNote("Thi 7.5 Ielts");
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


            listHash.put(listDataHeader.get(i), shortNoteList);
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
