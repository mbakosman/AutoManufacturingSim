package com.automotive.simulation.test;

import com.automotive.simulation.FinishedProduct;
import com.automotive.simulation.RawMaterialArrivalEvent;
import com.automotive.simulation.Simulation;

public class SingleProduct {
    public static void main(String[] args) {
        // create a simulation
    	Simulation simulation = Simulation.getInstance(3, 6, 2, 4, 2, 3, 3, 5);
 // machine and worker numbers

        // create raw materials initially and schedule raw material arrival events at regular intervals
        double initialArrivalInterval = 5.0; // per 5 units of time, add new raw materials
        FinishedProduct product = new FinishedProduct("Single Product");
        for (int i = 0; i < 10; i++) { // add 10 raw materials initially
            simulation.addRawMaterial(product.getName());
        }
        simulation.scheduleEvent(new RawMaterialArrivalEvent(0, product, initialArrivalInterval, simulation.getProcessingMachine(), simulation.getProcessingWorker()));// schedule raw material arrival events

        // start the simulation
        simulation.run();
    }
}