package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.Constants;

import java.util.ArrayList;
import java.util.ListIterator;


public class Trip extends ArrayList<Gift> implements ITrip {
    private final int id;

    public Trip(int id) {
        this.id = id;
    }

    public double cost(){
        double totalCost = 0.0;
        double weight = Constants.sledWeight;
        Location lastLocation = Constants.northPole;
        ListIterator<Gift> it = listIterator(size());
        while (it.hasPrevious()) {
            Gift gift = it.previous();
            totalCost += weight * gift.getLocation().distanceTo(lastLocation);
            weight += gift.getWeight();
            lastLocation = gift.getLocation();
        }
        return totalCost;
    }

    public int getId() {
        return id;
    }

    public Iterable<Gift> getDistributedGifts() {
        return this;
    }
}
