package com.kennethwcox.fitness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class ProfilePage extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    Context context;

    public static final String TAG = ProfilePage.class.getSimpleName();
    private static final int SELECT_PICTURE_ACTIVITY_REQUEST_CODE = 0;

    private ImageView image;
    private String imageUri = "";
    private Uri uri;
    public SharedPreferences savedData;
    private Bitmap bm;
    public boolean dialogCheck;


    TextView name;
    TextView age;
    TextView ft;
    TextView in;
    TextView weight;

    //path: /data/data/com.kennethwcox.fitness


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        image = (ImageView) findViewById(R.id.profilePicture); //set up image view
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.ProfileAge);
        ft = (TextView) findViewById(R.id.Height_ft);
        in = (TextView) findViewById(R.id.Height_in);
        weight = (TextView) findViewById(R.id.ProfileWeight);
        context = this;

        savedData = PreferenceManager.getDefaultSharedPreferences(this);

        if (savedData.contains("uImage")) {
            String bmString = savedData.getString("uImage", "image");
            bm = decodeBase64(bmString);

            image.setImageBitmap(bm);

        }
        if (savedData.contains("initialized")) {
            LoadAction();
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsAlert();
            }
        });

    }

    public void LoadAction() {
        //pull data from shared preference
        name.setText(savedData.getString("uName", "name"));
        age.setText("Age: " + savedData.getString("uAge", "age"));
        weight.setText("Wt: " + savedData.getString("uWeight", "weight"));
        ft.setText("Ht: " + savedData.getString("uFeet", "ft") + "'");
        in.setText(savedData.getString("uInches", "in"));
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Profile Picture");
        alertDialog.setMessage("Select a new profile picture?");

        //if they tap button to select new image
        alertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 1);
            }
        });

        //if they tap cancel
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        uri = data.getData();

        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                image.setImageURI(uri);
                bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
            }
        }

        SharedPreferences.Editor editor = savedData.edit();
        editor.putString("uImage", encodeTobase64(bm));

        editor.apply();

    }

    //turn the image into a string
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    //turn the string of the image back into an image
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
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
                Toast.makeText(this, "Already on Profile Page!", Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.main_app_bar, container, false);
    }


}
