package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;
import java.util.ListIterator;

public class Trip extends ArrayList<Gift> implements ITrip {
    private final int id;
    private static int id_template = 0;

    public Trip() {
        this.id = id_template++;
    }
    public Trip(int id) {
        this.id = id;
    }

    public double cost() {
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

    public double totalWeight() {
        double weight = Constants.sledWeight;
        for (Gift gift : this) {
            weight += gift.getWeight();
        }
        return weight;
    }

    public int getId() {
        return id;
    }

    public Iterable<Gift> getDistributedGifts() {
        return this;
    }
}
