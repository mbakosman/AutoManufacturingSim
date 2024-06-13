package com.automotive.simulation;

public class MachineBreakdownEvent extends Event {
    private Machine machine;

    public MachineBreakdownEvent(double time, Machine machine) {
        super(time);
        this.machine = machine;
    }

    @Override
    public void process() {
        if (Simulation.getInstance().shouldContinueSimulation(getTime())) {// check if simulation should continue
            machine.setBroken(true);
            Simulation.getInstance().scheduleEvent(new MachineRepairEvent(getTime() + machine.getMaintenanceTime(), machine));// schedule machine repair event
        }
    }
}