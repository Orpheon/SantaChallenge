package ch.mse.santachallenge;

import java.util.ArrayList;

public class Trip extends ArrayList<Gift> {
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
}
