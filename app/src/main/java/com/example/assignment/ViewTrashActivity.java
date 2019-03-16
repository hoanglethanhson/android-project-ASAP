package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

public class ViewTrashActivity extends AppCompatActivity {

    private ArrayList<ShortTermNote> notes;
    private DatabaseHandler databaseHandler;
    private ListView listTrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Plan Bin");
        setContentView(R.layout.activity_view_trash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler  = new DatabaseHandler(this);

        //init data
        listTrash  = findViewById(R.id.listTrash);
        updateListTrash();

        //add event
        listTrash.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                noticeRestore(view, position);
                return false;
            }
        });
        listTrash.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                noticeDelete(view, position);
            }
        });
    }

    private void noticeRestore(View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirmation");
        final TextView message = new TextView(v.getContext());
        message.setText("\n\t\t\tDo you want to restore this?");
        builder.setView(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int itemId = notes.get(position).getId();
                databaseHandler.restorePlan(itemId);
                updateListTrash();
                Toast.makeText(getApplicationContext(), "Restore from Recycle Bin successfully!", Toast.LENGTH_LONG).show();
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

    private void noticeDelete(View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirmation");
        final TextView message = new TextView(v.getContext());
        message.setText("\n\t\t\tDo you want to delete this?");
        builder.setView(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int itemId = notes.get(position).getId();
                databaseHandler.deleteShortById(itemId);
                updateListTrash();
                Toast.makeText(getApplicationContext(), "Delete from Recycle Bin successfully!", Toast.LENGTH_LONG).show();
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

    private void updateListTrash() {
        notes = databaseHandler.findAllTrash();
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No trash to display!", Toast.LENGTH_LONG).show();
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

        listTrash.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
