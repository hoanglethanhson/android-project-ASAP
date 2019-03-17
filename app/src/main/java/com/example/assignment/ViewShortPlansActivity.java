package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private ArrayList<ShortTermNote> notes;

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        updateListView();

        //set onlclick event handler
        //set onclick handler for item
        ListView listView = findViewById(R.id.listPlan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewShortPlansActivity.this, ViewShortPlanDetail.class);

                intent.putExtra("id",String.valueOf(notes.get(position).getId()));
                intent.putExtra("from_activity","viewshortplanactivity");
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
        notes = databaseHandler.findAllShortNotes();
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No task to display!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, notes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());
                convertView = layoutInflater.inflate(R.layout.item_note_with_checkmark, parent, false);

                TextView txtTitle = convertView.findViewById(R.id.tv_item_text);
                TextView txtContent = convertView.findViewById(R.id.tv_item_content);
                ImageView check = convertView.findViewById(R.id.iv_checkmark);
                txtTitle.setText(notes.get(position).getTitle());
                txtContent.setText(notes.get(position).getContent());
                if (notes.get(position).getIsComplete() == 1) {
                    check.setVisibility(View.VISIBLE);
                } else {
                    check.setVisibility(View.INVISIBLE);
                }


                return convertView;
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
