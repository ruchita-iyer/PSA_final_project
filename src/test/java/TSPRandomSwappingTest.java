
import com.mycompany.tsp.TSPRandomSwapping;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ruchi
 */
public class TSPRandomSwappingTest {
    @Test
    public void testCalculateDistance() {
        List<TSPRandomSwapping.City> cities = new ArrayList<>();
        cities.add(new TSPRandomSwapping.City("A", 0, 0));
        cities.add(new TSPRandomSwapping.City("B", 3, 0));
        cities.add(new TSPRandomSwapping.City("C", 3, 4));
        cities.add(new TSPRandomSwapping.City("D", 0, 4));

        List<TSPRandomSwapping.City> tour = new ArrayList<>();
        tour.add(cities.get(0));
        tour.add(cities.get(1));
        tour.add(cities.get(2));
        tour.add(cities.get(3));

        double expectedDistance = 1556119.17;
        DecimalFormat df = new DecimalFormat("#.##");
        double actualDistance = TSPRandomSwapping.calculateDistance(tour);
        String result= df.format(actualDistance);
        double r=Double.parseDouble(result);        
        assertEquals(expectedDistance, r, 0.001);
    }

    @Test
    public void testCreateTour() {
        List<TSPRandomSwapping.City> cities = new ArrayList<>();
        cities.add(new TSPRandomSwapping.City("A", 0, 0));
        cities.add(new TSPRandomSwapping.City("B", 3, 0));
        cities.add(new TSPRandomSwapping.City("C", 3, 4));
        cities.add(new TSPRandomSwapping.City("D", 0, 4));

        List<TSPRandomSwapping.City> tour = TSPRandomSwapping.createTour(cities);

        // Make sure all cities are in the tour
        for (TSPRandomSwapping.City city : cities) {
            assertTrue(tour.contains(city));
        }

        // Make sure the tour has the same size as the number of cities
        assertEquals(cities.size(), tour.size());
    }

    @Test
    public void testSwapCities() {
        List<TSPRandomSwapping.City> cities = new ArrayList<>();
        cities.add(new TSPRandomSwapping.City("A", 0, 0));
        cities.add(new TSPRandomSwapping.City("B", 3, 0));
        cities.add(new TSPRandomSwapping.City("C", 3, 4));
        cities.add(new TSPRandomSwapping.City("D", 0, 4));

        List<TSPRandomSwapping.City> tour = new ArrayList<>();
        tour.add(cities.get(0));
        tour.add(cities.get(1));
        tour.add(cities.get(2));
        tour.add(cities.get(3));

        List<TSPRandomSwapping.City> newTour = TSPRandomSwapping.swapCities(new ArrayList<>(tour));

        // Make sure the new tour has the same size as the original tour
        assertEquals(tour.size(), newTour.size());

        // Make sure the new tour has the same cities as the original tour
        for (TSPRandomSwapping.City city : tour) {
            assertTrue(newTour.contains(city));
        }
    }
}
