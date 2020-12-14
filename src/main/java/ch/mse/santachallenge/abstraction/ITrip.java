package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.Gift;

import java.util.List;

public interface ITrip extends List<Gift> {
    int getId();

    Iterable<Gift> getDistributedGifts();

    double totalWeight();

    double cost();
    double costSkipGift(Gift skipGift);
    double costExtraGift(Gift extraGift, int pos);
    double avgLongitude();
}
