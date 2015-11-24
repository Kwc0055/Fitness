package com.kennethwcox.fitness;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MapsFragment extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    public static final String TAG = MapsFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap;
    Location location;
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    private LocationRequest mLocationRequest;

    TextView lng;
    TextView lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_page);

        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        if((DrawerLayout)findViewById(R.id.drawer_layout) == null) {
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

//      Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds



        //tracker = new GPSTracker(this);
        lng = (TextView) findViewById(R.id.lngTxt);
        lat = (TextView) findViewById(R.id.latTxt);
        //mapFragment.getMapAsync(this);

        //createLocationRequest();
        mapFragment.getMapAsync(this);

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



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //setUpMap();
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");

        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));

        lat.setText(Double.toString(currentLatitude));
        lng.setText(Double.toString(currentLongitude));
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    /*protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }*/

    /*protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }*/

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    /*protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }*/







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
                Toast.makeText(this, "Already on Maps Page!", Toast.LENGTH_SHORT).show();
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
