package com.automotive.simulation;

public class Machine {
    private String id;
    private double baseProcessingTime;
    private double failureRate;
    private double maintenanceTime;
    private boolean isBroken;
    private int machineCount;

    public Machine(String id, double baseProcessingTime, double failureRate, double maintenanceTime, int machineCount) {
        this.id = id;
        this.baseProcessingTime = baseProcessingTime;
        this.failureRate = failureRate;
        this.maintenanceTime = maintenanceTime;
        this.isBroken = false;
        this.machineCount = machineCount;
    }

    public String getId() {
        return id;
    }

    public double getBaseProcessingTime() {
        return baseProcessingTime;
    }

    public double getProcessingTime() {
        return baseProcessingTime / machineCount; // Divide  base processing time by  number of machines
    }

    public double getFailureRate() {
        return failureRate;
    }

    public double getMaintenanceTime() {
        return maintenanceTime;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean isBroken) {
        this.isBroken = isBroken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBaseProcessingTime(double baseProcessingTime) {
        this.baseProcessingTime = baseProcessingTime;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }

    public void setMaintenanceTime(double maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public int getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(int machineCount) {
        this.machineCount = machineCount;
    }
}