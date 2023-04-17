/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import com.mycompany.tsp.TSPSimulatedAnnealing;
import com.mycompany.tsp.TSPSimulatedAnnealing.City;
import com.mycompany.tsp.TSPSimulatedAnnealing.Tour;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ruchi
 */
public class TSPSimulatedAnnealingTest {
    
private static final double DELTA = 0.001;
    private static ArrayList<TSPSimulatedAnnealing.City> cities;

  @BeforeClass
  public static void setUp() {
    cities = new ArrayList<>();
    cities.add(new City("A", 0.0, 0.0));
    cities.add(new City("B", 1.0, 0.0));
    cities.add(new City("C", 0.0, 1.0));
    cities.add(new City("D", 1.0, 1.0));
    cities.add(new City("E", 2.0, 2.0));
    cities.add(new City("F", 2.0, 0.0));
  }

  
  @Test
  public void testGetRandomIndex() {
    int index = TSPSimulatedAnnealing.getRandomIndex(cities.size());
   assertTrue(index >= 0 && index < cities.size());
  }

  @Test
  public void testGetDistanceBetweenCities() {
    City city1 = cities.get(0);
    City city2 = cities.get(1);
    assertEquals(111195.0, Math.round(TSPSimulatedAnnealing.Tour.getDistanceBetweenCities(city1, city2)), 0.001);
  }

  @Test
  public void testTour() {
    Tour tour = new Tour(cities);
   assertEquals(981509, Math.round(tour.getDistance()), 0.001);
  }

  

    @Test
    public void testCalculateDistance() {
        TSPSimulatedAnnealing.Tour tour = new TSPSimulatedAnnealing.Tour(cities);
        assertEquals(981509, Math.round(tour.getDistance()), DELTA);
    }

    @Test
    public void testGetDistanceBetweenCities1() {
        TSPSimulatedAnnealing.Tour tour = new TSPSimulatedAnnealing.Tour(cities);
        double expected = 111195;
        double actual = Math.round(Tour.getDistanceBetweenCities(cities.get(0), cities.get(1)));
       assertEquals(expected, actual, DELTA);
    }
}
