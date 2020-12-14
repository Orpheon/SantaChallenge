package ch.mse.sam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;

public class SolutionSamTest {

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

    @Test
    public void testGetReindeerWeariness() {
        ArrayList<GiftTour> giftsTour = new ArrayList<>();
        SolutionSam solution = new SolutionSam(giftsTour);
        assertEquals(0, solution.getReindeerWeariness(), 1E-6);

        giftsTour.add(new GiftTour(0, new Location(0, 0), 1, 0));
        solution = new SolutionSam(giftsTour);
        assertEquals(210158.41135821596, solution.getReindeerWeariness(), 1E-6);

        giftsTour.add(new GiftTour(1, new Location(0, 10), 2, 0));
        solution = new SolutionSam(giftsTour);
        assertEquals(232397.39668712774, solution.getReindeerWeariness(), 1E-6);
    }

    /**
     * test that it returns false when the cargo limit is exceeded with one gift.
     */
    @Test
    public void testIsValid2() {
        ArrayList<GiftTour> giftsTour = new ArrayList<>();
        giftsTour.add(new GiftTour(0, new Location(0, 0), 1001, 0));
        SolutionSam.setGifts(new HashSet<>(giftsTour));
        SolutionSam solution = new SolutionSam(giftsTour);
        assertFalse(solution.isValid(false, true));
    }

    /**
     * test that it returns false when the cargo limit is exceeded with multiple
     * gifts.
     */
    @Test
    public void testIsValid3() {
        ArrayList<GiftTour> giftTour = new ArrayList<>();
        giftTour.add(new GiftTour(0, new Location(0, 0), 500, 0));
        giftTour.add(new GiftTour(1, new Location(0, 0), 501, 0));
        SolutionSam.setGifts(new HashSet<>(giftTour));
        SolutionSam solution = new SolutionSam(giftTour);
        assertFalse(solution.isValid(false, true));
    }

    /**
     * test that the cargo limit can be delivered.
     */
    @Test
    public void testIsValid4() {
        ArrayList<GiftTour> giftTour = new ArrayList<>();
        giftTour.add(new GiftTour(0, new Location(0, 0), 500, 0));
        giftTour.add(new GiftTour(1, new Location(0, 0), 500, 0));
        SolutionSam.setGifts(new HashSet<>(giftTour));
        SolutionSam solution = new SolutionSam(giftTour);
        assertTrue(solution.isValid(false, true));
    }

    /**
     * test that it is still possible to deliver more than the cargo limit in 2
     * tours.
     */
    @Test
    public void testIsValid5() {
        ArrayList<GiftTour> giftTour = new ArrayList<>();
        giftTour.add(new GiftTour(0, new Location(0, 0), 600, 0));
        giftTour.add(new GiftTour(1, new Location(0, 0), 600, 1));
        SolutionSam.setGifts(new HashSet<>(giftTour));
        SolutionSam solution = new SolutionSam(giftTour);
        assertTrue(solution.isValid(false, true));
    }

    /**
     * test that no gift is delivered more than once.
     */
    @Test
    public void testIsValid6() {
        GiftTour giftA = new GiftTour(0, new Location(0, 0), 600, 0);
        GiftTour giftB = new GiftTour(1, new Location(0, 0), 600, 1);
        HashSet<Gift> gifts = new HashSet<>();
        gifts.add(giftA);
        gifts.add(giftB);
        SolutionSam.setGifts(gifts);
        ArrayList<GiftTour> gt = new ArrayList<>();
        gt.add(giftA);
        gt.add(giftA);
        gt.add(giftB);
        SolutionSam solution = new SolutionSam(gt);
        assertFalse(solution.isValid(false, true));
    }

    /**
     * test that all packets have to be delivered.
     */
    @Test
    public void testIsValid7() {
        GiftTour giftA = new GiftTour(0, new Location(0, 0), 600, 0);
        GiftTour giftB = new GiftTour(1, new Location(0, 0), 600, 1);
        HashSet<Gift> gifts = new HashSet<>();
        gifts.add(giftA);
        gifts.add(giftB);
        SolutionSam.setGifts(gifts);
        ArrayList<GiftTour> gt = new ArrayList<>();
        gt.add(giftB);
        SolutionSam solution = new SolutionSam(gt);
        assertFalse(solution.isValid(false, true));
    }

    @Test
    public void testGetTourWeight() {
        ArrayList<GiftTour> giftTour = new ArrayList<>();
        giftTour.add(new GiftTour(0, null, 1, 0));
        giftTour.add(new GiftTour(1, null, 3, 0));
        giftTour.add(new GiftTour(2, null, 5, 1));
        giftTour.add(new GiftTour(3, null, 2, 1));
        SolutionSam solution = new SolutionSam(giftTour);
        assertEquals((Double) 4.0d, solution.getTourWeight().get(0));
        assertEquals((Double) 7.0, solution.getTourWeight().get(1));
    }
}
