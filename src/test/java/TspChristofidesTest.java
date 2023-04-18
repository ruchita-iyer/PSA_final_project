import com.mycompany.tsp.TspChristofides;
import com.mycompany.tsp.TspChristofides.City;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class TspChristofidesTest {

    @Test
    public void testDistanceTo() {
        City city1 = new City("1", 40.7128, -74.0060);
        City city2 = new City("2", 37.7749, -122.4194);
        double expectedDistance = 5381.670512194861; 
        double actualDistance = city1.distanceTo(city2);
        //System.out.println(expectedDistance);
        //System.out.println(actualDistance);
        assertEquals(expectedDistance, actualDistance, 0.01); // allow for small floating-point error
    }
    
        @Test
    public void testReadCitiesFromCSV() throws Exception {
        List<TspChristofides.City> cities = TspChristofides.readCitiesFromCSV("./resources/crimeActual.csv");
        assertNotNull(cities);
        assertEquals(585, cities.size());

        TspChristofides.City city1 = cities.get(0);
        assertEquals("447a81a19157c2f6ef97accacebaa66d8153e19ca43c16ca452e6d8d447823", city1.id);
        //System.out.println(city1.id);
        assertEquals(51.483548, city1.latitude, 0.000001);
        assertEquals(-0.009691, city1.longitude, 0.000001);

        TspChristofides.City city2 = cities.get(1);
        assertEquals("112f8b2a663198263314a16a8b52f1f6835cefcbcf0a35388c98ee5db23dd82", city2.id);
        assertEquals(51.513075, city2.latitude, 0.000001);
        assertEquals(-0.118888, city2.longitude, 0.000001);

        TspChristofides.City city3 = cities.get(2);
        assertEquals("1b679ce8cc565f83868ff4a0829af95442b51ffdf4366341a850c6f248f7d41", city3.id);
        assertEquals(51.540042, city3.latitude, 0.000001);
        assertEquals(0.076327, city3.longitude, 0.000001);


    }
    
    	
    @Test
    public void testBuildMinimumSpanningTree() {
    // Create a list of cities with known distances
    List<TspChristofides.City> cities = new ArrayList<>();
    TspChristofides.City city1 = new TspChristofides.City("A", 0.0, 0.0);
    TspChristofides.City city2 = new TspChristofides.City("B", 0.0, 1.0);
    TspChristofides.City city3 = new TspChristofides.City("C", 1.0, 0.0);
    TspChristofides.City city4 = new TspChristofides.City("D", 1.0, 1.0);
    cities.add(city1);
    cities.add(city2);
    cities.add(city3);
    cities.add(city4);
    
    // Set known distances between cities
    city1.latitude = 0.0;
    city1.longitude = 0.0;
    city2.latitude = 0.0;
    city2.longitude = 1.0;
    city3.latitude = 1.0;
    city3.longitude = 0.0;
    city4.latitude = 1.0;
    city4.longitude = 1.0;
    
    // Build MST
    List<TspChristofides.City> mst = TspChristofides.buildMinimumSpanningTree(cities);
    
    // Test if MST has the expected edges
    List<TspChristofides.City> expectedMst = new ArrayList<>();
    expectedMst.add(city2);
    expectedMst.add(city3);
    expectedMst.add(city4);


    assertEquals(expectedMst, mst);
}

@Test
public void testFindOddDegreeVertices() {
    // Create a list of cities with varying degrees
    List<TspChristofides.City> cities = new ArrayList<>();
    cities.add(new TspChristofides.City("1", 0, 0)); // degree = 3
    cities.add(new TspChristofides.City("2", 1, 1)); // degree = 2
    cities.add(new TspChristofides.City("3", 1, -1)); // degree = 2
    cities.add(new TspChristofides.City("4", -1, 1)); // degree = 2
    cities.add(new TspChristofides.City("5", -1, -1)); // degree = 2

    // Calculate the expected list of odd-degree vertices
    List<TspChristofides.City> expected = new ArrayList<>();

    // Test the method
    List<TspChristofides.City> actual = TspChristofides.findOddDegreeVertices(cities);
    
    assertEquals(expected.size(), actual.size());
    assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
}


 
    
           
}
