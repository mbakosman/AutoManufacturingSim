package com.automotive.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulation {
    private static Simulation instance;
    private PriorityQueue<Event> eventQueue;
    private double currentTime;
    private double simulationEndTime = 100.0; // simulation end time

    // performance datas
    private Map<String, Integer> rawMaterialQueue;
    private Map<String, Integer> processingQueue;
    private Map<String, Integer> assemblyQueue;
    private Map<String, Integer> qualityControlQueue;
    private Map<String, Integer> packagingQueue;
    private Map<String, Double> machineWorkTimeMap;
    private Map<String, Double> workerWorkTimeMap;

    // machines and workers
    private Machine processingMachine;
    private Worker processingWorker;
    private Machine assemblyMachine;
    private Worker assemblyWorker;
    private Machine qualityControlMachine;
    private Worker qualityControlWorker;
    private Machine packagingMachine;
    private Worker packagingWorker;

    private Simulation(int processingMachines, int processingWorkers, int assemblyMachines, int assemblyWorkers, int qualityControlMachines, int qualityControlWorkers, int packagingMachines, int packagingWorkers) {
        this.eventQueue = new PriorityQueue<>();
        this.currentTime = 0.0;
        this.rawMaterialQueue = new HashMap<>();
        this.processingQueue = new HashMap<>();
        this.assemblyQueue = new HashMap<>();
        this.qualityControlQueue = new HashMap<>();
        this.packagingQueue = new HashMap<>();
        this.machineWorkTimeMap = new HashMap<>();
        this.workerWorkTimeMap = new HashMap<>();

        // machines and workers
        this.processingMachine = new Machine("Processing Machine", 4, 0.05, 8, processingMachines); // base processing time 4 units, failure rate 0.05, maintenance time 8 units
        this.processingWorker = new Worker("Processing Worker", new Shift("Day Shift", 0, 12), processingWorkers);
        this.assemblyMachine = new Machine("Assembly Machine", 6, 0.08, 12, assemblyMachines); // base processing time 6 units, failure rate 0.08, maintenance time 12 units
        this.assemblyWorker = new Worker("Assembly Worker", new Shift("Day Shift", 0, 12), assemblyWorkers);
        this.qualityControlMachine = new Machine("Quality Control Machine", 5, 0.07, 10, qualityControlMachines); // base processing time 5 units, failure rate 0.07, maintenance time 10 units
        this.qualityControlWorker = new Worker("Quality Control Worker", new Shift("Day Shift", 0, 12), qualityControlWorkers);
        this.packagingMachine = new Machine("Packaging Machine", 3, 0.04, 6, packagingMachines); // base processing time 3 units, failure rate 0.04, maintenance time 6 units
        this.packagingWorker = new Worker("Packaging Worker", new Shift("Day Shift", 0, 12), packagingWorkers);
    }

    // Singleton pattern
    public static Simulation getInstance(int processingMachines, int processingWorkers, int assemblyMachines, int assemblyWorkers, int qualityControlMachines, int qualityControlWorkers, int packagingMachines, int packagingWorkers) {
        if (instance == null) {
            instance = new Simulation(processingMachines, processingWorkers, assemblyMachines, assemblyWorkers, qualityControlMachines, qualityControlWorkers, packagingMachines, packagingWorkers);
        }
        return instance;
    }

    // Singleton pattern
    public static Simulation getInstance() {
        return instance;
    }

    // Schedule an event for simulation
    public void scheduleEvent(Event event) {
        if (event.getTime() < simulationEndTime) {// check if event time is before simulation end time
            eventQueue.add(event);
        }
    }

    // Start simulation
    public void run() {
        printLogHeader();
        while (!eventQueue.isEmpty() && currentTime < simulationEndTime) { // check if there are events in queue and current time is before simulation end time
            Event event = eventQueue.poll();
            currentTime = event.getTime(); // update current time
            event.process();// process event
            logCurrentState();// log current state
        }
        analyzePerformance();// analyze performance
    }

    // Check if simulation should continue
    public boolean shouldContinueSimulation(double nextEventTime) {
        return nextEventTime < simulationEndTime;// check if next event time is before simulation end time
    }


    // Add raw material to simulation
    public void addRawMaterial(String productName) {
        rawMaterialQueue.put(productName, rawMaterialQueue.getOrDefault(productName, 0) + 1);
        scheduleEvent(new ProcessingEvent(currentTime, new FinishedProduct(productName), processingMachine, processingWorker));
    }

    // Add processing product to simulation
    public void addProcessingProduct(String productName) {
        processingQueue.put(productName, processingQueue.getOrDefault(productName, 0) + 1);
    }

    // Add assembly product to simulation
    public void addAssemblyProduct(String productName) {
        assemblyQueue.put(productName, assemblyQueue.getOrDefault(productName, 0) + 1);
    }

    // Add quality control product to simulation
    public void addQualityControlProduct(String productName) {
        qualityControlQueue.put(productName, qualityControlQueue.getOrDefault(productName, 0) + 1);
    }

    // Add packaged product to simulation
    public void addPackagedProduct(String productName) {
        packagingQueue.put(productName, packagingQueue.getOrDefault(productName, 0) + 1);
    }

    // Remove raw material from simulation
    public void removeRawMaterial(String productName) {
        int currentCount = rawMaterialQueue.getOrDefault(productName, 0);
        if (currentCount > 0) {// check if raw material count is greater than 0
            rawMaterialQueue.put(productName, currentCount - 1);
        }
    }

    // Remove processing product from simulation
    public void removeProcessingProduct(String productName) {
        int currentCount = processingQueue.getOrDefault(productName, 0);
        if (currentCount > 0) {// check if processing product count is greater than 0
            processingQueue.put(productName, currentCount - 1);
        }
    }

    // Remove assembly product from simulation
    public void removeAssemblyProduct(String productName) {
        int currentCount = assemblyQueue.getOrDefault(productName, 0);
        if (currentCount > 0) {// check if assembly product count is greater than 0
            assemblyQueue.put(productName, currentCount - 1);
        }
    }

    // Remove quality control product from simulation
    public void removeQualityControlProduct(String productName) {
        int currentCount = qualityControlQueue.getOrDefault(productName, 0);
        if (currentCount > 0) {// check if quality control product count is greater than 0
            qualityControlQueue.put(productName, currentCount - 1);
        }
    }

    // print the log header
    private void printLogHeader() {
        System.out.println("_________________________________________________________________________________________________________________");
        System.out.printf("| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s |%n", "Time", "Raw Material", "Processing", "Assembly", "Quality Control", "Packaging");
        System.out.println("|------------|----------------------|----------------------|----------------------|----------------------|----------------------|");
    }

    // log the current state
    private void logCurrentState() {
        String rawMaterialLog = logQueueState(rawMaterialQueue);
        String processingLog = logQueueState(processingQueue);
        String assemblyLog = logQueueState(assemblyQueue);
        String qualityControlLog = logQueueState(qualityControlQueue);
        String packagingLog = logQueueState(packagingQueue);
        System.out.printf("| %-10.2f | %-20s | %-20s | %-20s | %-20s | %-20s |%n", currentTime, rawMaterialLog, processingLog, assemblyLog, qualityControlLog, packagingLog);
    }

    // log the queue state
    private String logQueueState(Map<String, Integer> queue) {
        StringBuilder log = new StringBuilder();
        for (Map.Entry<String, Integer> entry : queue.entrySet()) {
            log.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
        }
        return log.toString().trim();
    }

    // analyze the performance and optimize the resource allocation
    public void analyzePerformance() {
        System.out.println("_____________________________________________________________");
        System.out.println("Performance Analysis:");
        System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |%n", "Product", "Raw Material Count", "Processing Count", "Assembly Count", "Q. Control Count", "Packaging Count");
        System.out.println("|----------------------|----------------------|----------------------|----------------------|----------------------|----------------------|");

        for (Map.Entry<String, Integer> entry : rawMaterialQueue.entrySet()) {
            String productName = entry.getKey();
            int rawMaterialCount = entry.getValue();
            int processingCount = processingQueue.getOrDefault(productName, 0);
            int assemblyCount = assemblyQueue.getOrDefault(productName, 0);
            int qualityControlCount = qualityControlQueue.getOrDefault(productName, 0);
            int packagedCount = packagingQueue.getOrDefault(productName, 0);

            System.out.printf("| %-20s | %-20d | %-20d | %-20d | %-20d | %-20d |%n", productName, rawMaterialCount, processingCount, assemblyCount, qualityControlCount, packagedCount);
        }
        System.out.println("_____________________________________________________________");

        identifyBottlenecks();
        optimizeResourceAllocation();
    }


    // find bottlenecks in the simulation
    private void identifyBottlenecks() {
        System.out.println("_____________________________________________________________");
        System.out.println("Identifying Bottlenecks:");

        int maxQueueSize = 0;
        String bottleneckStage = "";

        if (getMaxQueueSize(rawMaterialQueue) > maxQueueSize) {
            maxQueueSize = getMaxQueueSize(rawMaterialQueue);
            bottleneckStage = "Raw Material";
        }
        if (getMaxQueueSize(processingQueue) > maxQueueSize) {
            maxQueueSize = getMaxQueueSize(processingQueue);
            bottleneckStage = "Processing";
        }
        if (getMaxQueueSize(assemblyQueue) > maxQueueSize) {
            maxQueueSize = getMaxQueueSize(assemblyQueue);
            bottleneckStage = "Assembly";
        }
        if (getMaxQueueSize(qualityControlQueue) > maxQueueSize) {
            maxQueueSize = getMaxQueueSize(qualityControlQueue);
            bottleneckStage = "Quality Control";
        }
        if (getMaxQueueSize(packagingQueue) > maxQueueSize) {
            maxQueueSize = getMaxQueueSize(packagingQueue);
            bottleneckStage = "Packaging";
        }

        System.out.println("Bottleneck Stage: " + bottleneckStage);
        System.out.println("Max Queue Size: " + maxQueueSize);
        System.out.println("_____________________________________________________________");
    }

    // get the maximum queue size
    private int getMaxQueueSize(Map<String, Integer> queue) {
        int maxQueueSize = 0;
        for (int count : queue.values()) {
            if (count > maxQueueSize) {
                maxQueueSize = count;
            }
        }
        return maxQueueSize;
    }



    // optimize the resource allocation
    private void optimizeResourceAllocation() {
        System.out.println("Optimizing Resource Allocation:");

        // use performance data to optimize the resource allocation
        int rawMaterialQueueSize = getMaxQueueSize(rawMaterialQueue);
        int processingQueueSize = getMaxQueueSize(processingQueue);
        int assemblyQueueSize = getMaxQueueSize(assemblyQueue);
        int qualityControlQueueSize = getMaxQueueSize(qualityControlQueue);
        int packagingQueueSize = getMaxQueueSize(packagingQueue);

        // current machine and worker counts
        int currentProcessingMachines = processingMachine.getMachineCount();
        int currentProcessingWorkers = processingWorker.getWorkerCount();
        int currentAssemblyMachines = assemblyMachine.getMachineCount();
        int currentAssemblyWorkers = assemblyWorker.getWorkerCount();
        int currentQualityControlMachines = qualityControlMachine.getMachineCount();
        int currentQualityControlWorkers = qualityControlWorker.getWorkerCount();
        int currentPackagingMachines = packagingMachine.getMachineCount();
        int currentPackagingWorkers = packagingWorker.getWorkerCount();

        // total machine and worker counts
        int totalMachines = currentProcessingMachines + currentAssemblyMachines + currentQualityControlMachines + currentPackagingMachines;
        int totalWorkers = currentProcessingWorkers + currentAssemblyWorkers + currentQualityControlWorkers + currentPackagingWorkers;

        // bottleneck optimized machine and worker counts
        int optimalProcessingMachines = currentProcessingMachines;
        int optimalProcessingWorkers = currentProcessingWorkers;
        int optimalAssemblyMachines = currentAssemblyMachines;
        int optimalAssemblyWorkers = currentAssemblyWorkers;
        int optimalQualityControlMachines = currentQualityControlMachines;
        int optimalQualityControlWorkers = currentQualityControlWorkers;
        int optimalPackagingMachines = currentPackagingMachines;
        int optimalPackagingWorkers = currentPackagingWorkers;

        // find the bottleneck and increase the machine and worker counts
        if (rawMaterialQueueSize > processingQueueSize) {
            optimalProcessingMachines++;
            optimalProcessingWorkers++;
        }
        if (processingQueueSize > assemblyQueueSize) {
            optimalAssemblyMachines++;
            optimalAssemblyWorkers++;
        }
        if (assemblyQueueSize > qualityControlQueueSize) {
            optimalQualityControlMachines++;
            optimalQualityControlWorkers++;
        }
        if (qualityControlQueueSize > packagingQueueSize) {
            optimalPackagingMachines++;
            optimalPackagingWorkers++;
        }

        // check total machine and worker counts
        int totalOptimalMachines = optimalProcessingMachines + optimalAssemblyMachines + optimalQualityControlMachines + optimalPackagingMachines;
        int totalOptimalWorkers = optimalProcessingWorkers + optimalAssemblyWorkers + optimalQualityControlWorkers + optimalPackagingWorkers;

        int machineDifference = totalMachines - totalOptimalMachines;
        int workerDifference = totalWorkers - totalOptimalWorkers;

        //optimize the machine and worker counts
        if (machineDifference > 0) {
            optimalProcessingMachines += machineDifference / 4;
            optimalAssemblyMachines += machineDifference / 4;
            optimalQualityControlMachines += machineDifference / 4;
            optimalPackagingMachines += machineDifference / 4;
        }

        if (workerDifference > 0) {
            optimalProcessingWorkers += workerDifference / 4;
            optimalAssemblyWorkers += workerDifference / 4;
            optimalQualityControlWorkers += workerDifference / 4;
            optimalPackagingWorkers += workerDifference / 4;
        }

        System.out.printf("| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |%n", "Stage", "Current Machines", "Current Workers", "Optimal Machines", "Optimal Workers", "Difference");
        System.out.println("|----------------------|----------------------|----------------------|----------------------|----------------------|----------------------|");

        printResourceAllocation("Processing", currentProcessingMachines, currentProcessingWorkers, optimalProcessingMachines, optimalProcessingWorkers);
        printResourceAllocation("Assembly", currentAssemblyMachines, currentAssemblyWorkers, optimalAssemblyMachines, optimalAssemblyWorkers);
        printResourceAllocation("Quality Control", currentQualityControlMachines, currentQualityControlWorkers, optimalQualityControlMachines, optimalQualityControlWorkers);
        printResourceAllocation("Packaging", currentPackagingMachines, currentPackagingWorkers, optimalPackagingMachines, optimalPackagingWorkers);

        System.out.println("_____________________________________________________________");
    }

    private void printResourceAllocation(String stage, int currentMachines, int currentWorkers, int optimalMachines, int optimalWorkers) {
        int machineDifference = optimalMachines - currentMachines;
        int workerDifference = optimalWorkers - currentWorkers;

        System.out.printf("| %-20s | %-20d | %-20d | %-20d | %-20d | %-20s |%n", stage, currentMachines, currentWorkers, optimalMachines, optimalWorkers, machineDifference + " machines, " + workerDifference + " workers");
    }


    public double getCurrentTime() {
        return currentTime;
    }

    public Machine getProcessingMachine() {
        return processingMachine;
    }

    public Worker getProcessingWorker() {
        return processingWorker;
    }

    public Machine getAssemblyMachine() {
        return assemblyMachine;
    }

    public Worker getAssemblyWorker() {
        return assemblyWorker;
    }

    public Machine getQualityControlMachine() {
        return qualityControlMachine;
    }

    public Worker getQualityControlWorker() {
        return qualityControlWorker;
    }

    public Machine getPackagingMachine() {
        return packagingMachine;
    }

    public Worker getPackagingWorker() {
        return packagingWorker;
    }
}
