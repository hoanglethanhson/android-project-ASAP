package com.example.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    Intent intent;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        listView = findViewById(R.id.listView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String[] questions = new String[]{
                "How to create new long term plan?",
                "How to add a short term plan?",
                "Do I have to pay for any extend function?"
        };

        List<String> question_list = new ArrayList<String>(Arrays.asList(questions));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.item_text_view, question_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 140;
                view.setLayoutParams(params);
                if(position %2 == 1)
                {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#fd267d"));
                }
                else
                {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.parseColor("#fe5466"));
                }
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position){
                        case 0:
                            intent = new Intent(view.getContext(),Question1.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(view.getContext(),Question2.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(view.getContext(),Question3.class);
                            startActivity(intent);
                    }
            }
        });
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
