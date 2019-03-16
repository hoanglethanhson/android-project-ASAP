package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private FloatingActionButton fabDelete, fabRevert;

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
        fabDelete = findViewById(R.id.fabDelete);
        fabRevert = findViewById(R.id.fabRevert);
        updateListTrash();

        //add event
        listTrash.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                revertOneItem(view, position);
                return false;
            }
        });
        listTrash.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteOneItem(view,position);
            }
        });
        fabRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revertAll(view);
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll(view);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void deleteAll(View v) {
        if (notes.isEmpty()) {
            Toast.makeText(getApplicationContext(), "NOTHING TO DELETE!", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirmation");
        final TextView message = new TextView(v.getContext());
        message.setText("\n\t\t\tDo you want to DELETE all?");
        builder.setView(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.deleteAllBin();
                updateListTrash();
                Toast.makeText(getApplicationContext(), "DELETE ALL SUCCESSFULLY!", Toast.LENGTH_LONG).show();
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

    private void revertAll(View v) {
        if (notes.isEmpty()) {
            Toast.makeText(getApplicationContext(), "NOTHING TO REVERT!", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirmation");
        final TextView message = new TextView(v.getContext());
        message.setText("\n\t\t\tDo you want to REVERT all?");
        builder.setView(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.revertAllBin();
                updateListTrash();
                Toast.makeText(getApplicationContext(), "REVERT ALL SUCCESSFULLY!", Toast.LENGTH_LONG).show();
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

    private void deleteOneItem(View v, final int position) {
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
                Toast.makeText(getApplicationContext(), "DELETE ITEM SUCCESSFULLY!", Toast.LENGTH_LONG).show();
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

    private void revertOneItem(View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirmation");
        final TextView message = new TextView(v.getContext());
        message.setText("\n\t\t\tDo you want to revert this?");
        builder.setView(message);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int itemId = notes.get(position).getId();
                databaseHandler.restorePlan(itemId);
                updateListTrash();
                Toast.makeText(getApplicationContext(), "REVERT ITEM SUCCESSFULLY", Toast.LENGTH_LONG).show();
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
