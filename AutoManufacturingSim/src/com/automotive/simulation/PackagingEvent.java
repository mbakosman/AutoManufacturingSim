package com.automotive.simulation;

public class PackagingEvent extends Event {
    private FinishedProduct product;
    private Machine machine;
    private Worker worker;

    public PackagingEvent(double time, FinishedProduct product, Machine machine, Worker worker) {
        super(time);
        this.product = product;
        this.machine = machine;
        this.worker = worker;
    }

    @Override
    public void process() {
        Simulation simulation = Simulation.getInstance();// get simulation instance

        if (!worker.getShift().isWithinShift(getTime())) {
            // if worker is not within  shift, wait until  start of shift
            double nextEventTime = worker.getShift().getStartTime();
            if (simulation.shouldContinueSimulation(nextEventTime)) {
                simulation.scheduleEvent(new PackagingEvent(nextEventTime, product, machine, worker));// schedule the packaging event
            }
            return;
        }

        if (machine.isBroken()) {// check if machine is broken
            // if machine is broken, wait for maintenance time and schedule machine repair event
            double repairTime = getTime() + machine.getMaintenanceTime();
            if (simulation.shouldContinueSimulation(repairTime)) {
                simulation.scheduleEvent(new MachineRepairEvent(repairTime, machine));// schedule machine repair event
            }
            return;
        }

        // check probability of machine failure
        if (Math.random() < machine.getFailureRate()) {
            machine.setBroken(true);
            double breakdownTime = getTime();// get breakdown time
            if (simulation.shouldContinueSimulation(breakdownTime)) {
                simulation.scheduleEvent(new MachineBreakdownEvent(breakdownTime, machine));// schedule machine breakdown event
            }
            return;
        }

        simulation.removeQualityControlProduct(product.getName());
        simulation.addPackagedProduct(product.getName());

        double nextEventTime = getTime() + machine.getProcessingTime(); // packaging time
        if (simulation.shouldContinueSimulation(nextEventTime)) {
            simulation.scheduleEvent(new PackagingEvent(nextEventTime, product, machine, worker)); // continue process loop
        }
    }
}