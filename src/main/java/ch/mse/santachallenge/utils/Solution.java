package ch.mse.santachallenge.utils;

import ch.mse.santachallenge.Constants;
import ch.mse.santachallenge.abstraction.ITrip;

import java.util.TreeSet;

public class Solution {
    public static double totalCostOf(Iterable<ITrip> solution){
        var cost = 0.0;
        for(var trip : solution){
            cost += trip.cost();
        }
        return cost;
    }

    public static void assertValid(Iterable<ITrip> solution){
        var giftCount = 0;
        var missingIds = new TreeSet<Integer>();
        var firstId = 1;
        for(int i = firstId; i < firstId + Constants.giftCount; i++) {
            missingIds.add(i);
        }
        for (var trip : solution){
            if(trip.totalWeight() > Constants.maxWeight){
                throw new AssertionError("Trip weight is above maximum");
            }
            for(var gift : trip){
                giftCount += 1;
                if(!missingIds.remove(gift.getId())){
                    throw new AssertionError(String.format("Gift with id %s is visited twice or more.", gift.getId()));
                }
            }
        }
        if(giftCount != Constants.giftCount){
            throw new AssertionError(String.format("Solution does not contain correct amount of gifts. Expected: %s, Actual: %s", Constants.giftCount, giftCount));
        }
        if(!missingIds.isEmpty()){
            throw new AssertionError(String.format("Some gifts are missing in the solution."));
        }
    }
}
