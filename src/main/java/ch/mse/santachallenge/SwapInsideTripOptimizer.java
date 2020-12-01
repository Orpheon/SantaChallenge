package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SwapInsideTripOptimizer implements IOptimizer {
    private int maxIterations = 100000;
    @Override
    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        ArrayList<ITrip> solution = new ArrayList<>();
        var rm = new Random();
        for(var trip : currentSolution){
            solution.add(trip);
        }
        var iteration = 0;
        while(iteration < maxIterations){
            var tripToOptimize = solution.get(rm.nextInt(solution.size()));
            var beforeCost = tripToOptimize.cost();
            var indexA = rm.nextInt(tripToOptimize.size());
            var indexB = rm.nextInt(tripToOptimize.size());
            if(indexA == indexB){
                continue;
            }
            var giftA = tripToOptimize.get(indexA);
            var giftB = tripToOptimize.get(indexB);
            tripToOptimize.set(indexA, giftB);
            tripToOptimize.set(indexB, giftA);
            var nowCost = tripToOptimize.cost();
            if(nowCost > beforeCost){
                //reverse action
                tripToOptimize.set(indexA, giftA);
                tripToOptimize.set(indexB, giftB);
            }
            iteration++;
        }
        return solution;
    }
}
