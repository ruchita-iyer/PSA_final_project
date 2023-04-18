
import com.mycompany.tsp.TSPGeneticAlgorithm;
import com.mycompany.tsp.TSPGeneticAlgorithm.City;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aakashrajawat
 */
public class TSPGeneticAlgorithmTest {
    @Test
    public void testReadCitiesFromFile() {
        String fileName = "./resources/crimeActual.csv";
        List<TSPGeneticAlgorithm.City> cities = TSPGeneticAlgorithm.readCitiesFromFile(fileName);
        assertEquals(585, cities.size());
        
        TSPGeneticAlgorithm.City city1 = cities.get(0);
        assertEquals("447a81a19157c2f6ef97accacebaa66d8153e19ca43c16ca452e6d8d447823", city1.getName());
        assertEquals(-0.009691, city1.getLongitude(), 0.001);
        assertEquals(51.483548, city1.getLatitude(), 0.001);
        
        TSPGeneticAlgorithm.City city2 = cities.get(1);
        assertEquals("112f8b2a663198263314a16a8b52f1f6835cefcbcf0a35388c98ee5db23dd82", city2.getName());
        assertEquals(-0.118888, city2.getLongitude(), 0.001);
        assertEquals(51.513075, city2.getLatitude(), 0.001);
        
        TSPGeneticAlgorithm.City city3 = cities.get(2);
        assertEquals("1b679ce8cc565f83868ff4a0829af95442b51ffdf4366341a850c6f248f7d41", city3.getName());
        assertEquals(0.076327, city3.getLongitude(), 0.001);
        assertEquals(51.540042, city3.getLatitude(), 0.001);
        
    }
    
            @Test
    public void testSolve() {
        List<City> cities = new ArrayList<>();
        cities.add(new City("City1", 0, 0));
        cities.add(new City("City2", 1, 1));
        cities.add(new City("City3", 2, 2));
        cities.add(new City("City4", 3, 3));
        cities.add(new City("City5", 4, 4));

        TSPGeneticAlgorithm tspGA = new TSPGeneticAlgorithm(cities, 10, 50, 0.2, 2);
        List<City> solution = tspGA.solve();

        assertNotNull(solution);
        assertEquals(cities.size(), solution.size());

        // Check if all cities are in the solution
        for (City city : cities) {
            assertTrue(solution.contains(city));
        }

        // Check if there are no duplicates in the solution
        List<City> distinctCities = new ArrayList<>(solution);
        assertEquals(distinctCities.size(), solution.size());
    }


    @Test
    public void testCalculateTotalDistance() {
        List<TSPGeneticAlgorithm.City> cities = new ArrayList<>();
        TSPGeneticAlgorithm.City city1 = new TSPGeneticAlgorithm.City("City1", 12.34, 56.78);
        TSPGeneticAlgorithm.City city2 = new TSPGeneticAlgorithm.City("City2", 12.56, 56.89);
        TSPGeneticAlgorithm.City city3 = new TSPGeneticAlgorithm.City("City3", 12.78, 56.90);
        cities.add(city1);
        cities.add(city2);
        cities.add(city3);
        List<TSPGeneticAlgorithm.City> route = new ArrayList<>();
        route.add(city1);
        route.add(city2);
        route.add(city3);
        double expected = distance(city1, city2) + distance(city2, city3) + distance(city3, city1);
        double actual = TSPGeneticAlgorithm.calculateTotalDistance(route);
        assertEquals(expected, actual, 0.001);
    }
    
    private double distance(TSPGeneticAlgorithm.City city1, TSPGeneticAlgorithm.City city2) {
        double lon1 = Math.toRadians(city1.getLongitude());
        double lon2 = Math.toRadians(city2.getLongitude());
        double lat1 = Math.toRadians(city1.getLatitude());
        double lat2 = Math.toRadians(city2.getLatitude());
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = TSPGeneticAlgorithm.EARTH_RADIUS * c;
        return distance;
    }

    
}

