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
import java.util.Collections;
import java.util.Random;

public class TSPSimulatedAnnealing {
    // CSV file path
    private static final String FILE_PATH = "./resources/crimeActual.csv";

    // Simulated Annealing parameters
    private static final double INITIAL_TEMPERATURE = 10000.0;
    private static final double COOLING_RATE = 0.003;
    private static final int NUM_ITERATIONS = 1000;

    // Cities information
    private static final ArrayList<City> cities = new ArrayList<>();

    public static void main(String[] args) {

        // Read input CSV file
        readCSV(FILE_PATH);

        // Initialize current and best tour
        Tour currentTour = new Tour(cities);
        Tour bestTour = new Tour(currentTour.getCities());

        // Initialize temperature
        double temperature = INITIAL_TEMPERATURE;

        // Simulated Annealing algorithm
        while (temperature > 1) {
            for (int i = 0; i < NUM_ITERATIONS; i++) {

                // Create new tour by randomly swapping two cities
                Tour newTour = new Tour(currentTour.getCities());
                int cityIndex1 = getRandomIndex(cities.size());
                int cityIndex2 = getRandomIndex(cities.size());
                newTour.swapCities(cityIndex1, cityIndex2);

                // Calculate energy difference between current and new tour
                double currentEnergy = currentTour.getDistance();
                double newEnergy = newTour.getDistance();
                double deltaEnergy = newEnergy - currentEnergy;

                // Decide whether to accept the new tour based on Metropolis criterion
                if (deltaEnergy < 0 || Math.exp(-deltaEnergy / temperature) > Math.random()) {
                    currentTour = new Tour(newTour.getCities());
                }

                // Update best tour if necessary
                if (currentTour.getDistance() < bestTour.getDistance()) {
                    bestTour = new Tour(currentTour.getCities());
                }
            }

            // Decrease temperature
            temperature *= 1 - COOLING_RATE;
        }

        // Print the best tour
        //System.out.println("Best tour distance: " + bestTour.getDistance() + " meters");
        
        System.out.print("Best tour path: ");
        for (City city : bestTour.getCities()) {
            System.out.print(city.getId().substring(city.getId().length() - 5) + " -> ");
        }
        System.out.print(bestTour.getCities().get(0).getId().substring(bestTour.getCities().get(0).getId().length() - 5));
        System.out.format("\nTour length:: %.2f m %n", bestTour.getDistance());
    }

    // Helper method to read input CSV file
    private static void readCSV(String filePath) {
        BufferedReader br = null;
        String line = "";
        String csvDelimiter = ",";
        boolean firstLine = true;
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                firstLine = false;
                continue;
            }
                String[] cityData = line.split(csvDelimiter);
                City city = new City(cityData[0], Double.parseDouble(cityData[1]), Double.parseDouble(cityData[2]));
                cities.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method to get a random index within a given range
    public static int getRandomIndex(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }


public static class City {

    private final String id;
    private final double latitude;
    private final double longitude;

    public City(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

public static class Tour {

    private final ArrayList<City> cities;
    private double distance;

    public Tour(ArrayList<City> cities) {
        this.cities = cities;
        calculateDistance();
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public double getDistance() {
        return distance;
    }

// Helper method to calculate the total distance of the tour
    private void calculateDistance() {
        distance = 0.0;
        for (int i = 0; i < cities.size(); i++) {
            City currentCity = cities.get(i);
            City nextCity = cities.get((i + 1) % cities.size());
            distance += getDistanceBetweenCities(currentCity, nextCity);
        }
    }

// Helper method to calculate the distance between two cities
    public static double getDistanceBetweenCities(City city1, City city2) {
        double lat1 = city1.getLatitude();
        double lon1 = city1.getLongitude();
        double lat2 = city2.getLatitude();
        double lon2 = city2.getLongitude();
        double R = 6371.0; // Earth radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000;
    }

// Helper method to swap two cities in the tour
    public void swapCities(int index1, int index2) {
        Collections.swap(cities, index1, index2);
        calculateDistance();
    }
}
}
