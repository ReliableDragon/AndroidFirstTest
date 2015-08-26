package com.example.gabe.gabe_first_test;

import java.io.IOException;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.wallet.fragment.Dimension;

/**
 * Created by Gabe on 2/4/2015.
 */
public class CustomButton extends View {
    Bitmap trashButton;
    Paint paint = new Paint();
    Paint linePaint;
    boolean mShowText;
    int mColor, numBought;
    String mText;
    Float cost;
    GestureDetector mDetector;
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    public interface OnCurrentItemChangedListener {
        void OnCurrentItemChanged(CustomButton source, int currentItem);
    }

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);
        try {
            mColor = a.getColor(R.styleable.CustomButton_buttonColor, 0xff000000);
            numBought = a.getInt(R.styleable.CustomButton_numberPurchased, 0);
            cost = a.getFloat(R.styleable.CustomButton_costEach, 1.0f);
            mText = a.getString(R.styleable.CustomButton_itemName);
        } finally {
            a.recycle();
        }
        if (mText == null) {
            mText = "STEAMING HOT TOFU";
        }
        init();
    }

    public boolean getShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
    }

    public void init() {
        //setScaleType(ScaleType.FIT_CENTER);
        trashButton = BitmapFactory.decodeResource(getResources(), R.drawable.trashbutton);
        numBought = 0;
        paint.setColor(Color.BLACK);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5.0f);
    }

    public String getText() {
        return mText;
    }

    public void setText(String inText) {
        mText = inText;
        invalidate();
    }

    public int getmColor() {
        return mColor;
    }

    public void setmTextColor(int inColor) {
        mColor = inColor;
        invalidate();
    }

    @Override
         public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > getWidth()/2.0f) {
                    numBought++;
                } else {
                    if (numBought > 0)
                        numBought--;
                }
                return true;
        }
        invalidate();
        return false;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        trashButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.trashbutton), (int)(w), (int)(h), false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = canvas.getHeight();
        int w = canvas.getWidth();

        paint.setColor(Color.parseColor("#9DA5A1"));
        canvas.drawRect(0, 0, w, h * 0.7f, paint); //Main background
        paint.setColor(Color.parseColor("#C42F25"));
        canvas.drawRect(0, h * 0.7f, w / 2.0f, h, paint); //Minus button
        paint.setColor(Color.parseColor("#8DD85B"));
        canvas.drawRect(w / 2.0f, h * 0.7f, w, h, paint); //Plus button
//        paint.setColor(Color.parseColor("#B0E0E6"));
//        canvas.drawCircle(w/2.0f, 0.63f*h-(0.175f*0.5f*w), 0.1f*h, paint);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(0.175f * w);
        canvas.drawText(mText, w / 2.0f, 1.2f * h / 6.0f, paint); //Item name
        canvas.drawText("$" + numberFormat.format(cost), w / 2.0f, 2.5f*h / 6.0f, paint); //Price
        canvas.drawText(String.valueOf(numBought), w/2.0f, 3.8f*h/6.0f, paint); //Quantity
        paint.setStrokeWidth(0.025f * w);
        canvas.drawLine(0, (int) (0.7f * h), w, (int) (0.7f * h), paint); //Separated buttons from rest of view
        canvas.drawLine(w/2.0f, (int)(0.7f*h), w/2.0f, h, paint); //Splits buttons
        canvas.drawLine(0, 0, w, 0, paint); //Border - top
        canvas.drawLine(0, 0, 0, h, paint); //Border - left
        canvas.drawLine(0, h, w, h, paint); //Border - bottom
        canvas.drawLine(w, 0, w, h, paint); //Border - right
        paint.setStrokeWidth(0.04f*w);
        canvas.drawLine(0.20f*w, 0.85f*h, 0.30f*w, 0.85f*h, paint); //Minus
        paint.setStrokeWidth(0.028f*w);
        canvas.drawLine(0.70f*w, 0.85f*h, 0.80f*w, 0.85f*h, paint); //Horizontal plus stroke
        canvas.drawLine(0.75f*w, 0.80f*h, 0.75f*w, 0.90f*h, paint); //Vertical plus stroke
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) trashButton.getWidth();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) trashButton.getHeight();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Keep the view squared (or circled!)
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (w == 0 || h == 0) {
            setMeasuredDimension(0, 0);
        } else if (w < h) {
            setMeasuredDimension(w, (int)(1.2f*w));
        } else {
            setMeasuredDimension((int)(h*0.83f), h);
        }
    }

    public int getNumBought() {
        return numBought;
    }

    public void setNumBought(int numBought) {
        this.numBought = numBought;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }
}
