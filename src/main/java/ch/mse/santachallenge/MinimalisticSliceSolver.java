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
            Trip trip = new Trip();
            trip.addAll(slice.values());
            Collections.reverse(trip);
            var beforeTrip = trip;
            var beforeCost = beforeTrip.cost();
            if(weight + entry.getValue().getWeight() > Constants.maxWeight){
                trips.add(trip);
                slice.clear();
                weight = Constants.sledWeight;
                slice.put(entry.getValue().getLocation().getLatitude(), entry.getValue());
            }else{
                slice.put(entry.getValue().getLocation().getLatitude(), entry.getValue());
                var nowTrip = new Trip();
                nowTrip.addAll(slice.values());
                Collections.reverse(nowTrip);
                var nowCost = nowTrip.cost();
                var singleTrip = new Trip();
                singleTrip.add(entry.getValue());
                var singleCost = singleTrip.cost();

                if(singleCost < nowCost - beforeCost){
                    //It is cheaper to create a new trip so we will do it!
                    trips.add(beforeTrip);
                    slice.clear();
                    weight = Constants.sledWeight;
                    slice.put(entry.getValue().getLocation().getLatitude(), entry.getValue());
                }
            }

            weight += entry.getValue().getWeight();
            entry = unusedGifts.pollFirstEntry();
        }
        return trips;
    }
}
