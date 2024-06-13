package com.automotive.simulation;

public class AssemblyEvent extends Event {
    private FinishedProduct product;
    private Machine machine;
    private Worker worker;

    public AssemblyEvent(double time, FinishedProduct product, Machine machine, Worker worker) {
        super(time);
        this.product = product;
        this.machine = machine;
        this.worker = worker;
    }

    @Override
    public void process() {
        // get the simulation instance
        Simulation simulation = Simulation.getInstance();

        if (!worker.getShift().isWithinShift(getTime())) {
            // if the worker is not within the shift, wait until the start of the shift
            double nextEventTime = worker.getShift().getStartTime();
            if (simulation.shouldContinueSimulation(nextEventTime)) {// check if the simulation should continue
                simulation.scheduleEvent(new AssemblyEvent(nextEventTime, product, machine, worker));
            }
            return;
        }

        if (machine.isBroken()) {// check if the machine is broken
            // if the machine is broken, wait for the maintenance time and schedule the machine repair event
            double repairTime = getTime() + machine.getMaintenanceTime();// get the maintenance time
            if (simulation.shouldContinueSimulation(repairTime)) {
                simulation.scheduleEvent(new MachineRepairEvent(repairTime, machine));// schedule the machine repair event
            }
            return;
        }

        // check the probability of the machine failure
        if (Math.random() < machine.getFailureRate()) {
            machine.setBroken(true);// set the machine as broken
            double breakdownTime = getTime();
            if (simulation.shouldContinueSimulation(breakdownTime)) {// check if the simulation should continue
                simulation.scheduleEvent(new MachineBreakdownEvent(breakdownTime, machine));// schedule the machine breakdown event
            }
            return;
        }

        simulation.removeProcessingProduct(product.getName());
        simulation.addAssemblyProduct(product.getName());

        double nextEventTime = getTime() + machine.getProcessingTime(); // assembly time
        if (simulation.shouldContinueSimulation(nextEventTime)) {
            simulation.scheduleEvent(new QualityControlEvent(nextEventTime, product, simulation.getQualityControlMachine(), simulation.getQualityControlWorker()));// continue the process loop
        }
    }
}