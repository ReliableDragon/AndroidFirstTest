package com.example.gabe.gabe_first_test;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] lines = message.split(" ");
        for (int i = 0; i < lines.length; i++) {
            for (int j = i; j > 0; j--) {
                boolean outOfOrder = false;
                for (int k = 0; k < Math.min(lines[j].length(), lines[j-1].length()); k++) {
                    if (toLower(lines[j-1].toCharArray()[k]) < toLower(lines[j].toCharArray()[k])) {
                        outOfOrder = true;
                        break;
                    }
                }
                if (!outOfOrder && lines[j].length() > lines[j-1].length())
                    outOfOrder = true;
                if (outOfOrder) {
                    String temp = lines[j-1];
                    lines[j-1] = lines[j];
                    lines[j] = temp;
                }
            }
        }
        message = "";
        for (int i = lines.length-1; i >=0; i--) {

            message = message + lines[i] + '\n';
        }
        setContentView(R.layout.activity_display_message);
        final TextView out = (TextView)findViewById(R.id.textview1);
        final String outmsg = message;
        out.setText(outmsg);
        out.setTextSize(40);
        out.setTextColor(Color.parseColor("#6B58DB"));
        ImageView image = new ImageView(this);
        //TextView textView = new TextView(this);
        //textView.setTextSize(40);
        //textView.setTextColor(Color.parseColor("#6B58DB"));
        //textView.setText(message);
        //setContentView(textView);
    }

    public static char toLower(char c) {
        if (c > 64 && c < 91)
            return (char)(c + 32);
        else return c;
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
