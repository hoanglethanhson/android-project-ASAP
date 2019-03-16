package com.example.assignment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise here");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.asap_logo)
                .addItem(new Element().setTitle("Versiom 1.0"))
                .addItem(adsElement)
                .addGroup("Contact us:")
                .addEmail("quangtungson1@gmail.com")
                .addWebsite("www.dailymail.co.uk")
                .addFacebook("seaboyhl")
                .addInstagram("phanquanganh")
                .addYoutube("Fancorn")
                .addGitHub("hoanglethanhson/PRM391_ASAP")
                .create();
        setContentView(aboutPage);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
