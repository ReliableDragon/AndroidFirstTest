package com.example.gabe.gabe_first_test;

import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.MotionEvent;

/**
 * Created by Gabe on 1/21/2015.
 */

public class PongView extends View {
    private class Paddle {
        private float Y;
        public float getY() {
            return Y;
        }
        public void setY(float inY) {
            Y = inY;
        }
        private float targetY;
        public float getTargetY() {
            return targetY;
        }
        public void setTargetY(float inY) {
            targetY = inY;
        }
    }
    Paddle p1, p2;
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap, ball, paddle;

    private int ballX, ballY, paddleY, screenMaxY;
    private Double ballAngle, ballSpeed;
    Integer timesCalled = 0;
    boolean p2Last = false;
    boolean wallLast = false;
    boolean p1Last = false;
    float targetY = -1;

    long mLastMove = 0;
    long mMoveDelay = 40;

    Random r = new Random();

/*    long mLastMove = 0;
    long mMoveDelay = 400;*/
    boolean started;

    boolean goUp = false;
    boolean goDown = false;

    Paint paint = new Paint();

    public PongView(Context context) {
        super(context);
        setupDrawing();
    }

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    public void setupDrawing() {
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        started = false;
        paint.setColor(Color.GREEN);
        paint.setTextSize(30);
        //drawScreen();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        //canvas.drawPath(drawPath, drawPaint);
        if (!started) {
            started = true;
            p1 = new Paddle();
            p2 = new Paddle();
            p1.setY((float)canvas.getHeight()/2);
            p1.setTargetY((float) canvas.getHeight() / 2);
            p2.setY((float)canvas.getHeight()/2);
            p2.setTargetY((float) canvas.getHeight() / 2);
            ballX = canvas.getWidth()/2;
            ballY = canvas.getHeight()/2;
            ballSpeed = 13.0;
            ballAngle = r.nextDouble()*Math.PI*0.75 - Math.PI*0.375;
            screenMaxY = canvas.getHeight();
            updateGame();
        }
        canvas.drawText(timesCalled.toString(), 40+timesCalled, 40+timesCalled, paint);
        if(started) {
            canvas.drawText(timesCalled.toString(), 10+timesCalled, 10+timesCalled++, paint);
        }
        //canvas.drawRect(getWidth()-10, 0, getWidth(), getHeight(), paint);

        canvas.drawBitmap(ball, ballX, ballY, paint);
        canvas.drawBitmap(paddle, 0, p1.getY(), paint);
        canvas.drawBitmap(paddle, getWidth()-paddle.getWidth(), p2.getY(), paint);
        canvas.drawText("Paddle 1 This Side", 0, p1.getY(), paint);
    }

    public void drawScreen(Canvas canvas) {
        //canvas.drawRect(getWidth()-5, 0, getWidth(), getHeight(), paint);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.paddle), 0, p1.getY(), paint);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.paddle), getWidth()-paddle.getWidth(), p2.getY(), paint);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.ball), ballX, ballY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float Y = event.getY();
        float X = event.getX();
        if (X < getWidth()/2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE
                    || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                p1.setTargetY(Y);
                return true;
            /*if (Y > paddleY) {
                goUp = true;
            } else if (Y < paddleY) {
                goDown = true;
            }*/
            } /*else if (event.getAction() == MotionEvent.ACTION_UP) {
            *//*goUp = false;
            goDown = false;*//*
                targetY = -1;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            *//*goUp = false;
            goDown = false;
            if (Y > paddleY) {
                goUp = true;
            } else if (Y < paddleY) {
                goDown = true;
            }*//*
                targetY = Y;
            } */else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
                p1.setTargetY(p1.getY()+paddle.getHeight()/2);
                return true;
            } else return false;
        }
        else if (X > getWidth()/2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE
                    || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
                p2.setTargetY(Y);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
                p2.setTargetY(p2.getY()+paddle.getHeight()/2);
                return true;
            } else return false;
            return true;
        }
        return false;
    }

    private RefreshHandler mRedrawHandler = new RefreshHandler();

    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            updateGame();
            invalidate();
        }
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    public void updateGame() {
        long now = System.currentTimeMillis();
        if (now - mLastMove > mMoveDelay) {
            update();
            mLastMove = now;
        }
        mRedrawHandler.sleep(mMoveDelay);
    }

    public void update() {
        /*if (goDown && paddleY < screenMaxY-paddle.getHeight()) {
            paddleY -= 25;
        } else if (goUp && paddleY > 0) {
            paddleY += 25;
        }*//*
        if (targetY != -1) {*/
        if (p1.getY() + paddle.getHeight() / 2 > p1.getTargetY() && p1.getY() > 0) {
            if (p1.getY() + paddle.getHeight() / 2 > p1.getTargetY() + 25) {
                p1.setY(p1.getY() - 25);
            } else {
                p1.setY(p1.getTargetY()-paddle.getHeight()/2);
            }
        } else if (p1.getY() + paddle.getHeight() / 2 < p1.getTargetY() && p1.getY() + paddle.getHeight() < getHeight()) {
            if (p1.getY() + paddle.getHeight() / 2 < p1.getTargetY() - 25) {
                p1.setY(p1.getY() + 25);
            } else {
                p1.setY(p1.getTargetY()-paddle.getHeight()/2);
            }
        }
        if (p2.getY() + paddle.getHeight() / 2 > p2.getTargetY() && p2.getY() > 0) {
            if (p2.getY() + paddle.getHeight() / 2 > p2.getTargetY() + 25) {
                p2.setY(p2.getY() - 25);
            } else {
                p2.setY(p2.getTargetY()-paddle.getHeight()/2);
            }
        } else if (p2.getY() + paddle.getHeight() / 2 < p2.getTargetY() && p2.getY() + paddle.getHeight() < getHeight()) {
            if (p2.getY() + paddle.getHeight() / 2 < p2.getTargetY() - 25) {
                p2.setY(p2.getY() + 25);
            } else {
                p2.setY(p2.getTargetY()-paddle.getHeight()/2);
            }
        }
        //}
/*        goUp = false;
        goDown = false;*/
        ballX = (int)(ballX + Math.cos(ballAngle)*ballSpeed);
        ballY = (int)(ballY - Math.sin(ballAngle)*ballSpeed);
        checkCollisions();
        invalidate();
    }

    private void checkCollisions() {
        if (ballY+ball.getHeight() >= p2.getY() && ballY <= p2.getY()+paddle.getHeight() && ballX >= getWidth()-paddle.getWidth()) {
            ballAngle = Math.PI - ballAngle;
            p2Last = true;
            wallLast = false;
            p1Last= false;
        }
        else if (ballY <= 1 || ballY >= getHeight()-ball.getHeight()) {
            ballAngle = -1.0 * ballAngle;
            wallLast = true;
            p1Last = false;
            p2Last = false;
        }
        else if(ballY+ball.getHeight() >= p1.getY() && ballY <= p1.getY()+paddle.getHeight() && ballX <= paddle.getWidth()) {
            ballAngle = Math.PI - ballAngle;
            p1Last = true;
            wallLast = false;
            p2Last = false;
        }
    }

/*    private class gameRunner extends AsyncTask<void, void, void> {

        boolean move = true;

        protected void doInBackground() {
            long now = System.currentTimeMillis();
            if (now - mLastMove > mMoveDelay) {
                mLastMove = now;

            }
            //sleep(mMoveDelay);
        }
        protected void onProgressUpdate() {
            if (move) {
                pongView.update();
            }
        }
    }*/
}

