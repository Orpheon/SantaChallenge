package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ISolver;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class MinimalisticSliceSolver implements ISolver {

    public ArrayList<ITrip> Solve(Iterable<Gift> gifts) {
        ArrayList<ITrip> trips = new ArrayList<>();
        TreeMap<Double, Gift> unusedGifts = new TreeMap<>(Double::compareTo);
        TreeMap<Double, Gift> slice = new TreeMap<>(Double::compareTo);
        for(var gift : gifts){
            unusedGifts.put(gift.getLocation().getLongitude(), gift);
        }
        var entry = unusedGifts.pollFirstEntry();
        var weight = Constants.sledWeight;
        while(entry != null) {
            if(weight + entry.getValue().getWeight() > Constants.maxWeight){
                Trip trip = new Trip();
                trip.addAll(slice.values());
                Collections.reverse(trip);
                trips.add(trip);
                slice.clear();
                weight = Constants.sledWeight;
            }
            slice.put(entry.getValue().getLocation().getLatitude(), entry.getValue());
            weight += entry.getValue().getWeight();
            entry = unusedGifts.pollFirstEntry();
        }
        return trips;
    }
}
