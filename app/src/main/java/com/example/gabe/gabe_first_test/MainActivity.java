/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gabe.gabe_first_test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

/**
 * Location sample.
 *
 * Demonstrates use of the Location API to retrieve the last known location for a device.
 * This sample uses Google Play services (GoogleApiClient) but does not need to authenticate a user.
 * See https://github.com/googlesamples/android-google-accounts/tree/master/QuickStart if you are
 * also using APIs that need authentication.
 */
public class MainActivity extends ActionBarActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    protected static final String TAG = "basic-location-sample";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView locationText;
    protected EditText editthingy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));
        locationText = (TextView) findViewById(R.id.locationtext);
        editthingy = (EditText) findViewById(R.id.typeything);

        buildGoogleApiClient();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            TextView text = (TextView) findViewById(R.id.touch_text);
            text.setText("Touched at " + x + "x, " + y + "y.");
        }
        //Toast.makeText(getApplicationContext(), "I'm here forever!", Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "I'm here briefly!", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            Geocoder geo = new Geocoder(getApplicationContext());
            try {
                locationText.setText(geo.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1).get(0).getThoroughfare());
            } catch (IOException e) { }
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void playSound(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.demonlight);
/*        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.prepare();
        } catch (IOException e) { }*/
        mPlayer.start();
    }

    public void birdThing (View view) {
        Intent intent = new Intent(this, BirdActivity.class);
        startActivity(intent);
    }

    public void pongAct (View view) {
        Intent intent = new Intent(this, PongActivity.class);
        startActivity(intent);
    }

    public void drawingAct (View view) {
        Intent intent = new Intent(this, DrawingActivity.class);
        startActivity(intent);
    }

    public void newAct (View view) {
        TextView myText = (TextView) findViewById(R.id.gpsstring);
        if (myText.getText()=="Do I change?")
            myText.setText("I changed!");
        else myText.setText("Do I change?");
    }

    public void geoAct (View view) {
        Intent intent = new Intent(this, GeoActivity.class);
        String message = mLastLocation.toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteHS(View view) {
        try {
            String HSFILENAME = this.getFilesDir() +  "/highscores_file.txt";
            FileWriter writer = new FileWriter(HSFILENAME);
            //FileOutputStream fos = openFileOutput(HSFILENAME, Context.MODE_PRIVATE);
            BufferedWriter buffOut = new BufferedWriter(writer);
            buffOut.write("");
            buffOut.flush();
            buffOut.close();
            writer.close();
        } catch (IOException e) { }
    }

    public void launchTCP(View view) {
        Intent intent = new Intent(this, TCPActivity.class);
        startActivity(intent);
    }

    public void launchPOS(View view) {
        Intent intent = new Intent(this, posActivity.class);
        startActivity(intent);
    }
}