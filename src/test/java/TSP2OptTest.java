
import com.mycompany.tsp.TSP2Opt;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ruchi
 */
public class TSP2OptTest {
    
     @Test
    public void testDistance() {
        TSP2Opt.Point a = new TSP2Opt.Point("A", 0, 0);
        TSP2Opt.Point b = new TSP2Opt.Point("B", 1, 1);
        double expectedDistance = 157.24; // The distance between (0, 0) and (1, 1) in meters
        double actualDistance = TSP2Opt.distance(a, b);
        assertEquals(expectedDistance, actualDistance, 0.01);
    }

    @Test
    public void testTourDistance() {
        double[][] distances = { { 0, 100, 200 }, { 100, 0, 300 }, { 200, 300, 0 } };
        List<Integer> tour = new ArrayList<>();
        tour.add(0);
        tour.add(1);
        tour.add(2);
        double expectedDistance = 600;
        double actualDistance = TSP2Opt.tourDistance(tour, distances);
        assertEquals(expectedDistance, actualDistance, 0.01);
    }

    @Test
    public void testTwoOptSwap() {
        List<Integer> tour = new ArrayList<>();
        tour.add(0);
        tour.add(1);
        tour.add(2);
        tour.add(3);
        List<Integer> expectedTour = new ArrayList<>();
        expectedTour.add(0);
        expectedTour.add(2);
        expectedTour.add(1);
        expectedTour.add(3);
        List<Integer> actualTour = TSP2Opt.twoOptSwap(tour, 1, 2);
        assertEquals(expectedTour, actualTour);
    }
    
}
