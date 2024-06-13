package com.automotive.simulation;

public class MachineRepairEvent extends Event {
    private Machine machine;

    public MachineRepairEvent(double time, Machine machine) {
        super(time);
        this.machine = machine;
    }

    @Override
    public void process() {
        if (Simulation.getInstance().shouldContinueSimulation(getTime())) {// check if simulation should continue
            machine.setBroken(false);
        }
    }
}