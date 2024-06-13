package com.automotive.simulation;

public class RawMaterialArrivalEvent extends Event {
    private FinishedProduct product;
    private double arrivalInterval;
    private Machine machine;
    private Worker worker;

    public RawMaterialArrivalEvent(double time, FinishedProduct product, double arrivalInterval, Machine machine, Worker worker) {
        super(time);
        this.product = product;
        this.arrivalInterval = arrivalInterval;
        this.machine = machine;
        this.worker = worker;
    }

    @Override
    public void process() {
        Simulation.getInstance().addRawMaterial(product.getName());// add raw material to simulation

        // add next raw material arrival event
        double nextArrivalTime = getTime() + arrivalInterval;
        if (Simulation.getInstance().shouldContinueSimulation(nextArrivalTime)) {// check if simulation should continue
            Simulation.getInstance().scheduleEvent(new RawMaterialArrivalEvent(nextArrivalTime, product, arrivalInterval, machine, worker));// schedule next raw material arrival event
        }

        // schedule processing event
        Simulation.getInstance().scheduleEvent(new ProcessingEvent(getTime(), product, machine, worker));
    }
}