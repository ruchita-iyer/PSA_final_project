/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tsp;

/**
 *
 * @author ruchi
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPSimulatedAnnealing {
    public static int numCities;
    public static String[] cityIds;
    public static double[] cityLongitudes;
    public static double[] cityLatitudes;
    public static double[][] distances;
    public static final Random random = new Random();
    
    public static void main(String[] args) {
        // read input file
        String filename = "C:\\Users\\ruchi\\Downloads\\crimeActual.csv";
        readInputFile(filename);
        
        // initialize distance matrix
        initializeDistances();
        
        // set initial temperature and cooling rate
        double initialTemperature = 10000;
        double coolingRate = 0.003;
        
        // initialize current and best solutions
        int[] currentSolution = generateRandomSolution();
        int[] bestSolution = currentSolution.clone();
        
        // initialize current and best distances
        double currentDistance = calculateTotalDistance(currentSolution);
        double bestDistance = currentDistance;
        
        // main simulated annealing loop
        while (initialTemperature > 1) {
            // generate a neighbor solution
            int[] neighborSolution = generateNeighborSolution(currentSolution);
            double neighborDistance = calculateTotalDistance(neighborSolution);
            
            // decide whether to accept the neighbor solution
            if (acceptNeighbor(currentDistance, neighborDistance, initialTemperature)) {
                currentSolution = neighborSolution;
                currentDistance = neighborDistance;
            }
            
            // update the best solution if necessary
            if (currentDistance < bestDistance) {
                bestSolution = currentSolution.clone();
                bestDistance = currentDistance;
            }
            
            // cool the temperature
            initialTemperature *= 1 - coolingRate;
        }
        
        // print the best solution
        System.out.println("Best Solution:");
        for (int i = 0; i < numCities; i++) {
            System.out.println(cityIds[bestSolution[i]]+"-->");
        }
        System.out.println("Best Distance: " + bestDistance);
    }
    
    public static boolean acceptNeighbor(double currentDistance, double neighborDistance, double temperature) {
        if (neighborDistance < currentDistance) {
            return true;
        }
        double acceptanceProbability = Math.exp((currentDistance - neighborDistance) / temperature);
        return random.nextDouble() < acceptanceProbability;
    }
    
    public static void readInputFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<String> ids = new ArrayList<>();
            List<Double> longitudes = new ArrayList<>();
            List<Double> latitudes = new ArrayList<>();
            String line;
             boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                 if (firstLine) {
                firstLine = false;
                continue;
            }

                String[] values = line.split(",");
                String id = values[0];
                double longitude = Double.parseDouble(values[1]);
                double latitude = Double.parseDouble(values[2]);
                ids.add(id);
                longitudes.add(longitude);
                latitudes.add(latitude);
            }
            numCities = ids.size();
            cityIds = new String[numCities];
            cityLongitudes = new double[numCities];
            cityLatitudes = new double[numCities];
            for (int i = 0; i < numCities; i++) {
                cityIds[i] = ids.get(i);
                cityLongitudes[i] = longitudes.get(i);
                cityLatitudes[i] = latitudes.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
        public static void initializeDistances() {
        distances = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    double longitudeDiff = cityLongitudes[i] - cityLongitudes[j];
                    double latitudeDiff = cityLatitudes[i] - cityLatitudes[j];
                    double distance = Math.sqrt(longitudeDiff * longitudeDiff + latitudeDiff * latitudeDiff);
                    distances[i][j] = distance;
                    distances[j][i] = distance;
                }
            }
        }
    }
    
    public static int[] generateRandomSolution() {
        int[] solution = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            solution[i] = i;
        }
        for (int i = 0; i < numCities; i++) {
            int j = random.nextInt(numCities);
            int temp = solution[i];
            solution[i] = solution[j];
            solution[j] = temp;
        }
        return solution;
    }
    
    public static int[] generateNeighborSolution(int[] solution) {
        int[] neighbor = solution.clone();
        if(numCities>0){
        int i = random.nextInt(numCities);
        int j = random.nextInt(numCities);
        int temp = neighbor[i];
        neighbor[i] = neighbor[j];
        neighbor[j] = temp;}
        return neighbor;
    }
    
    public static double calculateTotalDistance(int[] solution) {
        double distance = 0;
        for (int i = 0; i < numCities - 1; i++) {
            distance += distances[solution[i]][solution[i+1]];
        }
        distance += distances[solution[numCities-1]][solution[0]];
        return distance;
    }
}
