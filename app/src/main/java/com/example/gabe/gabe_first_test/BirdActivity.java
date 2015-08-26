package com.example.gabe.gabe_first_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;

public  class BirdActivity extends FragmentActivity implements GameOverDialog.NoticeDialogListener,
        SensorEventListener {
    //final boolean DEBUG = true;
    private TappyBirdView birbView;
    private FoodClass food;
    private BirdClass birb;
    private boolean started;
    long mLastMove = 0;
    long mMoveDelay = 40;
    long start;
    ArrayList<Actor> actors;
    ClockClass clock;
    int numThreads = 0;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String HSFILENAME;
    Integer highscore;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    boolean newRecord;
    float force;
    int numFood;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird);
        birbView = (TappyBirdView) findViewById(R.id.birdView);
        HSFILENAME =  this.getFilesDir() +  "/highscores_file.txt";
        File file = new File(/*Environment.getDataDirectory() + "/" + */HSFILENAME);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        force = 0;
        rand = new Random();
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        } else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (savedInstanceState == null || !savedInstanceState.containsKey("ACTORS_KEY")) {
            started = false;
            birbView.UpdateClock(15);
            clock = new ClockClass();
            actors = new ArrayList<>();
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            try {
//                if (!file.createNewFile()) {
//                    fileReader = new FileReader(file);
//                    highscore = fileReader.read();
//                    fileReader.close();
//                }
//                else { highscore = 0; }
                if (file.exists()) {
                    FileReader fileIn = new FileReader(HSFILENAME);
                    BufferedReader buffIn = new BufferedReader(fileIn);
//                    int dump = fileIn.read();
//                    char c = (char) dump;
//                    String str = String.valueOf(c);
//                    dump = Integer.getInteger(str);
                    String str = buffIn.readLine();
                    if (str != null) {
                        highscore = Integer.parseInt(str);
                    } else { highscore = 0; }
                    buffIn.close();
                    fileIn.close();
                }
                else { file.createNewFile(); }
            } catch (FileNotFoundException e) {
//                try {
                //file.createNewFile();
                highscore = 0;
//                } catch (IOException f) { }
            } catch (IOException g) { }

        } else {
            actors = savedInstanceState.getParcelableArrayList("ACTORS_KEY");
            clock = new ClockClass(savedInstanceState.getInt("CLOCK_VALUE"));
            birbView.UpdateClock(clock.getTimeLeft());
            birb = savedInstanceState.getParcelable("PLAYER_BIRD");
            birbView.UpdateScore(birb.getFoodEaten());
            started = false;
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            highscore = savedInstanceState.getInt("HIGHSCORE_KEY");
        }
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ACTORS_KEY", actors);
        outState.putParcelable("PLAYER_BIRD", birb);
        outState.putInt("CLOCK_VALUE", clock.getTimeLeft());
        outState.putInt("HIGHSCORE_KEY", highscore);
        //outState.putBoolean("STARTED_KEY", started);
        mRedrawHandler.turnOff();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bird, menu);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!started) {
            if (birb == null) {
                food = new FoodClass(this);
                birb = new BirdClass(this, 0.5f, 0.5f);
                actors.add(birb);
                actors.add(food);
                actors.add(new FoodClass(this));
                actors.add(new EvilBirdClass(this, birb));
                numFood = 2;
                clock.setTimeLeft(15);
                start = System.currentTimeMillis();
                started = true;
        } else {
            start = System.currentTimeMillis() - 1000 * clock.getTimeLeft();
        }
        updateGame();
    }
//        birb.Goto(ToFracConvertWidth(event.getX()), ToFracConvertHeight(event.getY()));
//        birbView.drawBirb(birb);
//        if (goLeft) {
//            birb.Goto(birb.getX() + 0.03f, ToFracConvertHeight(event.getY()));
//        } else if (goRight) {
//            birb.Goto(birb.getX() - 0.03f, ToFracConvertHeight(event.getY()));
//        } else {
//            birb.Goto(birb.getX(), ToFracConvertHeight(event.getY()));
//        }
        birb.jump();
        return false;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        force = event.values[0];
