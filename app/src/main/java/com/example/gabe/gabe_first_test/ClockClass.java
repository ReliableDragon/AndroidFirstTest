package com.example.gabe.gabe_first_test;

/**
 * Created by Gabe on 1/27/2015.
 */
public class ClockClass {
    int timeLeft;

    ClockClass() {
        timeLeft = 15;
    }

    ClockClass(int inTime) {
        timeLeft = inTime;
    }

    public void Countdown() {
        timeLeft--;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int inTime) {
        timeLeft = inTime;
    }
}
