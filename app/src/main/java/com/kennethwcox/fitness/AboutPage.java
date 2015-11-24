package com.kennethwcox.fitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class AboutPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       // NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        //drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;

        switch(id){
            case R.id.about_menu_item:
                //create a toast message here
                Toast.makeText(this, "Already on About Page!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_profile_menu_item:
                i = new Intent(this, CredentialsPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                return true;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.about_menu_item) {
            i = new Intent(this, AboutPage.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*public void onNavigationDrawerItemSelected(int position) {
        Intent i;
        switch(position){
            case 0:
                i = new Intent(this, HomePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 1:
                i = new Intent(this, BMI_calculator.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 2:
                i = new Intent(this, Stopwatch.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 3:
                i = new Intent(this, ProfilePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 4:
                Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
                break;
        }

    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_app_bar, container, false);
        return rootView;
    }

}
