package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SwapInsideTripOptimizer implements IOptimizer {
    private int maxIterations = 100000;
    private Random rm = new Random();
    @Override
    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        ArrayList<ITrip> solution = new ArrayList<>();
        for(var trip : currentSolution){
            solution.add(trip);
        }
        var iteration = 0;
        while(iteration < maxIterations){
            var tripToOptimize = solution.get(rm.nextInt(solution.size()));
            OptimizeTrip(tripToOptimize);
            iteration++;
        }
        return solution;
    }

    public void OptimizeTrip(ITrip tripToOptimize) {
        OptimizeTrip(tripToOptimize, 1);
    }

    public void OptimizeTrip(ITrip tripToOptimize, int iterations) {
        if (tripToOptimize.size() < 2){
            return;
        }
        for(int i = 0; i < iterations; i++){
            var beforeCost = tripToOptimize.cost();
            var indexA = -1;
            var indexB = -1;
            while(indexA == indexB){
                indexA = rm.nextInt(tripToOptimize.size());
                indexB = rm.nextInt(tripToOptimize.size());
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
        }
    }
}
