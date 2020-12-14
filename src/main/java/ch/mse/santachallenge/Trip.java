package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;
import java.util.Collection;
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
    public Trip(Collection<Gift> gifts){
        super(gifts);
        this.id = id_template++;
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

    @Override
    public double avgLongitude() {
        if (size() == 0){
            return 0.0;
        }
        var longitudeBase = get(0).getLocation().getLongitude() + 180.0;
        //The longitude base is used because -180.0 + 180.0 would result in avg 0 even tho they are at the same position
        var longitudeAccu = 0.0;
        for(var gift : this){
            var longitude = longitudeBase + ((gift.getLocation().getLongitude() - longitudeBase + 720.0) % 360.0);
            longitudeAccu += longitude;
        }
        var avgLongitude = longitudeAccu / size();
        return avgLongitude;
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
