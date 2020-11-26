package ch.mse.sam;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import ch.mse.santachallenge.Gift;

public class Utils {
    private static final int WEIGTH_LIMIT = 1000;

    /**
     * Checks if solution is valid.
     * <ul>
     * <li>each tour has to take the cargo limit in account</li>
     * <li>each gift has to be delivered</li>
     * </ul>
     * 
     * @param gifts
     * @param solution
     * @return
     */
    public static boolean isTourValid(LinkedList<Gift> gifts, LinkedList<LinkedList<Gift>> solution) {
        boolean retVal = true;
        HashMap<Gift, Integer> visitCount = new HashMap<>();
        for (Gift gift : gifts) {
            visitCount.put(gift, 0);
        }
        Iterator<LinkedList<Gift>> it = solution.iterator();
        int id = 0;
        while (it.hasNext()) {
            LinkedList<Gift> tour = it.next();
            int weigth = 0;
            for (Gift gift : tour) {
                visitCount.put(gift, visitCount.get(gift) + 1);
                weigth += gift.getWeight();
            }
            // each tour has to take the cargo limit in account
            if (weigth > WEIGTH_LIMIT) {
                System.err.println("The tour " + id + " is over the limit and has the weigth " + weigth + ".");
                retVal = false;
            }
            id++;
        }
        // each gift has to be delivered
        for (Entry<Gift, Integer> entry : visitCount.entrySet()) {
            if (entry.getValue() != 1) {
                System.err.println("The gift " + entry.getKey() + " was visited " + entry.getValue() + " times.");
                retVal = false;
            }
        }
        return retVal;
    }
}
