package com.example.gabe.gabe_first_test;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.content.Context;

/**
 * Created by Gabe on 2/1/2015.
 */
public class EvilBirdClass extends Actor {
    BirdClass birb;
    float dX, dY;
    boolean ateBird;

    EvilBirdClass(Context context, BirdClass bird) {
        birb = bird;
        dX = bird.getX()/50.0f;
        dY = bird.getY()/50.0f;
        x = 0;
        y = 0;
        ateBird = false;
        if (pic == null) {
            pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.evilbird);
        }
    }

    public void Update() {
        if (x < 0 || x > 0.95 || y < 0 || y > 0.95) {
            dX = (birb.getX() - x) / (Math.abs(birb.getX() - x) + Math.abs(birb.getY() - y))/50.0f;
            dY = (birb.getY() - y) / (Math.abs(birb.getX() - x) + Math.abs(birb.getY() - y))/50.0f;
        }
        x += dX;
        y += dY;
    }

    public void Collide(Actor other) {
        if (other instanceof BirdClass) {
            dX = -dX;
            dY = -dY;
            birb.Bounce();
        } else if (other instanceof FoodClass) {
            ((FoodClass) other).Eat();
        }
    }

    public boolean gameOver() { return ateBird; }
}
