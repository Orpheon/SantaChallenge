package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.Gift;

public interface ITrip {
    int getId();
    Iterable<Gift> getDistributedGifts();
}
