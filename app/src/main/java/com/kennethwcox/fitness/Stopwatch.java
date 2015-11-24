package com.kennethwcox.fitness;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class Stopwatch extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{
    //variables
    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours = "00", minutes = "00" ,seconds = "00", milliseconds = "000";
    private long secs,mins,hrs,msecs;
    private boolean stopped = false;
    private long ms, s, m, h, temps = 0, temps2 = 0, check;
    TextView laptext;
    LinkedList<String> lapList = new LinkedList<>();
    ListAdapter adapter;
    ListView list;
    int lap = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        //hide the stop button
        hideStopButton();


           /*                    create the list                 */
        //create adapter
        adapter = new ListAdapter(this, R.layout.laplistview, lapList);

        //configure the list view
        list = (ListView)findViewById(R.id.lapTimeList);

    }


    //start button method
    public void startClick(View view) {
        showStopButton();

        if(stopped){
            startTime = System.currentTimeMillis() - elapsedTime;
        }
        else{
            startTime = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    //stop button method
    public void stopClick(View view){
        hideStopButton();

        mHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    //reset button method
    public void resetClick(View view){
        stopped = false;
        ((TextView)findViewById(R.id.timer)).setText("00:00:00");
        ((TextView)findViewById(R.id.timerMS)).setText(".000");
        h = 0;
        m = 0;
        s = 0;
        ms = 0;

        hrs = 0;
        mins = 0;
        secs = 0;
        msecs = 0;
    }

    //lap button method
    public void lapClick(View view){
        addListItems();
    }


    //show and hide methods
    private void showStopButton(){
        (findViewById(R.id.startBtn)).setVisibility(View.GONE);
        (findViewById(R.id.resetBtn)).setVisibility(View.GONE);
        (findViewById(R.id.stopBtn)).setVisibility(View.VISIBLE);
    }

    private void hideStopButton(){
        (findViewById(R.id.startBtn)).setVisibility(View.VISIBLE);
        (findViewById(R.id.resetBtn)).setVisibility(View.VISIBLE);
        (findViewById(R.id.stopBtn)).setVisibility(View.GONE);
    }

    //timer function
    private void Timer(float time){
        //milliseconds to seconds
        secs = (long)(time/1000);
        //seconds to minutes
        mins = (long)((time/1000)/60);
        //minutes to hours
        hrs = (long)(((time/1000)/60)/60);

        //convert to string
        secs = secs % 60;
        seconds = String.valueOf(secs);

        //proper display of seconds w/ leading 0
        if(secs == 0){
            seconds = "00";
        }
        if(secs < 10 && secs > 0){
            seconds = "0" + seconds;
        }

        //same but with minutes
        mins = mins % 60;
        minutes = String.valueOf(mins);

        if(mins == 0){
            minutes = "00";
        }
        if(mins < 10 && mins > 0){
            minutes = "0" + minutes;
        }

        //same but with hours
        hours = String.valueOf(hrs);

        if(hrs == 0){
            hours = "00";
        }
        if(hrs < 10 && hrs > 0){
            hours = "0" + hours;
        }

        //grab milliseconds and set up text
        msecs = (long)time % 1000;

        milliseconds = String.valueOf(msecs);
        if (milliseconds.length() == 2) {
            //show 10s and 1s place
            milliseconds = "0" + milliseconds;

        }
        if (milliseconds.length() <= 1) {
            //show 100s place
            milliseconds = "00";
        }

        // Setting the timer text to the elapsed time
        ((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds);
        ((TextView)findViewById(R.id.timerMS)).setText("." + milliseconds);
    }

    //timer runnable function
    private Runnable startTimer = new Runnable(){
        public void run(){
            elapsedTime = System.currentTimeMillis() - startTime;
            Timer(elapsedTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };


    //list of lap times
    private void addListItems(){
        String temp;
        long check2 = 0, tms = 0, ts = 0, tm = 0, th = 0;

        if(lap > 1){

            temps2 = (((((hrs * 60) + mins) * 60) + secs) * 1000) + msecs; //convert lap to ms

            check2 = (temps2) - (temps); //difference in milliseconds

            //get difference from last stored lap and current lap
            h = hrs - h;
            m = mins - m;
            s = secs - s;
            ms = msecs - ms;

            //if you get a negative value convert and subtract
            if(ms < 0){
                s -= 1;

                ms = 1000 + ms;
            }
            else if(s < 0){
                m -= 1;

                s = 60 + s;
            }
            else if(m < 0){
                h -= 1;

                m = 60 + m;
            }


            //set text color
            if(check2 > check){
                tms = (check2 - check);
                tm = ts/60;
                th = tm/60;

                if(tms > 1000){
                    ts = tms / 1000; //gets the seconds from ms
                    tms %= 1000;

                    if(ts > 60){
                        tm = ts / 60;
                        ts %= 60;

                        if(tm > 60){
                            th = tm / 60;
                            tm %= 60;
                        }
                    }
                }

                //set lap text color to red
                temp = ("rLap " + lap + ": " + h + ":" + m + ":" + s + "." + ms + "         + " + th + ":" + tm + ":" + ts + "." + tms);
            }else if(check2 == check){
                //time is the EXACT same as the last lap
                //set text to Black
                temp = ("Lap " + lap + ": " + h + ":" + m + ":" + s + "." + ms);
            }else{
                //this lap is FASTER than the last lap

                tms = (check - check2);
                tm = ts/60;
                th = tm/60;

                if(tms > 1000){
                    ts = tms / 1000; //gets the seconds from ms
                    tms %= 1000;

                    if(ts > 60){
                        tm = ts / 60;
                        ts %= 60;

                        if(tm > 60){
                            th = tm / 60;
                            tm %= 60;
                        }
                    }
                }
                //set text to green
                temp = ("gLap " + lap + ": " + h + ":" + m + ":" + s + "." + ms + "         - " + th + ":" + tm + ":" + ts + "." + tms);
            }

            check = check2; //get time of the previous lap
        }
        else{
            temp = ("Lap " + lap + ": " + hours + ":" + minutes + ":" + seconds + "." + milliseconds);
            check = (((((hrs * 60) + mins) * 60) + secs) * 1000) + msecs; //convert 1st lap to ms and store it
        }

        //store the previous lap time
        h = hrs;
        m = mins;
        s = secs;
        ms = msecs;

        temps = (((((h * 60) + m) * 60) + s) * 1000) + ms; //convert lap to ms to be calculated during next lap



        lapList.add(temp);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        lap += 1;
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
                i = new Intent(this, AboutPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                return true;
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
        /*if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void onNavigationDrawerItemSelected(int position) {
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
                Toast.makeText(this, "Already on Stopwatch Page!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                i = new Intent(this, ProfilePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 4:
                i = new Intent(this, MapsFragment.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 5:
                Toast.makeText(this, "COMING SOON!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_app_bar, container, false);
        return rootView;
    }

}
