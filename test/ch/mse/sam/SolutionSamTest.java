package ch.mse.sam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;

public class SolutionSamTest {

    private SolutionSam solution;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        SolutionSam.setGifts(new LinkedList<>());
        solution = new SolutionSam();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetReindeerWeariness() {
        assertEquals(0, solution.getReindeerWeariness(), 1E-6);
        solution.getGiftsTour().add(new GiftTour(0, new Location(0, 0), 1, 0));
        SolutionSam.setGifts(new LinkedList<>());
        assertEquals(210158.41135821596, solution.getReindeerWeariness(), 1E-6);
        solution.getGiftsTour().add(new GiftTour(1, new Location(0, 10), 2, 0));
        assertEquals(232397.39668712774, solution.getReindeerWeariness(), 1E-6);
    }

    /**
     * test that an exception is thrown when the gifts is null.
     */
    @Test
    public void testIsValid1() {
        assertTrue(solution.isValid(false));
    }

    /**
     * test that it returns false when the cargo limit is exceeded with one gift.
     */
    @Test
    public void testIsValid2() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 1001, 0);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertFalse(solution.isValid(false));
    }

    /**
     * test that it returns false when the cargo limit is exceeded with muliple
     * gifts.
     */
    @Test
    public void testIsValid3() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 500, 0);
        gifts.add(gt);
        gt = new GiftTour(1, new Location(0, 0), 501, 0);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertFalse(solution.isValid(false));
    }

    /**
     * test that the cargo limit can be delivered.
     */
    @Test
    public void testIsValid4() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 500, 0);
        gifts.add(gt);
        solution.getGiftsTour().add(gt);
        gt = new GiftTour(1, new Location(0, 0), 500, 0);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertTrue(solution.isValid(false));
    }

    /**
     * test that it is still possible to deliver more than the cargo limit in 2
     * tours.
     */
    @Test
    public void testIsValid5() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 600, 0);
        gifts.add(gt);
        solution.getGiftsTour().add(gt);
        gt = new GiftTour(1, new Location(0, 0), 600, 1);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertTrue(solution.isValid(false));
    }

    /**
     * test that no gift is delivered more than once.
     */
    @Test
    public void testIsValid6() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 600, 0);
        gifts.add(gt);
        gt = new GiftTour(0, new Location(0, 0), 600, 1);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertTrue(solution.isValid(false));
    }

    /**
     * test that all packets have to be delivered.
     */
    @Test
    public void testIsValid7() {
        LinkedList<Gift> gifts = new LinkedList<>();
        GiftTour gt = new GiftTour(0, new Location(0, 0), 600, 0);
        gifts.add(gt);
        solution.getGiftsTour().add(gt);
        gt = new GiftTour(0, new Location(0, 0), 600, 1);
        gifts.add(gt);
        SolutionSam.setGifts(gifts);
        solution.getGiftsTour().add(gt);
        assertFalse(solution.isValid(false));
    }
}
