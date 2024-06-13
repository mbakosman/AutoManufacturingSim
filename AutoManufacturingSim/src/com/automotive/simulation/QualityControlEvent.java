package com.automotive.simulation;

public class QualityControlEvent extends Event {
    private FinishedProduct product;
    private Machine machine;
    private Worker worker;

    public QualityControlEvent(double time, FinishedProduct product, Machine machine, Worker worker) {
        super(time);
        this.product = product;
        this.machine = machine;
        this.worker = worker;
    }

    @Override
    public void process() {
        Simulation simulation = Simulation.getInstance();// get simulation instance

        if (!worker.getShift().isWithinShift(getTime())) {
            //if worker is not within shift, wait until start of shift
            double nextEventTime = worker.getShift().getStartTime();
            if (simulation.shouldContinueSimulation(nextEventTime)) {
                simulation.scheduleEvent(new QualityControlEvent(nextEventTime, product, machine, worker));// schedule quality control event
            }
            return;
        }

        if (machine.isBroken()) {
            // check if machine is broken
            double repairTime = getTime() + machine.getMaintenanceTime();// get maintenance time
            if (simulation.shouldContinueSimulation(repairTime)) {
                simulation.scheduleEvent(new MachineRepairEvent(repairTime, machine));// schedule the machine repair event
            }
            return;
        }

        // check the probability of machine failure
        if (Math.random() < machine.getFailureRate()) {
            machine.setBroken(true);
            double breakdownTime = getTime();
            if (simulation.shouldContinueSimulation(breakdownTime)) {// check if simulation should continue
                simulation.scheduleEvent(new MachineBreakdownEvent(breakdownTime, machine));// schedule the machine breakdown event
            }
            return;
        }

        simulation.removeAssemblyProduct(product.getName());
        simulation.addQualityControlProduct(product.getName());

        double nextEventTime = getTime() + machine.getProcessingTime(); // quality control time
        if (simulation.shouldContinueSimulation(nextEventTime)) {
            simulation.scheduleEvent(new PackagingEvent(nextEventTime, product, simulation.getPackagingMachine(), simulation.getPackagingWorker()));// continue the process loop
        }
    }
}