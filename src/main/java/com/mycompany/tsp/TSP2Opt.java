/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tsp;

/**
 *
 * @author aakashrajawat
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSP2Opt {

    /**
     * @param args the command line arguments
     */
    
    private static final double EARTH_RADIUS = 6371;
    
    // Class to represent a point on the earth's surface
    public static class Point {
        public String crimeId;
        public double longitude;
        public double latitude;
        
        public Point(String crimeId, double longitude, double latitude) {
            this.crimeId = crimeId;
            this.longitude = longitude;
            this.latitude = latitude;
        }
         public String getcrimeId() {
            return this.crimeId;
        }
    }
    
    public static void main(String[] args) {


        String filename = "./resources/crimeActual.csv";
        
        List<Point> points = new ArrayList<Point>();
        
        // Read in the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                    if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] fields = line.split(",");
                String crimeId = fields[0];
                double longitude = Double.parseDouble(fields[1]);
                double latitude = Double.parseDouble(fields[2]);
                points.add(new Point(crimeId, longitude, latitude));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            return;
        }
        
        // Calculate the distance matrix
        double[][] distances = new double[points.size()][points.size()];
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                distances[i][j] = distance(points.get(i), points.get(j));
            }
        }
        
        // Solve the TSP using the 2-opt algorithm
        List<Integer> tour = new ArrayList<Integer>();
        for (int i = 0; i < points.size(); i++) {
            tour.add(i);
        }
        double bestDistance = tourDistance(tour, distances);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < points.size() - 1; i++) {
                for (int j = i + 1; j < points.size(); j++) {
                    List<Integer> newTour = twoOptSwap(tour, i, j);
                    double newDistance = tourDistance(newTour, distances);
                    if (newDistance < bestDistance) {
                        tour = newTour;
                        bestDistance = newDistance;
                        improved = true;
                    }
                }
            }
        }
        
        
        // Print out the tour and its distance
        System.out.print("Tour:");
        for (int i = 0; i < tour.size(); i++) {
            Point point = points.get(tour.get(i));
            System.out.printf("%s-> ", point.crimeId.substring(point.crimeId.length()-5));
            //System.out.printf("Best route distance: %.2f m\n", distance*1000);
        }
        //System.out.println(points.get(0).crimeId.substring(points.get(0).crimeId.length()-5));System.out.println(tour.get(0).Point.getcrimeId().substring(tour.get(0).getcrimeId().length()-5));
        System.out.format("Tour length:: %.2f m %n", bestDistance*1000);
    }
    
    // Calculate the distance between two points on the earth's surface
    public static double distance(Point a, Point b) {
        double lat1 = Math.toRadians(a.latitude);
        double lat2 = Math.toRadians(b.latitude);
        double lon1 = Math.toRadians(a.longitude);
        double lon2 = Math.toRadians(b.longitude);
        
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double sinLat = Math.sin(dlat / 2);
        double sinLon = Math.sin(dlon / 2);
        double a1 = sinLat * sinLat + Math.cos(lat1) * Math.cos(lat2) * sinLon * sinLon;
        double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));
        return EARTH_RADIUS * c;
}

    // Calculate the distance of a tour
public static double tourDistance(List<Integer> tour, double[][] distances) {
    double distance = 0;
    for (int i = 0; i < tour.size() - 1; i++) {
        distance += distances[tour.get(i)][tour.get(i + 1)];
    }
    distance += distances[tour.get(tour.size() - 1)][tour.get(0)];
    return distance;
}

// Swap two edges in a tour using the 2-opt algorithm
public static List<Integer> twoOptSwap(List<Integer> tour, int i, int j) {
    List<Integer> newTour = new ArrayList<Integer>(tour.subList(0, i));
    for (int k = j; k >= i; k--) {
        newTour.add(tour.get(k));
    }
    for (int k = j + 1; k < tour.size(); k++) {
        newTour.add(tour.get(k));
    }
    return newTour;
}

    
}
