package ch.mse.santachallenge.utils;

import ch.mse.santachallenge.abstraction.ITrip;

public class Solution {
    public static double totalCostOf(Iterable<ITrip> trips){
        var cost = 0.0;
        for(var trip : trips){
            cost += trip.cost();
        }
        return cost;
    }
}
