package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<ShortTermNote> notes;
    private static final int MAIN_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("ASAP");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //This is a db test part

        //DatabaseHandler databaseHandler = new DatabaseHandler(this);

        /*databaseHandler.addShortTermNote(new ShortTermNote("untitled", "first task", "2019/03/16 07:00", -1, -1, -1 ));
        databaseHandler.addShortTermNote(new ShortTermNote("sleep", "none", "2019/04/30 09:00", -1, -1, -1 ));
        databaseHandler.addShortTermNote(new ShortTermNote("eat breakfast", "none", "2019/05/30 09:00", -1, -1, -1 ));
        databaseHandler.addShortTermNote(new ShortTermNote("play cs", "none", "2019/03/30 09:00", -1, -1, -1 ));
        databaseHandler.addShortTermNote(new ShortTermNote("eat lunch", "none", "2019/06/15 07:00", -1, -1, -1 ));
        */
        //update listView
        updateListView();

        //set onclick handler for item
        ListView listView = findViewById(R.id.listPlan);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewShortPlanDetail.class);
//                TwoLineListItem twoLineListItem  =(TwoLineListItem) view;
//                String firstLine = twoLineListItem.getText1().getText().toString();
//                String[] words=firstLine.split("\\s");
//                intent.putExtra("id", words[0]);

                intent.putExtra("id",String.valueOf(notes.get(position).getId()));
                startActivityForResult(intent, MAIN_REQUEST_CODE);
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
        //databaseHandler.deleteShortTermNote("third");
        ListView listView = findViewById(R.id.listPlan);
        notes = databaseHandler.findUrgentNotes();
        if (notes.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "No urgent notes to display!", Toast.LENGTH_LONG).show();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
                android.R.id.text1, notes) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//                View view =super.getView(position, convertView, parent);
//                TextView text1 = view.findViewById(android.R.id.text1);
//                TextView text2 = view.findViewById(android.R.id.text2);
//
//                text1.setText(notes.get(position).getId() + " " + notes.get(position).getTitle());
//                text2.setText(notes.get(position).getContent());
////                text1.setVisibility(View.INVISIBLE);
//
//                return view;
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


    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        AlertDialog.Builder alertDialogBuilder =
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }
                })
                .setNegativeButton("No",null);
         AlertDialog alertDialog =  alertDialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.shortPlans) {
            intent = new Intent(MainActivity.this, ViewShortPlansActivity.class);
            startActivity(intent);
        } else if (id == R.id.longPlans) {
            intent = new Intent(MainActivity.this, ViewLongPlansActivity.class);
            startActivity(intent);
        } else if (id == R.id.weather) {
            intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);

        } else if (id == R.id.trash) {
            intent = new Intent(MainActivity.this, ViewTrashActivity.class);
            startActivity(intent);

        } else if (id == R.id.about) {
            intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.help) {
            intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onHelpClick(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, HelpActivity.class);
        startActivity(intent);
    }
}
