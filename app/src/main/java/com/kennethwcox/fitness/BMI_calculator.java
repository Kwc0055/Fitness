package com.kennethwcox.fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class BMI_calculator extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public SharedPreferences load;

    EditText weight;
    EditText ft;
    EditText in;
    TextView result;
    TextView classify;
    SeekBar bar;
    Button calc;
    DecimalFormat format;
    String bmiText;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //load saved data
        load = PreferenceManager.getDefaultSharedPreferences(this);

        /* formula is (weight/height * height) * 703 = bmi */
        weight = (EditText) findViewById(R.id.BMI_weight_text);
        ft = (EditText) findViewById(R.id.BMI_height_ft_edit);
        in = (EditText) findViewById(R.id.BMI_height_in_edit);

        //load the saved data if there is any
        if(load.contains("initialized")){
            String weightLoad = load.getString("uWeight", "");
            weight.setText(load.getString("uWeight", ""));
            ft.setText(load.getString("uFeet", ""));
            in.setText(load.getString("uInches", ""));
        }else {
            weight.setHint(R.string.weight);
        }

        result = (TextView) findViewById(R.id.BMI_result);
        classify = (TextView) findViewById(R.id.classification);
        calc = (Button) findViewById(R.id.BMI_calc);
        bar = (SeekBar) findViewById(R.id.seekBar);
        context = getApplicationContext();
        format = new DecimalFormat("0.00");

        ft.setGravity(Gravity.CENTER_HORIZONTAL);
        in.setGravity(Gravity.CENTER_HORIZONTAL);
        weight.setGravity(Gravity.CENTER_HORIZONTAL);
        bar.setMax(100);


        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (weight.getText().toString().length() > 0 && ft.getText().toString().length() > 0 && in.getText().toString().length() > 0) {
                    Float w = Float.parseFloat(weight.getText().toString());
                    int feet = Integer.parseInt(ft.getText().toString());
                    int inches = Integer.parseInt(in.getText().toString());

                    int h = (feet * 12) + inches;
                    Float bmi = (w / (h * h)) * 703;
                    bmiText = format.format(bmi);
                    result.setText(bmiText);


                    //classification of bmi
                    if (bmi < 16) {
                        classify.setText("(Severe Thinness)");
                        bar.setProgress(13);
                        bar.setBackgroundColor(Color.rgb(255, 0, 0)); //red
                    } else if (bmi >= 16 && bmi <= 16.99) {
                        classify.setText("(Moderate Thinness)");
                        bar.setProgress(26);
                        bar.setBackgroundColor(Color.rgb(255, 128, 0)); //orange
                    } else if (bmi >= 17 && bmi <= 18.49) {
                        classify.setText("(Mild Thinness)");
                        bar.setProgress(39);
                        bar.setBackgroundColor(Color.rgb(255, 255, 51)); //yellow
                    } else if (bmi >= 18.5 && bmi <= 24.99) {
                        classify.setText("(Healthy!)");
                        bar.setProgress(50);
                        bar.setBackgroundColor(Color.rgb(0, 255, 0)); //green
                    } else if (bmi >= 25 && bmi <= 29.99) {
                        classify.setText("(Pre-Obese)");
                        bar.setProgress(63);
                        bar.setBackgroundColor(Color.rgb(255, 255, 51)); //yellow
                    } else if (bmi >= 30 && bmi <= 34.99) {
                        classify.setText("(Obese Class I)");
                        bar.setProgress(76);
                        bar.setBackgroundColor(Color.rgb(255, 153, 51)); //yellow-orange
                    } else if (bmi >= 35 && bmi <= 39.99) {
                        classify.setText("(Obese Class II)");
                        bar.setProgress(89);
                        bar.setBackgroundColor(Color.rgb(255, 128, 0)); //orange
                    } else {
                        classify.setText("(Obese Class III)");
                        bar.setProgress(100);
                        bar.setBackgroundColor(Color.rgb(255, 0, 0)); //red
                    }
                }else{
                    Toast.makeText(context, "Invalid Input!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        switch (position) {
            case 0:
                i = new Intent(this, HomePage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                break;
            case 1:
                Toast.makeText(this, "Already on BMI Calculator Page!", Toast.LENGTH_SHORT).show();
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
