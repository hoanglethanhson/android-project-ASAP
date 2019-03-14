package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.example.assignment.Entity.LongTermNote;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;

public class ViewLongPlanDetail extends AppCompatActivity {
    private static final int SHORT_MAIN_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_long_plan_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            return;
        }
        final String longTermPlanTitle= bundle.getString("longplan");
        TextView txtHeader = findViewById(R.id.txtLongPlanDetail);
        txtHeader.setText(longTermPlanTitle);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        updateListView(longTermPlanTitle);

        //set onlclick event handler
        //set onclick handler for item
        ListView listView = findViewById(R.id.listShortPlan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ViewLongPlanDetail.this, ViewShortPlanDetail.class);
//                TwoLineListItem twoLineListItem  =(TwoLineListItem) view;
//                String firstLine = twoLineListItem.getText1().getText().toString();
//                String[] words=firstLine.split("\\s");
//                intent.putExtra("id", words[0]);
//                startActivityForResult(intent, SHORT_MAIN_REQUEST_CODE);
                //startActivity(intent);
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
        final ArrayList<ShortTermNote> notes = databaseHandler.findShortByLong(longTermNote.getId());
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No task to display!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, notes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.item_note_with_checkmark, parent, false);

                TextView text = convertView.findViewById(R.id.tv_item_text);
                ImageView check = convertView.findViewById(R.id.iv_checkmark);

                text.setText(notes.get(position).getTitle());
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

}
