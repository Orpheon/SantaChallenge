package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ISolver;
import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.utils.Solution;

import java.util.*;

public class SliceSolver implements ISolver {

    @Override
    public List<ITrip> Solve(Iterable<Gift> gifts) {
        List<ITrip> bestSolution = null;
        double bestCost = Double.MAX_VALUE;
        for(double slizeWidth = 0.5; slizeWidth < 15.0; slizeWidth += 0.5){
            var solution = Solve(gifts, slizeWidth);
            var cost = Solution.totalCostOf(solution);
            if(cost < bestCost){
                bestSolution = solution;
                bestCost = cost;
            }
        }
        Solution.assertValid(bestSolution);
        return bestSolution;
    }

    private ArrayList<ITrip> Solve(Iterable<Gift> gifts, double slizeWidth) {
        ArrayList<ITrip> trips = new ArrayList<>();
        TreeMap<Double, Gift> unusedGifts = new TreeMap<>(Double::compareTo);
        TreeMap<Double, Gift> slice = new TreeMap<>(Double::compareTo);
        for(var gift : gifts){
            unusedGifts.put(gift.getLocation().getLongitude(), gift);
        }
        var maxLongitude = -180.0;
        var currentLongitude = -180.0;
        while(!unusedGifts.isEmpty()) {
            slice.clear();
            maxLongitude += slizeWidth;
            while (currentLongitude < maxLongitude && !unusedGifts.isEmpty()) {
                var entry = unusedGifts.pollFirstEntry();
                var gift = entry.getValue();
                slice.put(gift.getLocation().getLatitude(), gift);
                currentLongitude = entry.getKey();
            }
            //From here we can work with the calculated slice
            trips.addAll(getTripsForSlice(slice));
        }
        return trips;
    }

    private ArrayList<ITrip> getTripsForSlice(TreeMap<Double, Gift> slice) {
        var sliceTrips = new ArrayList<ITrip>();
        while (!slice.isEmpty()) {
            Trip trip = new Trip();
            var currentWeight = Constants.sledWeight;
            var nextEntry = slice.firstEntry();
            while (nextEntry != null) {
                var gift = nextEntry.getValue();
                if (currentWeight + gift.getWeight() <= Constants.maxWeight) {
                    trip.add(gift);
                    currentWeight += gift.getWeight();
                }
                nextEntry = slice.higherEntry(nextEntry.getKey());
            }
            Collections.reverse(trip);
            sliceTrips.add(trip);
            for (var gift : trip) {
                slice.remove(gift.getLocation().getLatitude());
            }
        }
        return sliceTrips;
    }
}
