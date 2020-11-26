/**
 * 
 */
package ch.mse.sam;

import java.util.Collections;
import java.util.LinkedList;

import ch.mse.santachallenge.Gift;

/**
 * @author Sam
 *
 */
public class SolverRandom {

    public LinkedList<LinkedList<Gift>> solve(LinkedList<Gift> gifts) {
        Collections.shuffle(gifts);
        LinkedList<LinkedList<Gift>> result = new LinkedList<>();
        result.add(new LinkedList<>());
        double currentTourWeight = 0;
        for (Gift gift : gifts) {
            LinkedList<Gift> currentTour;
            if (gift.getWeight() + currentTourWeight > ProgramSam.CARGO_LIMIT) {
                result.addFirst(new LinkedList<Gift>());
                currentTourWeight = 0;
            }
            currentTour = result.getFirst();
            currentTourWeight += gift.getWeight();
            currentTour.add(gift);
        }
        return result;
    }
}
