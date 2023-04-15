/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import com.mycompany.tsp.TSPSimulatedAnnealing;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ruchi
 */
public class TSPSimulatedAnnealingTest {
    
      public TSPSimulatedAnnealing tspsa;

    @Before
    public void setUp() {
        tspsa = new TSPSimulatedAnnealing();
        TSPSimulatedAnnealing.numCities = 5;
        TSPSimulatedAnnealing.cityIds = new String[] { "A", "B", "C", "D", "E" };
        TSPSimulatedAnnealing.cityLongitudes = new double[] { 0, 1, 2, 3, 4 };
        TSPSimulatedAnnealing.cityLatitudes = new double[] { 0, 1, 2, 3, 4 };
        TSPSimulatedAnnealing.distances = new double[][] {
            { 0, 1, 2, 3, 4 },
            { 1, 0, 1, 2, 3 },
            { 2, 1, 0, 1, 2 },
            { 3, 2, 1, 0, 1 },
            { 4, 3, 2, 1, 0 }
        };
    }

    @Test
    public void testAcceptNeighborShouldReturnTrueWhenNeighborDistanceIsSmaller() {
        assertTrue(TSPSimulatedAnnealing.acceptNeighbor(2.0, 1.5, 100.0));
    }


    @Test
    public void testAcceptNeighborShouldReturnTrueWhenNeighborDistanceIsLargerButProbabilitySucceeds() {
        TSPSimulatedAnnealing.random.setSeed(12345);
        assertTrue(TSPSimulatedAnnealing.acceptNeighbor(2.0, 2.5, 1.0));
    }

    @Test
    public void testGenerateRandomSolutionShouldGenerateDifferentSolutions() {
        TSPSimulatedAnnealing.random.setSeed(12345);
        int[] solution1 = TSPSimulatedAnnealing.generateRandomSolution();
        int[] solution2 = TSPSimulatedAnnealing.generateRandomSolution();
        assertNotEquals(solution1, solution2);
    }

    @Test
    public void testGenerateNeighborSolutionShouldGenerateDifferentSolutions() {
        TSPSimulatedAnnealing.random.setSeed(12345);
        int[] solution = new int[] { 0, 1, 2, 3, 4 };
        int[] neighbor1 = TSPSimulatedAnnealing.generateNeighborSolution(solution);
        int[] neighbor2 = TSPSimulatedAnnealing.generateNeighborSolution(solution);
        assertNotEquals(neighbor1, neighbor2);
    }

    @Test
    public void testCalculateTotalDistanceShouldReturnCorrectDistance() {
        int[] solution = new int[] { 0, 1, 2, 3, 4 };
        assertEquals(8.0, TSPSimulatedAnnealing.calculateTotalDistance(solution), 0.0);
    }


       @Test
    public void testGenerateRandomSolution() {
        int[] solution = TSPSimulatedAnnealing.generateRandomSolution();
        assertEquals(solution.length, TSPSimulatedAnnealing.numCities);

        boolean[] cityUsed = new boolean[TSPSimulatedAnnealing.numCities];
        for (int i = 0; i < TSPSimulatedAnnealing.numCities; i++) {
            cityUsed[solution[i]] = true;
        }
        for (int i = 0; i < TSPSimulatedAnnealing.numCities; i++) {
            assertTrue(cityUsed[i]);
        }
    }


    @Test
    public void testAcceptNeighbor() {
        double currentDistance = 10;
        double neighborDistance = 12;
        double temperature = 100;
        double acceptanceProbability = Math.exp((currentDistance - neighborDistance) / temperature);
        double[] probabilities = new double[10000];
        int numAccepted = 0;
        for (int i = 0; i < 10000; i++) {
            if (TSPSimulatedAnnealing.acceptNeighbor(currentDistance, neighborDistance, temperature)) {
                numAccepted++;
            }
        }
        double actualProbability = (double) numAccepted / 10000;
        assertEquals(acceptanceProbability, actualProbability, 0.01);
    }
}
