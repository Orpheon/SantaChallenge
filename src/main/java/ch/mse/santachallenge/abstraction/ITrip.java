package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.Gift;

import java.util.List;

public interface ITrip extends List<Gift> {
    int getId();
    Iterable<Gift> getDistributedGifts();
    double cost();
    double totalWeight();
}
