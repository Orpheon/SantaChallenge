package ch.mse.sam;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SolverGeneticTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests the same case as shown in the lecture.
     */
    @Test
    public void testPartiallyMappedCrossover1() {
        ArrayList<GiftTour> male = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        ArrayList<GiftTour> actual = SolverGenetic.partiallyMappedCrossover(male, female, 3, 6);
        ArrayList<GiftTour> expected = addNewGiftTours(new ArrayList<>(), 4, 3, 8, 7, 6, 1, 5, 2);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /**
     * Test with minimal range.
     */
    @Test
    public void testPartiallyMappedCrossover2() {
        ArrayList<GiftTour> male = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        ArrayList<GiftTour> actual = SolverGenetic.partiallyMappedCrossover(male, female, 0, 1);
        ArrayList<GiftTour> expected = addNewGiftTours(new ArrayList<>(), 8, 3, 1, 2, 7, 4, 5, 6);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /**
     * Test with maximal range.
     */
    @Test
    public void testPartiallyMappedCrossover3() {
        ArrayList<GiftTour> male = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        ArrayList<GiftTour> actual = SolverGenetic.partiallyMappedCrossover(male, female, 0, 8);
        ArrayList<GiftTour> expected = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    /**
     * Test with an empty mate as parameter.
     */
    @Test
    public void testPartiallyMappedCrossoverIllegal1() {
        ArrayList<GiftTour> male = new ArrayList<>();
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        Exception ex = null;
        try {
            SolverGenetic.partiallyMappedCrossover(male, female, 0, 8);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    /**
     * Test with null as parameter.
     */
    @Test
    public void testPartiallyMappedCrossoverIllegal2() {
        ArrayList<GiftTour> male = null;
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        Exception ex = null;
        try {
            SolverGenetic.partiallyMappedCrossover(male, female, 0, 8);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    /**
     * Test when the start range equals the end range.
     */
    @Test
    public void testPartiallyMappedCrossoverIllegal3() {
        ArrayList<GiftTour> male = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        Exception ex = null;
        try {
            SolverGenetic.partiallyMappedCrossover(male, female, 6, 6);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    /**
     * Test when the start range is smaller than the end range.
     */
    @Test
    public void testPartiallyMappedCrossoverIllegal4() {
        ArrayList<GiftTour> male = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        ArrayList<GiftTour> female = addNewGiftTours(new ArrayList<>(), 8, 3, 4, 7, 6, 1, 5, 2);
        Exception ex = null;
        try {
            SolverGenetic.partiallyMappedCrossover(male, female, 6, 4);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    private ArrayList<GiftTour> addNewGiftTours(ArrayList<GiftTour> male, Integer... giftIds) {
        for (Integer giftId : giftIds) {
            male.add(new GiftTour(giftId, null, 0.0, 0));
        }
        return male;
    }

    /**
     * Swaps the permutation on 1 place.
     */
    @Test
    public void testMutatePosition1() {
        ArrayList<GiftTour> child = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        child = SolverGenetic.mutatePosition(child, Arrays.asList(new Swap[] { new Swap(0, 1) }));
        ArrayList<GiftTour> expected = addNewGiftTours(new ArrayList<>(), 3, 1, 8, 2, 7, 4, 5, 6);
        assertArrayEquals(expected.toArray(), child.toArray());
    }

    /**
     * Swaps the permutation on 2 places.
     */
    @Test
    public void testMutatePosition2() {
        ArrayList<GiftTour> child = addNewGiftTours(new ArrayList<>(), 1, 3, 8, 2, 7, 4, 5, 6);
        List<Swap> swaps = Arrays.asList(new Swap[] { new Swap(0, 1), new Swap(1, 2) });
        child = SolverGenetic.mutatePosition(child, swaps);
        ArrayList<GiftTour> expected = addNewGiftTours(new ArrayList<>(), 3, 8, 1, 2, 7, 4, 5, 6);
        assertArrayEquals(expected.toArray(), child.toArray());
    }

    /**
     * Checks, if the swap function fails when two places are the same.
     */
    @Test
    public void testMutatePositionIlegal() {
        Exception ex = null;
        try {
            new Swap(0, 0);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    /**
     * Tests the generation of random numbers with normal parameters.
     */
    @Test
    public void testGetUniqueRandomNumbers() {
        for (int i = 0; i < 1000; i++) {
            testGetUniqueRandomNumbersH0(4, 10, true);
        }
    }

    /**
     * Tests the generation of random numbers with normal parameters.
     */
    @Test
    public void testGetUniqueRandomNumbersIllegal() {
        Exception ex = null;
        try {
            testGetUniqueRandomNumbersH0(11, 10, true);
        } catch (IllegalArgumentException e) {
            ex = e;
        }
        assertNotEquals(null, ex);
    }

    private void testGetUniqueRandomNumbersH0(int n, int max, boolean sort) {
        LinkedList<Integer> randomNumbers = SolverGenetic.getUniqueRandomNumbers(n, max, sort);
        assertEquals(n, randomNumbers.size());
        HashMap<Integer, Integer> numCount = new HashMap<Integer, Integer>();
        Integer lastNum = null;
        for (Integer num : randomNumbers) {
            assertEquals(true, num < max); // number has to be smaller than the max
            Integer tmp = numCount.get(num);
            if (tmp == null) {
                tmp = 0;
            }
            numCount.put(num, tmp + 1);
            if (lastNum == null) {
                lastNum = num;
            } else {
                assertEquals(true, lastNum < num); // numbers have to be in ascending order
            }
        }
        for (Entry<Integer, Integer> entry : numCount.entrySet()) {
            assertEquals((Integer) 1, entry.getValue()); // every number has to be used once
        }
    }

    /**
     * Tests that the mutate tour function schanges the tour of a gift.
     */
    @Test
    public void testMutateTour1() {
        ArrayList<GiftTour> child = new ArrayList<>();
        child.add(new GiftTour(0, null, 1, 0));
        child.add(new GiftTour(1, null, 1, 0));
        child.add(new GiftTour(2, null, 1, 0));
        child.add(new GiftTour(3, null, 1, 1));
        child.add(new GiftTour(4, null, 1, 1));
        child.add(new GiftTour(4, null, 1, 1));
        LinkedList<Swap> swaps = new LinkedList<>();
        swaps.add(new Swap(0, 3));
        child = SolverGenetic.mutateTour(child, swaps);
        assertEquals(1, child.get(0).getTourId());
        assertEquals(1, child.get(3).getTourId());
    }

    /**
     * tests, if a gift from a full tour is moved at another one, which still has
     * space.
     */
    @Test
    public void testFixMeee1() {
        ArrayList<GiftTour> child = new ArrayList<>();
        child.add(new GiftTour(0, null, 500, 0));
        child.add(new GiftTour(1, null, 500, 0));
        child.add(new GiftTour(2, null, 500, 0));
        child.add(new GiftTour(3, null, 1, 1));
        child.add(new GiftTour(4, null, 1, 1));
        child.add(new GiftTour(5, null, 1, 1));
        child = SolverGenetic.fixMeee(child);
        SolutionSam.setGifts(new HashSet<>(child));
        assertEquals(true, new SolutionSam(child).isValid(false, true));
    }

    /**
     * tests, if multiple gifts from a full tour is moved at another one, which
     * still has space.
     */
    @Test
    public void testFixMeee2() {
        ArrayList<GiftTour> child = new ArrayList<>();
        child.add(new GiftTour(0, null, 500, 0));
        child.add(new GiftTour(1, null, 500, 0));
        child.add(new GiftTour(2, null, 500, 0));
        child.add(new GiftTour(3, null, 500, 0));
        child.add(new GiftTour(4, null, 1, 1));
        child.add(new GiftTour(5, null, 1, 1));
        child.add(new GiftTour(6, null, 1, 1));
        child = SolverGenetic.fixMeee(child);
        SolutionSam.setGifts(new HashSet<>(child));
        assertEquals(true, new SolutionSam(child).isValid(false, true));
    }

    /**
     * Tests, if a new tour is created, if there is not enough space in any other
     * tour.
     */
    @Test
    public void testFixMeee3() {
        ArrayList<GiftTour> child = new ArrayList<>();
        child.add(new GiftTour(0, null, 500, 0));
        child.add(new GiftTour(1, null, 500, 0));
        child.add(new GiftTour(2, null, 600, 0));
        child.add(new GiftTour(4, null, 600, 0));
        child.add(new GiftTour(5, null, 500, 1));
        child.add(new GiftTour(6, null, 500, 1));
        child = SolverGenetic.fixMeee(child);
        SolutionSam.setGifts(new HashSet<>(child));
        assertEquals(true, new SolutionSam(child).isValid(false, true));
    }
}
