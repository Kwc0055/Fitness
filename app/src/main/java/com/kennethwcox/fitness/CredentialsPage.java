package com.kennethwcox.fitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

public class CredentialsPage extends AppCompatActivity {

    public SharedPreferences save;
    public static String USER_FILE_DATA = "User_Data";

    EditText name;
    EditText age;
    EditText inches;
    EditText feet;
    EditText weight;

    Button saveBtn;

    String nameSave;
    String ageSave;
    String inchesSave;
    String feetSave;
    String weightSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize the shared prefference
        save = PreferenceManager.getDefaultSharedPreferences(this);

        //initialize all the edit text fields
        name = (EditText) findViewById(R.id.name_edit);
        name.setHint(R.string.nameEdit);
        age = (EditText) findViewById(R.id.age_edit);
        age.setHint(R.string.ageEdit);
        weight = (EditText) findViewById(R.id.weight_edit);
        weight.setHint(R.string.weightEdit);
        feet = (EditText) findViewById(R.id.ft_edit);
        feet.setHint(R.string.feetEdit);
        inches = (EditText) findViewById(R.id.in_edit);
        inches.setHint(R.string.inchesEdit);

        //initialize button
        saveBtn = (Button) findViewById(R.id.saveButton);

        if (save.contains("initialized")) {
            LoadAction();
        }


        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getInformation();
                SaveAction();
                Toast.makeText(v.getContext(), "Data Saved!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void getInformation() {
        nameSave = name.getText().toString();
        ageSave = age.getText().toString();
        weightSave = weight.getText().toString();
        feetSave = feet.getText().toString();
        inchesSave = inches.getText().toString();
    }

    public void LoadAction() {
        //pull data from shared preference
        name.setText(save.getString("uName", "name"));
        age.setText(save.getString("uAge", "age"));
        weight.setText(save.getString("uWeight", "weight"));
        feet.setText(save.getString("uFeet", "ft"));
        inches.setText(save.getString("uInches", "in"));
    }

    public void SaveAction() {

        SharedPreferences.Editor editor = save.edit();
        editor.clear();
        editor.putBoolean("initialized", true);
        editor.putString("uName", nameSave);
        editor.putString("uAge", ageSave);
        editor.putString("uWeight", weightSave);
        editor.putString("uFeet", feetSave);
        editor.putString("uInches", inchesSave);
        editor.commit();
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

        switch (id) {
            case R.id.about_menu_item:
                i = new Intent(this, AboutPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.startActivity(i);
                return true;
            case R.id.edit_profile_menu_item:
                //add a toast message
                makeText(this, "Already on Edit Profile Page!", LENGTH_SHORT).show();
                break;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.about_menu_item) {
            i = new Intent(this, AboutPage.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(i);
            return true;
        }*/

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

}
