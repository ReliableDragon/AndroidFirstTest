package com.example.gabe.gabe_first_test;

import java.io.IOException;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class TCPActivity extends ActionBarActivity {

    TextView lastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        lastMessage = (TextView) findViewById(R.id.lastMessage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tc, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        doesTCP tcpThread = new doesTCP();
        tcpThread.execute();
    }

    private class doesTCP extends AsyncTask<Integer, Integer, Void> {
        String message;
        TCPClient tcp;

        protected void onPreExecute() {
            tcp = new TCPClient();
        }

        @Override
        protected Void doInBackground(Integer[] dummy) {
            try {
                message = tcp.getMessage();
            } catch (IOException e) { }
            return null;
        }
        protected void onProgressUpdate(Integer[] dummy) {

        }

        protected void onPostExecute() {
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            lastMessage.setText(message);
        }
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
}
