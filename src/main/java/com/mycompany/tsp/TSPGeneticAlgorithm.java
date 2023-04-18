/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tsp;

import com.mycompany.tsp.TSPGeneticAlgorithm.City;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
//import com.mycompany.psafinalproject.City;
/**
 *
 * @author aakashrajawat
 */
public class TSPGeneticAlgorithm {
    public static final double EARTH_RADIUS = 6371; // Earth's radius in km
    private static final int POPULATION_SIZE = 1000;
    private static final int MAX_GENERATIONS = 700;
    private static final double MUTATION_RATE = 0.5;
    private static final int ELITISM_COUNT = 5;
    
    private List<City> cities;
    private int populationSize;
    private int maxGenerations;
    private double mutationRate;
    private int elitismCount;
    
    public static class City {
        private String name;
        private double longitude;
        private double latitude;

        public City(String name, double longitude, double latitude) {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public String getName() {
            return name;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }
    }


    
    public TSPGeneticAlgorithm(List<City> cities, int populationSize, int maxGenerations, double mutationRate, int elitismCount) {
        this.cities = cities;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.elitismCount = elitismCount;
    }
    
    
    
    public List<City> solve() {
        List<Route> population = initPopulation();
        for (int i = 0; i < maxGenerations; i++) {
            population = evolvePopulation(population);
        }
        return getBestRoute(population).getCities();
    }
    
    public List<Route> initPopulation() {
        List<Route> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Route(shuffle(cities)));
        }
        return population;
    }
    
    public List<City> shuffle(List<City> cities) {
        List<City> shuffled = new ArrayList<>(cities);
        Collections.shuffle(shuffled);
        return shuffled;
    }
    
    private List<Route> evolvePopulation(List<Route> population) {
        List<Route> newPopulation = new ArrayList<>();
        Collections.sort(population);
        for (int i = 0; i < elitismCount; i++) {
            newPopulation.add(population.get(i));
        }
        for (int i = elitismCount; i < populationSize; i++) {
            Route parent1 = selectParent(population);
            Route parent2 = selectParent(population);
            Route child = crossover(parent1, parent2);
            mutate(child);
            newPopulation.add(child);
        }
        return newPopulation;
    }
    
    private Route selectParent(List<Route> population) {
        Random rand = new Random();
        int index = rand.nextInt(population.size());
        return population.get(index);
    }
    
    private Route crossover(Route parent1, Route parent2) {
        int start = new Random().nextInt(parent1.getCities().size());
        int end = new Random().nextInt(parent1.getCities().size() - start) + start;
        List<City> childCities = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            childCities.add(parent1.getCities().get(i));
        }
        for (City city : parent2.getCities()) {
            if (!childCities.contains(city)) {
                childCities.add(city);
            }
        }
        return new Route(childCities);
    }
    
    private void mutate(Route route) {
        Random rand = new Random();
        for (int i = 0; i < route.getCities().size(); i++) {
            if (rand.nextDouble() < mutationRate) {
                int j = rand.nextInt(route.getCities().size());
                Collections.swap(route.getCities(), i, j);
            }
        }
    }
    
private Route getBestRoute(List<Route> population) {
    return Collections.min(population);
}

public static void main(String[] args) {
    String fileName = "./resources/crimeActual.csv";
    List<City> cities = readCitiesFromFile(fileName);
    TSPGeneticAlgorithm tspGA = new TSPGeneticAlgorithm(cities, POPULATION_SIZE, MAX_GENERATIONS, MUTATION_RATE, ELITISM_COUNT);
    List<City> solution = tspGA.solve();
    double distance = calculateTotalDistance(solution);
    
    System.out.print("Best route:");
    for (City city : solution) {
        System.out.print(city.getName().substring(city.getName().length()-5)+"->"); // To print last 5 digits of crimeID hexcode
    }
    System.out.print(solution.get(0).getName().substring(solution.get(0).getName().length()-5)+"->");
    System.out.printf("Tour length:: %.2f m\n", distance*1000);
}

