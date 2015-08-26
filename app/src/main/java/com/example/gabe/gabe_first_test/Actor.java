package com.example.gabe.gabe_first_test;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabe on 1/27/2015.
 */
public class Actor implements Parcelable{
    int mData;
    float x,y;
    static Bitmap pic;
    boolean cleanUp;

    Actor() { cleanUp = false; }

    public void Collide(Actor other) {}

    public float getX() {return x;}

    public float getY() {return y;}

    public int getID() {return mData;}

    public Bitmap getPic() {return pic;}

    public int describeContents() {
        return 0;
    }

    /** save object in parcel */
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Actor> CREATOR
            = new Parcelable.Creator<Actor>() {
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };

    /** recreate object from parcel */
    private Actor(Parcel in) {
        mData = in.readInt();
    }

    public void Update() { }

    public boolean Done() { return cleanUp; }
}
