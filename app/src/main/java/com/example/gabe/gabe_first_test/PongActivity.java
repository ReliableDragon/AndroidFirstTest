package com.example.gabe.gabe_first_test;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class PongActivity extends Activity /*implements OnClickListener*/ {
    PongView pongView;
    long mLastMove = 0;
    long mMoveDelay = 2000;
    long now;
    boolean keepGoing = true;
    boolean isDone = true;
    //boolean move = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pongView = new PongView(this);
        pongView.setBackgroundColor(Color.BLACK);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //TextView textview = new TextView(this);
        //textview.setText("This is a test");
        setContentView(R.layout.activity_pong);
/*        Button startButton = new Button(this);
        startButton.setText("Start");
        startButton.setOnClickListener(this);
        startButton.setId(R.id.startbutton);*/
//        Toast.makeText(this, "Starting", Toast.LENGTH_SHORT);
//        final GameRunner gameRunner = new GameRunner();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                while(keepGoing) {
//                    if (isDone)
//                    gameRunner.execute();
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        System.out.println("Got interrupted!");
//                    }
//                }
//            }
//        }, 2000);
//        Toast.makeText(this, "Done", Toast.LENGTH_SHORT);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pong, menu);
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

        return super.onOptionsItemSelected(item);
    }

/*    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.startbutton:
                pongView.drawScreen();
                break;
        }
    }*/

/*    @Override
    public void onResume() {
        while(keepGoing) {
            update();
        }
    }
    public void update() {
        now = System.currentTimeMillis();
        if (now - mLastMove > mMoveDelay) {
            mLastMove = now;
            pongView.update();
        }
    }*/
    private class GameRunner extends AsyncTask<Integer, Integer, Void>{

    boolean move = true;

    protected void onPreExecute() {
        isDone = false;
    }

    @Override
    protected Void doInBackground(Integer[] dummy) {
        /*while(keepGoing) {*/
            long now = System.currentTimeMillis();
            if (now - mLastMove > mMoveDelay) {
                mLastMove = now;
                move = true;
            }
            publishProgress();
        /*}*/
        return null;
    }
    protected void onProgressUpdate(Integer[] dummy) {
        if (move) {
            pongView.update();
            move = false;
            pongView.invalidate();
        }
    }

    protected void onPostExecute() {
        isDone = true;
    }
}
}
