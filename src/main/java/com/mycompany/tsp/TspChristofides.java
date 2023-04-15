/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aakashrajawat
 */
public class TspChristofides {

    public static class City {
       public String id;
       public double latitude;
       public double longitude;
       public boolean visited;
        
        public City(String id, double longitude, double latitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.visited = false;
        }
                
        public String getcrimeId() {
            return this.id;
        }

        public double distanceTo(City other) {
            double lat1 = Math.toRadians(this.latitude);
            double lat2 = Math.toRadians(other.latitude);
            double lon1 = Math.toRadians(this.longitude);
            double lon2 = Math.toRadians(other.longitude);
            
            double dLat = lat2 - lat1;
            double dLon = lon2 - lon1;
            
            double a = Math.pow(Math.sin(dLat/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon/2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            
            return 6371 * c; //6371 -> Radius of the Earth
        }
    }
    
    public static void main(String[] args) throws IOException {
        List<City> cities = readCitiesFromCSV("/Users/aakashrajawat/Downloads/project.csv");
        List<City> tour = christofides(cities);
        double tourLength = calculateTourLength(tour)*1000;
        System.out.println("Tour length: " + tourLength + "m");
        System.out.print("Tour: ");
        for (City c : tour) {
            System.out.print(c.getcrimeId() + " -> ");
        }
        
        System.out.println(tour.get(0).getcrimeId());

    }
    
    public static List<City> readCitiesFromCSV(String filename) throws IOException {
        List<City> cities = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = "";
        boolean firstLine = true;

        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            String[] fields = line.split(",");
            String id = fields[0];
            double lon = Double.parseDouble(fields[1]);
            double lat = Double.parseDouble(fields[2]);
            cities.add(new City(id, lon, lat));
        }
        br.close();
        return cities;
    }
    
    public static List<City> christofides(List<City> cities) {
        // Step 1: Build minimum spanning tree
        List<City> mst = buildMinimumSpanningTree(cities);
        
        // Step 2: Build subset of odd-degree vertices
        List<City> odds = findOddDegreeVertices(mst);
        
        // Step 3: Build minimum weight perfect matching on odd-degree vertices
        List<City> matching = buildMinimumWeightPerfectMatching(odds);
        
        // Step 4: Combine minimum spanning tree and matching to form tour
        mst.addAll(matching);
        List<City> tour = findEulerianTour(mst);
        tour.add(tour.get(0)); // add first city to end to complete the tour
        
        return tour;
    }
    
    public static List<City> buildMinimumSpanningTree(List<City> cities) {
        List<City> mst = new ArrayList<>();
        cities.get(0).visited = true; // start from first city
        while (mst.size() < cities.size() - 1) { // MST has n-1 edges
            double minDistance = Double.MAX_VALUE;
            City closestCity = null;
            City nearestNeighbor = null;
            for (City city : cities) {
                if (city.visited) {
                    for (City neighbor : cities) {
                        if(!neighbor.visited) {
                        double distance = city.distanceTo(neighbor);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestCity = city;
                            nearestNeighbor = neighbor;
                        }
                    }
                }
            }
        }
mst.add(nearestNeighbor);
nearestNeighbor.visited = true;
}
return mst;
}

public static List<City> findOddDegreeVertices(List<City> cities) {
    List<City> odds = new ArrayList<>();
    for (City city : cities) {
        int degree = 0;
        for (City neighbor : cities) {
            if (city != neighbor && city.distanceTo(neighbor) > 0.0) {
                degree++;
            }
        }
        if (degree % 2 == 1) { // odd degree
            odds.add(city);
        }
    }
    return odds;
}

public static List<City> buildMinimumWeightPerfectMatching(List<City> cities) {
    // Build complete graph of odd-degree vertices
    List<List<Double>> distances = new ArrayList<>();
    for (int i = 0; i < cities.size(); i++) {
        distances.add(new ArrayList<>());
        for (int j = 0; j < cities.size(); j++) {
            distances.get(i).add(cities.get(i).distanceTo(cities.get(j)));
        }
    }
    
    // Run minimum weight matching algorithm (greedy)
    List<City> matching = new ArrayList<>();
    boolean[] used = new boolean[cities.size()];
    for (int i = 0; i < cities.size(); i++) {
        int best = -1;
        for (int j = 0; j < cities.size(); j++) {
            if (!used[j] && (best == -1 || distances.get(i).get(j) < distances.get(i).get(best))) {
                best = j;
            }
        }
        used[best] = true;
        if (i % 2 == 0) { // add edge to matching
            matching.add(cities.get(i));
            matching.add(cities.get(best));
        }
    }
    
    return matching;
}

public static List<City> findEulerianTour(List<City> cities) {
    List<City> tour = new ArrayList<>();
    tour.add(cities.get(0));
    while (!cities.isEmpty()) {
        City lastCity = tour.get(tour.size() - 1);
        City nextCity = null;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i) != lastCity && (nextCity == null || lastCity.distanceTo(cities.get(i)) < lastCity.distanceTo(nextCity))) {
                nextCity = cities.get(i);
            }
        }
        tour.add(nextCity);
        cities.remove(nextCity);
    }
    return tour;
}

public static double calculateTourLength(List<City> tour) {
    double length = 0.0;
    for (int i = 0; i < tour.size() - 1; i++) {
        length += tour.get(i).distanceTo(tour.get(i+1));
    }
    return length;
}
}
