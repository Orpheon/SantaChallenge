package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.*;

public class SwapBetweenTripOptimizer implements IOptimizer {
    private int maxIterations = 100000;
    @Override
    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        TreeMap<Double, ITrip> solution = new TreeMap<>();
        for(var trip : currentSolution){
            var longitudeAccu = 0.0;
            for(var gift : trip){
                longitudeAccu += gift.getLocation().getLongitude();
            }
            var avgLongitude = longitudeAccu / trip.size();
            solution.put(avgLongitude, trip);
        }
        var solutionAsArray = new ArrayList<ITrip>(solution.values());
        //add border entries again to make further code easier
        var key = solution.firstKey(); //Will be somewhere around -180.0
        //solution.put()
        var rm = new Random();
        var iteration = 0;
        while(iteration < maxIterations){
            var tripA = solutionAsArray.get(rm.nextInt(solutionAsArray.size()));
            var indexA = rm.nextInt(tripA.size());
            var giftA = tripA.get(indexA);
        }
        return solutionAsArray;
    }
}
