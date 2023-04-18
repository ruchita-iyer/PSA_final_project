/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ruchi
 */
public class TSPRandomSwapping {
// Define City class to store city details

    public static class City {

        private final String id;
        private final double latitude;
        private final double longitude;

        public City(String id, double latitude, double longitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        private double distanceTo(City city) {
            double R = 6371; // Earth's radius in kilometers
            double lat1 = Math.toRadians(this.latitude);
            double lon1 = Math.toRadians(this.longitude);
            double lat2 = Math.toRadians(city.latitude);
            double lon2 = Math.toRadians(city.longitude);

            double dlat = lat2 - lat1;
            double dlon = lon2 - lon1;

            double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.sin(dlon / 2) * Math.sin(dlon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return R * c * 1000;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }

    // Read CSV file and create city objects
    private static List<City> readCities(String filename) {
        List<City> cities = new ArrayList<>();
        try ( BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] fields = line.split(",");
                String id = fields[0];
                double lat = Double.parseDouble(fields[1]);
                double lon = Double.parseDouble(fields[2]);
                City city = new City(id, lat, lon);
                cities.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cities;
    }

    // Calculate the total distance of the tour
    public static double calculateDistance(List<City> tour) {
        double distance = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            distance += tour.get(i).distanceTo(tour.get(i + 1));
        }
        distance += tour.get(tour.size() - 1).distanceTo(tour.get(0)); // distance to return to start
        return distance;
    }

    // Create a random tour
    public static List<City> createTour(List<City> cities) {
        List<City> tour = new ArrayList<>(cities);
        Collections.shuffle(tour, new Random());
        return tour;
    }

    // Swap two cities in the tour randomly
    public static List<City> swapCities(List<City> tour) {
        int size = tour.size();
        int i = new Random().nextInt(size);
        int j = new Random().nextInt(size);
        Collections.swap(tour, i, j);
        return tour;
    }

    // Main function to solve TSP using random swapping method
    public static void main(String[] args) {
        String filename = "./resources/crimeActual.csv";
        List<City> cities = readCities(filename);

        List<City> tour = createTour(cities);
        double bestDistance = calculateDistance(tour);

        int iterations = 1000; // Number of iterations
        for (int i = 0; i < iterations; i++) {
            List<City> newTour = swapCities(new ArrayList<>(tour));
            double currentDistance = calculateDistance(newTour);
            if (currentDistance < bestDistance) {
                tour = newTour;
                bestDistance = currentDistance;
            }
        }
        // Print optimized tour and distance
        System.out.print("Optimized Tour: ");
        for (City city : tour) {
            System.out.print(city.id.substring(city.id.length() - 5)+ "->");
        }
        System.out.print(tour.get(0).id.substring(tour.get(0).id.length() - 5));
        System.out.format("\nTour length:: %.2f m %n", bestDistance);
    }
}
