package com.automotive.simulation;

public class ProcessingEvent extends Event {
    private FinishedProduct product;
    private Machine machine;
    private Worker worker;

    public ProcessingEvent(double time, FinishedProduct product, Machine machine, Worker worker) {
        super(time);
        this.product = product;
        this.machine = machine;
        this.worker = worker;
    }

    @Override
    public void process() {
        Simulation simulation = Simulation.getInstance();// get simulation instance

        if (!worker.getShift().isWithinShift(getTime())) {
            // if worker is not within shift, wait until start of shift
            double nextEventTime = worker.getShift().getStartTime();
            if (simulation.shouldContinueSimulation(nextEventTime)) {
                simulation.scheduleEvent(new ProcessingEvent(nextEventTime, product, machine, worker));// schedule processing event
            }
            return;
        }

        if (machine.isBroken()) {
            // check if machine is broken
            double repairTime = getTime() + machine.getMaintenanceTime();// get maintenance time
            if (simulation.shouldContinueSimulation(repairTime)) {
                simulation.scheduleEvent(new MachineRepairEvent(repairTime, machine));// schedule machine repair event
            }
            return;
        }

        // check probability of machine failure
        if (Math.random() < machine.getFailureRate()) {
            machine.setBroken(true);
            double breakdownTime = getTime();
            if (simulation.shouldContinueSimulation(breakdownTime)) {// check if simulation should continue
                simulation.scheduleEvent(new MachineBreakdownEvent(breakdownTime, machine));// schedule machine breakdown event
            }
            return;
        }

        simulation.removeRawMaterial(product.getName());
        simulation.addProcessingProduct(product.getName());

        double nextEventTime = getTime() + machine.getProcessingTime(); // processing time
        if (simulation.shouldContinueSimulation(nextEventTime)) {// check if simulation should continue
            simulation.scheduleEvent(new AssemblyEvent(nextEventTime, product, simulation.getAssemblyMachine(), simulation.getAssemblyWorker()));// continue the process loop
        }
    }
}