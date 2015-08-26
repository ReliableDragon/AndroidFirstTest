package com.example.gabe.gabe_first_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;

/**
 * Created by Gabe on 1/26/2015.
 */
public class BirdClass extends Actor {
    private final int id = 0;
    private float dx,dy,ddy,ddx;
    static Bitmap pic;
    int foodEaten;

    BirdClass(Context context) {
        if (pic == null) {
            pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.birb);
        }
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
        ddy = 0;
        ddx = 0;
        foodEaten = 0;
    }

    public float getX() {
        return x;
    }

    BirdClass(Context context, float inX, float inY) {
        if (pic == null) {
            pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.birb);
        }
        x = inX;
        y = inY;
        dx = 0;
        dy = 0;
        ddy = 0;
        ddx = 0;
        foodEaten = 0;
    }

    public float getY() {
        return y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public float getDdy() { return ddy; }

    public float getDdx() { return ddx; }

    public int getFoodEaten() {return foodEaten;}

//    public void Goto(float tX, float tY) {
//        if (tX > x + 0.03f)
//            ddx += 0.025f;
//        else if (tX < x - 0.01f)
//            ddx -= 0.025f;
//        else dx = tX - x;
//        /*if (tY < y && dy < 15) {*/
//            dy = 0.035f;
//            ddy = 0;
//       /* }*/
//    }

    public void jump() {
        dy = 0.035f;
        ddy = 0;
    }

//    public void slideLeft(float force) {
//        if (dx >= 0 && dx < 0.1f) {
//            dx += 0.03f;
//        } else {
//            dx = 0.03f;
//        }
//    }
//
//    public void slideRight(float force) {
////        if (dx <= 0 && dx > -0.1f) {
////            dx -= 0.03f;
////        } else {
////            dx = -0.03f;
////        }
//        dx = force/2.0f;
//    }

    public void slide(float force) {
        dx = -1.0f*force/11.5f;
    }

    public void slideStop() {
        dx = 0;
    }

    public void Update(/*Canvas canvas*/) {
        y -= dy;
        dx *= 0.8;
        x += dx;
        if (dx < 0.001f && dx > -0.001f) {
            dx = 0;
        }
        if (x < 0) {
            x = 0;
            dx = -dx;
        } else if (x > 0.95f) {
            x = 0.95f;
            dx = -dx;
        }
        if (y < 0.95f) {
            ddy -= 0.0015;
            dy += ddy;
        } else {
            y = 0.95f;
            if (ddy < 0)
                ddy = 0;
            if (dy < 0.02f && dy > -0.02f) {
                dy = 0;
            } else dy = -dy * 0.2f;
        }
    }

    public void Chomp(FoodClass food) {
        if (food.getX() < x + 0.05f && food.getX() > x - 0.02f && food.getY() < y + 0.05f && food.getY() > y - 0.02f) {
            food.Eat();
            foodEaten++;
        }
    }

    public void Collide(FoodClass fruit) {
        foodEaten++;
    }

    public void Collide(Actor obj) {
        if (obj instanceof FoodClass) {
            foodEaten++;
        }
    }

    public int getID() {
        return id;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void Die() { cleanUp = true; }

    public void Bounce() {
        dx = 0;
        dy = -0.3f;
        ddy = 0;
    }
}
