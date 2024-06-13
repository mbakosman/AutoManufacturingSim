package com.automotive.simulation;

public class Worker {
    private String name;
    private Shift shift;
    private int workerCount;

    public Worker(String name, Shift shift, int workerCount) {
        this.name = name;
        this.shift = shift;
        this.workerCount = workerCount;
    }

    public String getName() {
        return name;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}