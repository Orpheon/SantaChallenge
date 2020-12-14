package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.*;

public class MergeTripOptimizer implements IOptimizer {
    private int maxIterations = 10000;
    private int optimizeIterations = 1;
    private int mergeTripCount = 2;
    private int maxIndexDistance = 1;
    private TreeMap<Double, ITrip> solution;
    private ArrayList<Double> keysAsArray;
    private Random rm;
    private SwapInsideTripOptimizer optimizer = new SwapInsideTripOptimizer();

    public void setMaxIndexDistance(int maxIndexDistance){
        this.maxIndexDistance = maxIndexDistance;
    }

    @Override
    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        solution = new TreeMap<>(Double::compareTo);
        for(var trip : currentSolution){
            addTripToSolution(trip);
        }
        keysAsArray = new ArrayList<Double>(solution.keySet());
        rm = new Random();
        var iteration = 0;
        while(iteration < maxIterations){
            var tripsToMerge = getKeysToMerge(this.mergeTripCount);
            tryMerge(tripsToMerge);
            iteration += 1;
        }
        return new ArrayList<>(solution.values());
    }

    private boolean tryMerge(ArrayList<Double> keysToMerge) {
        var tripsToMerge = new ArrayList<ITrip>();
        for (var key : keysToMerge){
            tripsToMerge.add(solution.get(key));
        }
        var gifts = new TreeMap<Double, Gift>(Double::compareTo);
        var costBefore = 0.0;
        for(var trip : tripsToMerge){
            costBefore += trip.cost();
            for(var gift : trip){
                gifts.put(gift.getLocation().getLatitude(), gift);
            }
        }
        var maxNexTripCount = tripsToMerge.size();
        ArrayList<ITrip> newTrips = createNewTrips(gifts, maxNexTripCount);
        if (newTrips.size() <= tripsToMerge.size()){
            //No new trip was added so everything ok so everything ok so far.
            return tryReplace(keysToMerge, newTrips, costBefore);
        }else{
            return false;
        }
    }

    private boolean tryReplace(ArrayList<Double> keysToMerge, ArrayList<ITrip> newTrips, double maxCost) {
        var costAfter = 0.0;
        for (var newTrip : newTrips){
            optimizer.OptimizeTrip(newTrip, optimizeIterations);
            Collections.reverse(newTrip);
            costAfter += newTrip.cost();
        }
        if(costAfter < maxCost){
            for (var oldKey : keysToMerge){
                solution.remove(oldKey);
            }
            for (var newTrip : newTrips){
                addTripToSolution(newTrip);
            }
            keysAsArray = new ArrayList<Double>(solution.keySet());
            return true;
        }else{
            return false;
        }
    }

    private ArrayList<ITrip> createNewTrips(TreeMap<Double, Gift> gifts, int maxNexTripCount) {
        var newTrips = new ArrayList<ITrip>();
        var trip = new Trip();
        newTrips.add(trip);
        var entry = gifts.pollFirstEntry();
        var weight = Constants.sledWeight;
        while (entry != null && newTrips.size() <= maxNexTripCount){
            var gift = entry.getValue();
            if(weight + gift.getWeight() > Constants.maxWeight){
                trip = new Trip();
                newTrips.add(trip);
                weight = Constants.sledWeight;
            }
            trip.add(gift);
            weight += gift.getWeight();
            entry = gifts.pollFirstEntry();
        }
        return newTrips;
    }

    private ArrayList<Double> getKeysToMerge(int count) {
        var indexesToMerge = new ArrayList<Integer>();
        var size = keysAsArray.size();
        var firstIndex = rm.nextInt(size);
        indexesToMerge.add(firstIndex);
        while(indexesToMerge.size() < count){
            var nextIndex =  (firstIndex + (rm.nextInt(maxIndexDistance * 2) - maxIndexDistance) + size) % size;
            if (!indexesToMerge.contains(nextIndex)){
                indexesToMerge.add(nextIndex);
            }
        }
        var keysToMerge = new ArrayList<Double>();
        for(var index : indexesToMerge){
            keysToMerge.add(keysAsArray.get(index));
        }
        return keysToMerge;
    }

    private void addTripToSolution(ITrip trip) {
        solution.put(trip.avgLongitude(), trip);
    }
}
