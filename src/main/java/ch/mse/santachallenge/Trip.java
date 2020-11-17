package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;

import java.util.ArrayList;

public class Trip extends ArrayList<Gift> implements ITrip {
    private final int id;

    public Trip(int id) {
        this.id = id;
    }

    public double cost(){
        //TODO
        return 0.0;
    }

    public int getId() {
        return id;
    }

    public Iterable<Gift> getDistributedGifts() {
        return this;
    }
}
