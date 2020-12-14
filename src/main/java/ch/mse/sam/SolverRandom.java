/**
 * 
 */
package ch.mse.sam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import ch.mse.santachallenge.Gift;

/**
 * A solver, which creates random solution.
 * 
 * @author Sam
 *
 */
public class SolverRandom implements Solvable {

    @Override
    public SolutionSam solve(LinkedList<Gift> gifts) {
        Collections.shuffle(gifts);
        ArrayList<GiftTour> giftTours = new ArrayList<>();
        int tourId = 0;
        double currentTourWeight = 0.0;
        for (Gift gift : gifts) {
            if (gift.getWeight() + currentTourWeight > ProgramSam.CARGO_LIMIT) {
                tourId++;
                currentTourWeight = 0.0;
            }
            giftTours.add(new GiftTour(gift, tourId));
            currentTourWeight += gift.getWeight();
        }
        return new SolutionSam(giftTours);
    }
}
