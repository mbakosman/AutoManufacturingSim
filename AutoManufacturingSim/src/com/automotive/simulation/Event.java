package com.automotive.simulation;

// Abstract class for events
public abstract class Event implements Comparable<Event> {
    private double time;

    public Event(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public abstract void process();

    @Override
    public int compareTo(Event other) {// Compare events based on their time
        return Double.compare(this.time, other.time);
    }

    public void setTime(double time) {
        this.time = time;
    }
}
