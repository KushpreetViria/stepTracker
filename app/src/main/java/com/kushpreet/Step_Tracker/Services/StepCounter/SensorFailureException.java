package com.kushpreet.Step_Tracker.Services.StepCounter;

public class SensorFailureException extends RuntimeException {
    public SensorFailureException(String msg) {
        super(msg);
    }
}
