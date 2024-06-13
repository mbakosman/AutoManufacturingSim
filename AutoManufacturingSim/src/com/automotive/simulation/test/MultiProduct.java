package com.automotive.simulation.test;

import com.automotive.simulation.FinishedProduct;
import com.automotive.simulation.RawMaterialArrivalEvent;
import com.automotive.simulation.Simulation;

import java.util.Arrays;
import java.util.List;

public class MultiProduct {
    public static void main(String[] args) {
        // create a simulation
    	Simulation simulation = Simulation.getInstance(3, 6, 2, 4, 2, 3, 3, 5); // machine and worker numbers

        // create a list of finished products
        List<FinishedProduct> products = Arrays.asList(
                new FinishedProduct("Product A"),
                new FinishedProduct("Product B"),
                new FinishedProduct("Product C")
        );

        // create raw materials initially and schedule raw material arrival events at regular intervals
        double initialArrivalInterval = 5.0; // per 5 units of time, add new raw materials
        for (FinishedProduct product : products) {
            for (int i = 0; i < 1000; i++) { // add 1000 raw materials initially
                simulation.addRawMaterial(product.getName());
            }
            simulation.scheduleEvent(new RawMaterialArrivalEvent(0, product, initialArrivalInterval, simulation.getProcessingMachine(), simulation.getProcessingWorker()));// schedule raw material arrival events
        }

        // start  simulation
        simulation.run();
    }
}