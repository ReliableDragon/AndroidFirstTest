package com.example.gabe.gabe_first_test;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Gabe on 1/26/2015.
 */
public class TappyBirdView extends View {
    final int NUM_PICS = 2;
    static final int BIRB = 0;
    static final int FRUIT = 1;
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    private Bitmap[] pics;
    //canvas bitmap
    private Bitmap canvasBitmap/*, birb, fruit*/;
    Paint paint = new Paint();
    Paint textPaint = new Paint();
    Paint pausePaint = new Paint();
    String score, clock;

    public TappyBirdView(Context context) {
        super(context);
        setupDrawing();
    }

    public TappyBirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void DrawBird(Canvas canvas) {

    }

    public void setupDrawing() {
        pics = new Bitmap[NUM_PICS];
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        /*birb*/pics[0] = BitmapFactory.decodeResource(getResources(), R.drawable.birb);
        /*fruit*/pics[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fruit);
        score = "0";
        textPaint.setColor(Color.GREEN);
        textPaint.setTextSize(40);
        pausePaint.setColor(Color.GREEN);
        pausePaint.setTextSize(240);
        //drawCanvas.drawText("TAP TO START", getWidth()/2, getHeight()/2, pausePaint);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawText(score, 0.1f*getWidth(), 0.1f*getHeight(), textPaint);
        canvas.drawText(clock, 0.8f*getWidth(), 0.1f*getHeight(), textPaint);
    }

    public void drawBirb(BirdClass bird) {
        drawCanvas.drawBitmap(pics[BIRB], bird.getX(), bird.getY(), paint);
    }

    public void drawFood(FoodClass food) {
        drawCanvas.drawBitmap(pics[FRUIT], food.getX(), food.getY(), textPaint);
    }

    public void clearScreen() {
        invalidate();
        drawCanvas.drawColor(Color.BLACK);
    }

    public void UpdateScore(int inScore) {
        score = String.valueOf(inScore);
    }

    public void UpdateClock(int timeLeft) {
        clock = String.valueOf(timeLeft);
    }

    public void drawObjects(List<Actor> Actors) {
        clearScreen();
        for (Actor obj : Actors) {
            drawCanvas.drawBitmap(obj.getPic(), obj.getX()*getWidth(), obj.getY()*getHeight(), textPaint);
        }
    }

    public Bitmap getPic(int objID) {
        return pics[objID];
    }

    public void Pause() {
        drawCanvas.drawText("PAUSED", getWidth()/2, getHeight()/2, pausePaint);
    }

}