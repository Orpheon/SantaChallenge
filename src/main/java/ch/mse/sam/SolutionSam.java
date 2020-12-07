package ch.mse.sam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;

/**
 * Class which provides functionality for checking solution and calculating the
 * fitness.
 * 
 * @author Sam
 *
 */
public class SolutionSam implements Comparable<SolutionSam> {

    private static List<Gift> gifts = new LinkedList<Gift>();

    private ArrayList<GiftTour> giftsTour;

    public SolutionSam() {
        giftsTour = new ArrayList<>();
    }

    public SolutionSam(ArrayList<GiftTour> giftsTour) {
        this.giftsTour = giftsTour;
    }

    public static void setGifts(LinkedList<Gift> g) {
        gifts = Collections.unmodifiableList(g);
    }

    public ArrayList<GiftTour> getGiftsTour() {
        return giftsTour;
    }

    /**
     * Calculates the fitness of this solution.
     * 
     * @return
     */
    public double getReindeerWeariness() {
        HashMap<Integer, CostLocationWeight> tourIdMap = new HashMap<>();
        // traverse tours in reverse order to ease calculation
        for (int i = giftsTour.size() - 1; i >= 0; i--) {
            GiftTour giftTour = giftsTour.get(i);
            CostLocationWeight value = tourIdMap.get(giftTour.getTourId());
            if (value == null) {
                value = new CostLocationWeight();
            }
            value.cost += value.locationLast.distanceTo(giftTour.getLocation()) * value.weigthTotal;
            value.locationLast = giftTour.getLocation();
            value.weigthTotal += giftTour.getWeight();
            tourIdMap.put(giftTour.getTourId(), value);
        }
        double reindeerWeariness = 0.0;
        for (CostLocationWeight value : tourIdMap.values()) {
            value.cost += value.locationLast.distanceTo(ProgramSam.NORTH_POLE) * value.weigthTotal;
            reindeerWeariness += value.cost;
        }
        return reindeerWeariness;
    }

    private class CostLocationWeight {
        private double cost = 0.0;
        private Location locationLast = ProgramSam.NORTH_POLE;
        private double weigthTotal = ProgramSam.SLEIGTH_BASE_WEIGHT;

        @Override
        public String toString() {
            return "CostLocationWeight [cost=" + cost + ", locationLast=" + locationLast + ", weigthTotal="
                    + weigthTotal + "]";
        }
    }

    /**
     * Checks if solution is valid.
     * <ul>
     * <li>each tour has to take the cargo limit in account</li>
     * <li>each gift has to be delivered exactly once</li>
     * </ul>
     * 
     * @param printErrors if true, then the errors are printed to the standard out
     * @throws ArithmeticException if the tours are not set in this solution
     * @return true if valid else false
     */
    public boolean isValid(boolean printErrors) {
        if (gifts == null) {
            throw new ArithmeticException(
                    "The gifts have to be set in the solution that the solution can be verified.");
        }
        boolean retVal = true;
        HashMap<Integer, Integer> giftIdCount = new HashMap<>();
        for (Gift gift : gifts) {
            giftIdCount.put(gift.getId(), 0);
        }
        HashMap<Integer, Double> tourWeigth = new HashMap<>();
        for (GiftTour giftTour : giftsTour) {
            int tourId = giftTour.getTourId();
            Double weigth = tourWeigth.get(tourId);
            if (weigth == null) {
                weigth = 0.0;
            }
            tourWeigth.put(tourId, weigth + giftTour.getWeight());
            giftIdCount.put(giftTour.getId(), giftIdCount.get(giftTour.getId()) + 1);
        }
        // each tour has to take the cargo limit in account
        for (Entry<Integer, Double> entry : tourWeigth.entrySet()) {
            if (entry.getValue() > ProgramSam.CARGO_LIMIT) {
                if (printErrors) {
                    System.err.println("The tour " + entry.getKey() + " is over the limit and has the weigth "
                            + entry.getValue() + ".");
                }
                retVal = false;
            }
        }
        // each gift has to be delivered
        for (Entry<Integer, Integer> entry : giftIdCount.entrySet()) {
            if (entry.getValue() != 1) {
                if (printErrors) {
                    System.err.println("The gift " + entry.getKey() + " was visited " + entry.getValue() + " times.");
                }
                retVal = false;
            }
        }
        return retVal;
    }

    /**
     * Compares this solution to the other solution.
     */
    @Override
    public int compareTo(SolutionSam o) {
        int compareValue;
        double valueThis = getReindeerWeariness();
        double valueOther = o.getReindeerWeariness();
        if (valueThis < valueOther) {
            compareValue = -1;
        } else if (valueThis < valueOther) {
            compareValue = 1;
        } else {
            compareValue = 0;
        }
        return compareValue;
    }

    @Override
    public String toString() {
        return "SolutionSam [getReindeerWeariness()=" + getReindeerWeariness() + "]";
    }

    /**
     * @return the tours of this solution.
     */
    public List<Iterable<Gift>> getTours() {
        HashMap<Integer, LinkedList<Gift>> tourIdTour = new HashMap<>();
        for (GiftTour giftTour : giftsTour) {
            LinkedList<Gift> tour = tourIdTour.get(giftTour.getTourId());
            if (tour == null) {
                tour = new LinkedList<>();
                tourIdTour.put(giftTour.getTourId(), tour);
            }
            tour.add(giftTour);
        }
        LinkedList<Iterable<Gift>> result = new LinkedList<>();
        for (LinkedList<Gift> tour : tourIdTour.values()) {
            result.add(tour);
        }
        return result;
    }

    public void setGiftTour(ArrayList<GiftTour> giftsTour) {
        this.giftsTour = giftsTour;
    }
}
