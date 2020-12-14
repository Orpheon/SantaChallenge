package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.*;

public class SwapBetweenTripOptimizer implements IOptimizer {
    private int maxIterations = 100000;
    private int maxIndexDistance = 10;
    private ArrayList<ITrip> solutionAsArray;
    private ArrayList<Double> spareWeights;
    private Random rm;

    public void setMaxIndexDistance(int maxIndexDistance){
        this.maxIndexDistance = maxIndexDistance;
    }

    @Override
    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        TreeMap<Double, ITrip> solution = new TreeMap<>();
        for(var trip : currentSolution){
            solution.put(trip.avgLongitude(), trip);
        }
        solutionAsArray = new ArrayList<ITrip>(solution.values());
        spareWeights = new ArrayList<Double>(solutionAsArray.size());
        rm = new Random();
        for(int i = 0; i < solutionAsArray.size(); i++){
            spareWeights.add(0.0);
        }
        for(int i = 0; i < solutionAsArray.size(); i++){
            UpdateSpareWeight(i);
        }
        var iteration = 0;
        while(iteration < maxIterations){
            SwapBetweenTrips();
            iteration += 1;
        }
        return solutionAsArray;
    }

    private void SwapBetweenTrips() {
        var size = solutionAsArray.size();
        var indexA = rm.nextInt(size);
        var indexB =  (indexA + (rm.nextInt(maxIndexDistance * 2) - maxIndexDistance) + size) % size;
        var tripA = solutionAsArray.get(indexA);
        var indexGiftA = rm.nextInt(tripA.size());
        var giftA = tripA.get(indexGiftA);
        var tripB = solutionAsArray.get(indexB);
        var indexGiftB = rm.nextInt(tripB.size());
        var giftB = tripB.get(indexGiftB);
        if (CanSwap(indexA, indexB, giftA, giftB)){
            var beforeCost = tripA.cost() + tripB.cost();
            tripA.set(indexGiftA, giftB);
            tripB.set(indexGiftB, giftA);
            if (tripA.cost() + tripB.cost() <= beforeCost){
                UpdateSpareWeight(indexA);
                UpdateSpareWeight(indexB);
            }else {
                //Reverse change
                tripA.set(indexGiftA, giftA);
                tripB.set(indexGiftB, giftB);
            }
        }
    }

    private void UpdateSpareWeight(int index) {
        var spareWeight = Constants.maxWeight - solutionAsArray.get(index).totalWeight();
        spareWeights.set(index, spareWeight);
    }

    private boolean CanSwap(int indexA, int indexB, Gift giftA, Gift giftB) {
        return (giftA.getWeight() - giftB.getWeight()) < spareWeights.get(indexB) && (giftB.getWeight() - giftA.getWeight()) < spareWeights.get(indexA);
    }
}
