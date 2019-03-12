package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.example.assignment.DBHandler.DatabaseHandler;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;

public class ViewShortPlansActivity extends AppCompatActivity {

    private static final int SHORT_MAIN_REQUEST_CODE = 101;
    private int receiveCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("List of short term tasks");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_short_plans);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewShortPlansActivity.this, AddShortPlanActivity.class);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        updateListView();

        //set onlclick event handler
        //set onclick handler for item
        ListView listView = findViewById(R.id.listPlan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewShortPlansActivity.this, ViewShortPlanDetail.class);
                TwoLineListItem twoLineListItem  =(TwoLineListItem) view;
                String firstLine = twoLineListItem.getText1().getText().toString();
                String[] words=firstLine.split("\\s");
                intent.putExtra("id", words[0]);
                startActivityForResult(intent, SHORT_MAIN_REQUEST_CODE);
                //startActivity(intent);
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListView(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    public void updateListView() {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        ListView listView = findViewById(R.id.listPlan);
        final ArrayList<ShortTermNote> notes = databaseHandler.findAllShortNotes();
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No task to display!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, notes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                text1.setText(notes.get(position).getId() + " " + notes.get(position).getTitle());
                text2.setText(notes.get(position).getContent());

                return view;
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
