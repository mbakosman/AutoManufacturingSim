package com.automotive.simulation;

public class Shift {
    private String shiftName;
    private double startTime;
    private double endTime;

    public Shift(String shiftName, double startTime, double endTime) {
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getShiftName() {
        return shiftName;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public boolean isWithinShift(double time) {
        return time >= startTime && time < endTime;
    }
}