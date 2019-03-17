package com.example.assignment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.example.assignment.DBHandler.DatabaseHandler;
import com.example.assignment.Entity.LongTermNote;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;

public class ViewLongPlanDetail extends AppCompatActivity {
    private static final int SHORT_MAIN_REQUEST_CODE = 101;
    String longTermPlanTitle;
    private ArrayList<ShortTermNote> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_long_plan_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        longTermPlanTitle = bundle.getString("longplan");
        setTitle(longTermPlanTitle);
        updateListView(longTermPlanTitle);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLongPlanDetail.this, AddShortOfLongPlanActivity.class);
                intent.putExtra("longplan", longTermPlanTitle);
                startActivity(intent);
            }
        });

        //set onlclick event handler
        //set onclick handler for item
        ListView listView = findViewById(R.id.listShortPlan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ViewLongPlanDetail.this, ViewShortPlanDetail.class);
                myIntent.putExtra("id", String.valueOf(notes.get(position).getId()));
                myIntent.putExtra("from_activity","viewlongplandetail");
                myIntent.putExtra("longplan",longTermPlanTitle);
                startActivityForResult(myIntent, SHORT_MAIN_REQUEST_CODE);
            }
        });

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refreshLongDetail);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListView(longTermPlanTitle); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void updateListView(String longTitle) {
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        LongTermNote longTermNote = databaseHandler.findLongTermNote(longTitle);
        ListView listView = findViewById(R.id.listShortPlan);
        notes = databaseHandler.findShortByLong(longTermNote.getId());
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No task to display!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, notes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_note_with_checkmark, parent, false);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.itemDeleteLong:
                Toast.makeText(ViewLongPlanDetail.this, "Delete Long", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.itemUpdateLong:
                final Dialog dialog = new Dialog(ViewLongPlanDetail.this);
                dialog.setTitle("Update Title");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.update_long_plan);
                final EditText txtUpdate = dialog.findViewById(R.id.editUpdate);
                txtUpdate.setText(longTermPlanTitle);
                Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = txtUpdate.getText().toString();
                        DatabaseHandler databaseHandler = new DatabaseHandler(ViewLongPlanDetail.this);
                        LongTermNote longTermNote = databaseHandler.findLongTermNote(longTermPlanTitle);
                        int id = longTermNote.getId();
                        longTermNote = new LongTermNote(title);
                        databaseHandler.updateLongNote(id, longTermNote);
                        Intent intent = new Intent(ViewLongPlanDetail.this, ViewLongPlanDetail.class);
                        intent.putExtra("longplan", title);
                        startActivity(intent);


                        dialog.cancel();


                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.long_plan_menu, menu);
        return true;
    }


}
