package com.example.gabe.gabe_first_test;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Gabe on 1/26/2015.
 */
public class FoodClass extends Actor {
    final int id = 1;
    float x, y, speed;
    static Bitmap pic;

    FoodClass() { }

    FoodClass(Context context) {
        if (pic == null) {
            pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruit);
        }
        GenFood();
    }

    FoodClass(Context context, float inX, float inY) {
        if (pic == null) {
            pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruit);
        }
        x = inX;
        y = inY;
        speed = new Random().nextFloat()*0.016f + 0.004f;
    }

    public float getX() { return x; }

    public float getY() {
        return y;
    }

    private void GenFood(/*float gX, float gY*/) {
        Random rand = new Random();
        x = rand.nextFloat() * 0.8f + 0.1f;
//        y = rand.nextFloat() * 0.95f;
        y = 0;
        speed = rand.nextFloat()*0.016f + 0.004f;
    }

    public void Eat() {
        //GenFood();
        cleanUp = true;
    }

    public void Collide(BirdClass birb) {
        cleanUp = true;
    }
    public void Collide(Actor obj) {
        if (obj instanceof  BirdClass) {
            cleanUp = true;
        }
    }

    public int getID() {
        return id;
    }

    public Bitmap getPic() {return pic; }

    public void Update() {
        y += speed;
        if (y > 1.05) {
            cleanUp = true;
        }
    }
}