public static List<City> readCitiesFromFile(String fileName) {
    List<City> cities = new ArrayList<>();
    boolean firstLine = true;
    
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] tokens = line.split(",");
            String crimeId = tokens[0];
            double longitude = Double.parseDouble(tokens[1]);
            double latitude = Double.parseDouble(tokens[2]);
            cities.add(new City(crimeId, longitude, latitude));
        }
    } catch (IOException e) {
        System.err.println("Error reading cities from file: " + fileName);
        e.printStackTrace();
    }
    return cities;
}

public static double calculateDistance(City city1, City city2) {
    double lat1 = Math.toRadians(city1.getLatitude());
    double lat2 = Math.toRadians(city2.getLatitude());
    double lon1 = Math.toRadians(city1.getLongitude());
    double lon2 = Math.toRadians(city2.getLongitude());
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return EARTH_RADIUS * c;
}

 public static double calculateTotalDistance(List<City> cities) {
    double totalDistance = 0;
    for (int i = 0; i < cities.size() - 1; i++) {
        City city1 = cities.get(i);
        City city2 = cities.get(i + 1);
        totalDistance += calculateDistance(city1, city2);
    }
    City lastCity = cities.get(cities.size() - 1);
    City firstCity = cities.get(0);
    totalDistance += calculateDistance(lastCity, firstCity);
    return totalDistance;
}
}

class Route implements Comparable<Route> {
private List<City> cities;
private double distance;

public Route(List<City> cities) {
    this.cities = cities;
    this.distance = TSPGeneticAlgorithm.calculateTotalDistance(cities);
}

public List<City> getCities() {
    return cities;
}

public double getDistance() {
    return distance;
}

public int compareTo(Route other) {
    return Double.compare(this.distance, other.distance);
}
}

class Population {
private List<Route> routes;

public Population(int size, List<City> cities) {
    routes = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
        List<City> shuffledCities = new ArrayList<>(cities);
        Collections.shuffle(shuffledCities);
        Route route = new Route(shuffledCities);
        routes.add(route);
    }
}

public List<Route> getRoutes() {
    return routes;
}

public void setRoutes(List<Route> routes) {
    this.routes = routes;
}
}
class GeneticAlgorithm {
private double mutationRate;
private int elitismCount;

public GeneticAlgorithm(double mutationRate, int elitismCount) {
    this.mutationRate = mutationRate;
    this.elitismCount = elitismCount;
}

public Population evolvePopulation(Population population) {
    List<Route> routes = population.getRoutes();
    List<Route> newRoutes = new ArrayList<>(routes.size());
    if (elitismCount > 0) {
        List<Route> sortedRoutes = new ArrayList<>(routes);
        Collections.sort(sortedRoutes);
        newRoutes.addAll(sortedRoutes.subList(0, elitismCount));
    }
    while (newRoutes.size() < routes.size()) {
        Route parent1 = selectParent(routes);
        Route parent2 = selectParent(routes);
        Route child = crossover(parent1, parent2);
        mutate(child);
        newRoutes.add(child);
    }
    Population newPopulation = new Population(routes.size(), null);
    newPopulation.setRoutes(newRoutes);
    return newPopulation;
}

private Route selectParent(List<Route> routes) {
    int index1 = (int) (Math.random() * routes.size());
    int index2 = (int) (Math.random() * routes.size());
    Route route1 = routes.get(index1);
    Route route2 = routes.get(index2);
    return route1.compareTo(route2) < 0 ? route1 : route2;
}

private Route crossover(Route parent1, Route parent2) {
    int startPos = (int) (Math.random() * parent1.getCities().size());
    int endPos = (int) (Math.random() * parent1.getCities().size());
    if (startPos > endPos) {
        int temp = startPos;
        startPos = endPos;
        endPos = temp;
    }
    List<City> childCities = new ArrayList<>();
    for (int i = startPos; i < endPos; i++) {
        City city = parent1.getCities().get(i);
        childCities.add(city);
    }
    for (int i = 0; i < parent2.getCities().size(); i++) {
        City city = parent2.getCities().get(i);
        if (!childCities.contains(city)) {
            childCities.add(city);
        }
    }
    return new Route(childCities);
}

private void mutate(Route route) {
    for (int i = 0; i < route.getCities().size(); i++) {
        if (Math.random() < mutationRate) {
            int j = (int) (Math.random() * route.getCities().size());
            Collections.swap(route.getCities(), i, j);
        }
    }
}
}
