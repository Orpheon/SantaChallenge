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
        totalCost += weight * Constants.northPole.distanceTo(lastLocation);
        return totalCost;
    }

    public double costSkipGift(Gift skipGift) {
        double totalCost = 0.0;
        double weight = Constants.sledWeight;
        Location lastLocation = Constants.northPole;
        ListIterator<Gift> it = listIterator(size());
        while (it.hasPrevious()) {
            Gift gift = it.previous();
            if (gift != skipGift) {
                totalCost += weight * gift.getLocation().distanceTo(lastLocation);
                weight += gift.getWeight();
                lastLocation = gift.getLocation();
            }
        }
        totalCost += weight * Constants.northPole.distanceTo(lastLocation);
        return totalCost;
    }

    public double costExtraGift(Gift extraGift, int pos) {
        double totalCost = 0.0;
        double weight = Constants.sledWeight;
        Location lastLocation = Constants.northPole;
        // Apologies for the ugly code
        for (int idx = size(); idx >= 0; --idx) {
            if (idx == pos) {
                totalCost += weight * extraGift.getLocation().distanceTo(lastLocation);
                weight += extraGift.getWeight();
                lastLocation = extraGift.getLocation();
            }
            if (idx <= size() - 1) {
                Gift gift = get(idx);
                totalCost += weight * gift.getLocation().distanceTo(lastLocation);
                weight += gift.getWeight();
                lastLocation = gift.getLocation();
            }
        }
        totalCost += weight * Constants.northPole.distanceTo(lastLocation);
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