//        goRight = false;
//        goLeft = false;
//        if (x > 0.2) {
//            goRight = true;
//        } else if (x < -0.2) {
//            goLeft = true;
//        }
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        volatile boolean keepGoing;
        RefreshHandler() {
            super();
            keepGoing = true;
            numThreads++;
        }
        public void turnOff() { keepGoing = false;}
        @Override
        public void handleMessage(Message msg) {
            if (keepGoing) {
                updateGame();
            }
//            invalidate();
        }
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    public void updateGame() { //TODO: Make bird flap. (And make noise?)
        if (clock.getTimeLeft() > 0) {
            long now = System.currentTimeMillis();
            if (now - mLastMove > mMoveDelay) {
                if (start + (15 - clock.getTimeLeft()) * 1000 < now) {
                    clock.Countdown();
                    birbView.UpdateClock(clock.getTimeLeft());
                }
                if (rand.nextFloat() < (0.08f - 0.01f*numFood)) {
                    actors.add(new FoodClass(this));
                    numFood++;
                }
                UpdateActors(actors);
//                if (goLeft) birb.slideLeft();
//                else if (goRight) birb.slideRight();
                birb.slide(force);
                //birb.Update();
                CheckCollisions(actors);
                birbView.UpdateScore(birb.getFoodEaten());
                birbView.drawObjects(actors);
                mLastMove = now;
            }
            mRedrawHandler.sleep(mMoveDelay);
        }
        else {
            if (birb.getFoodEaten() > highscore) {
                newRecord = true;
                highscore = birb.getFoodEaten();
                //WriteHighscore();
            } else { newRecord = false; }
            showGameOver();
        }
    }

    public void WriteHighscore(String name) {
        try {
//            FileOutputStream fos = openFileOutput(HSFILENAME, Context.MODE_PRIVATE);
//            OutputStreamWriter out = new OutputStreamWriter(fos);
//            out.write(String.valueOf(highscore));
//            out.close();
//            fos.close();
            FileWriter writer = new FileWriter(HSFILENAME);
            //FileOutputStream fos = openFileOutput(HSFILENAME, Context.MODE_PRIVATE);
            BufferedWriter buffOut = new BufferedWriter(writer);
            buffOut.write(highscore.toString() + '\n' + "Highscore: " + highscore + ". Set by " + name + ".");
            buffOut.flush();
            buffOut.close();
            writer.close();
            //fos.write(String.valueOf(highscore).getBytes());
            //fos.close();
        } catch (IOException e) { }
    }

    public float ToPixConvertWidth(float fracWidth) {
        return fracWidth * birbView.getWidth();
    }

    public float ToFracConvertWidth(float pixWidth) {
        return pixWidth/birbView.getWidth();
    }

    public float ToPixConvertHeight(float fracHeight) {
        return fracHeight * birbView.getHeight();
    }

    public float ToFracConvertHeight(float pixHeight) {
        return pixHeight/birbView.getHeight();
    }

    public void CheckCollisions(List<Actor> actors) {
        for (Actor obj : actors) {
            for (Actor other : actors) {
                if ( other != obj && (!(ToPixConvertWidth(obj.getX()) > ToPixConvertWidth(other.getX()) + other.getPic().getWidth()
                        || ToPixConvertWidth(obj.getX()) + obj.getPic().getWidth() < ToPixConvertWidth(other.getX()))
                        && !(ToPixConvertHeight(obj.getY()) > ToPixConvertHeight(other.getY()) + other.getPic().getHeight()
                        || ToPixConvertHeight(obj.getY()) + obj.getPic().getHeight() < ToPixConvertHeight(other.getY())))) {
                    obj.Collide(other);
                }
            }
        }
    }

    public void UpdateActors(List<Actor> actors) {
        Actor obj;
        for (int i = 0; i < actors.size(); i++) {
            obj = actors.get(i);
            obj.Update();
            if (obj.Done()) {
                if (obj instanceof FoodClass)
                    numFood--;
                actors.remove(obj);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        if (newRecord) {
            String name = ((EditText) fragment.getDialog().findViewById(12345)).getText().toString();
            WriteHighscore(name);
        }
        Intent upIntent = new Intent(this, MainActivity.class);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is not part of the application's task, so
            // create a new task
            // with a synthesized back stack.
            TaskStackBuilder
                    .from(this)
                    .addNextIntent(new Intent(this, MainActivity.class))
                    .addNextIntent(upIntent).startActivities();
            finish();
        } else {
            // This activity is part of the application's task, so simply
            // navigate up to the hierarchical parent activity.
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        if (newRecord) {
            String name = ((EditText) fragment.getDialog().findViewById(12345)).getText().toString();
            WriteHighscore(name);
        }
        birb = new BirdClass(this, 0.5f, 0.5f);
        clock = new ClockClass();
        actors.clear();
        actors.add(birb);
        actors.add(new FoodClass(this));
        actors.add(new FoodClass(this));
        actors.add(new EvilBirdClass(this, birb));
        updateGame();
        start = System.currentTimeMillis();
    }

    public void showGameOver() {
        DialogFragment dialog = GameOverDialog.newInstance(highscore, newRecord);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "NoticeDialogFragment");
    }

}
