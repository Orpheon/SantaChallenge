package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.IOptimizer;
import ch.mse.santachallenge.abstraction.ITrip;
import java.util.*;

public class SliceOptimizer implements IOptimizer {
    private final double worstGiftsSeparationDistance = 10.0;
    private final double maxMergeDistance = 3.0;
    private final Random rm = new Random();
    private double totalCost;
    private TreeMap<Double, PrecalculatedEntry> entries;

    public List<ITrip> optimize(Collection<ITrip> currentSolution) {
        initEntries(currentSolution);
        var runsWithoutOptimization = 0;
        while(runsWithoutOptimization < 10000){
            var entryToOptimize = getNextEntryToOptimize();
            var mergableEntryFound = false;
            var positiveSearchDistance = 0.0;
            var higherEntry = entryToOptimize;
            var negativeSearchDistance = 0.0;
            var lowerEntry = entryToOptimize;
            while(!mergableEntryFound && (positiveSearchDistance < maxMergeDistance || negativeSearchDistance < maxMergeDistance)){
                if(positiveSearchDistance < maxMergeDistance){
                    higherEntry = entries.higherEntry(higherEntry.getKey());
                    if(higherEntry != null){
                        positiveSearchDistance = Math.abs(higherEntry.getKey() - entryToOptimize.getKey());
                        mergableEntryFound = tryMerge(entryToOptimize.getValue(), higherEntry.getValue());
                    }else{
                        positiveSearchDistance = Double.MAX_VALUE;
                    }


                }
                if(!mergableEntryFound && negativeSearchDistance < maxMergeDistance){
                    lowerEntry = entries.lowerEntry(lowerEntry.getKey());
                    if(lowerEntry != null){
                        negativeSearchDistance = Math.abs(lowerEntry.getKey() - entryToOptimize.getKey());
                        mergableEntryFound = tryMerge(entryToOptimize.getValue(), lowerEntry.getValue());
                    }else{
                        negativeSearchDistance = Double.MAX_VALUE;
                    }
                }
            }
            if(mergableEntryFound){
                runsWithoutOptimization = 0;
            }else{
                runsWithoutOptimization++;
            }
        }
        var solution = new ArrayList<ITrip>();
        for(var trip : entries.values()){
            solution.add(trip.tripOrderedByLatitude);
        }
        return solution;
    }

    private boolean tryMerge(PrecalculatedEntry entryToOptimize, PrecalculatedEntry entryToMerge){
        PrecalculatedEntry outA, outB;
        var oldCost = entryToMerge.cost + entryToOptimize.cost;
        var availableSpareWeight = entryToMerge.spareWeight;
        var exchangedGiftsWeight = 0.0;
        var exchangedGifts = new ArrayList<Gift>();
        var nextGift = entryToMerge.tripOrderedByLatitude.get(0);
        while(availableSpareWeight < entryToOptimize.worstGiftsWeight
                && exchangedGiftsWeight <= entryToOptimize.spareWeightWithoutWorstGifts
                && nextGift != null
                && nextGift.getLocation().getLatitude() <= entryToOptimize.latitudeAfterWorst){
            exchangedGifts.add(nextGift);
            exchangedGiftsWeight += nextGift.getWeight();
            if(entryToMerge.tripOrderedByLatitude.size() > exchangedGifts.size()){
                nextGift = entryToMerge.tripOrderedByLatitude.get(exchangedGifts.size());
            }else{
                nextGift = null;
            }
        }
        if(availableSpareWeight >= entryToOptimize.worstGiftsWeight && exchangedGiftsWeight <= entryToOptimize.spareWeightWithoutWorstGifts){
            var optimizedTrip = new Trip(entryToOptimize.tripOrderedByLatitude);
            var mergedTrip = new Trip(entryToMerge.tripOrderedByLatitude);
            for(var gift : entryToOptimize.worstGifts){
                optimizedTrip.remove(gift);
                mergedTrip.add(gift);
            }
            for(var gift : exchangedGifts){
                optimizedTrip.add(gift);
                mergedTrip.remove(gift);
            }
            var newCost = 0.0;
            if (optimizedTrip.size() > 1){
                outA = new PrecalculatedEntry(optimizedTrip);
                newCost += outA.cost;
            }else{
                outA = null;
            }
            outB = new PrecalculatedEntry(mergedTrip);
            newCost += outB.cost;
            if(newCost < oldCost){
                removeEntry(entryToMerge);
                removeEntry(entryToOptimize);
                addEntry(outA);
                addEntry(outB);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private void initEntries(Iterable<ITrip> currentSolution) {
        entries = new TreeMap<>(Double::compareTo);
        totalCost = 0.0;
        for(var trip : currentSolution){
            var entry = new PrecalculatedEntry(trip);
            addEntry(entry);
        }
    }

    private void addEntry(PrecalculatedEntry entry) {
        if(entry != null){
            totalCost += entry.cost;
            entries.put(entry.averageLongitude, entry);
        }
    }

    private void removeEntry(PrecalculatedEntry entry) {
        totalCost -= entry.cost;
        entries.remove(entry.averageLongitude);
    }

    private Map.Entry<Double, SliceOptimizer.PrecalculatedEntry> getNextEntryToOptimize() {
        Map.Entry<Double, SliceOptimizer.PrecalculatedEntry> entryToMerge = null;
        var firstKey = entries.firstKey();
        var valueSpan = Math.abs(firstKey - entries.lastKey());
        while(entryToMerge == null){
            var nextKey = firstKey + (rm.nextDouble() * valueSpan);
            if(rm.nextBoolean()){
                entryToMerge = entries.lowerEntry(nextKey);
            }else{
                entryToMerge = entries.higherEntry(nextKey);
            }
        }
        return entryToMerge;
    }

    private class PrecalculatedEntry{
        private final double worstGiftsWeight;
        private final List<Gift> worstGifts;
        private final double spareWeight;
        private final double spareWeightWithoutWorstGifts;
        private final double latitudeAfterWorst;
        private final Trip tripOrderedByLatitude;
        private final double cost;
        private final double averageLongitude;

        public PrecalculatedEntry(ITrip trip){
            if(trip == null || trip.size() < 1){
                throw new IllegalArgumentException("trip must be valid.");
            }
            worstGifts = new ArrayList<>();
            spareWeight = Constants.maxWeight - trip.totalWeight();
            var giftsOrderedByLatitude = new TreeMap<Double, Gift>(Double::compareTo);
            var longitudeAccu = 0.0;
            for(var gift : trip) {
                giftsOrderedByLatitude.put(gift.getLocation().getLatitude(), gift);
                longitudeAccu += gift.getLocation().getLongitude();
            }
            averageLongitude = longitudeAccu / trip.size();
            initWorstGifts(giftsOrderedByLatitude);
            latitudeAfterWorst = getLatitudeAfterWorst(giftsOrderedByLatitude);
            worstGiftsWeight = getWorstGiftsWeight();
            spareWeightWithoutWorstGifts = spareWeight + worstGiftsWeight;
            tripOrderedByLatitude = new Trip(giftsOrderedByLatitude.values());
            Collections.reverse(tripOrderedByLatitude);
            cost = tripOrderedByLatitude.cost();
        }

        private double getLatitudeAfterWorst(TreeMap<Double, Gift> giftsOrderedByLatitude) {
            var lastWorstGift = worstGifts.get(worstGifts.size() - 1);
            var entryAfterLastWorstGift = giftsOrderedByLatitude.lowerEntry(lastWorstGift.getLocation().getLatitude());
            if(entryAfterLastWorstGift != null){
                return entryAfterLastWorstGift.getKey();
            }else{
                return 0.0;
            }
        }

        private void initWorstGifts(TreeMap<Double, Gift> giftsOrderedByLatitude) {
            var nextEntry = giftsOrderedByLatitude.firstEntry();
            var lastLatitude = nextEntry.getKey();
            while(nextEntry != null && Math.abs(lastLatitude - nextEntry.getKey()) < worstGiftsSeparationDistance){
                worstGifts.add(nextEntry.getValue());
                lastLatitude = nextEntry.getKey();
                nextEntry = giftsOrderedByLatitude.higherEntry(lastLatitude);
            }
        }

        private double getWorstGiftsWeight() {
            var weightAccu = 0.0;
            for(var gift : worstGifts){
                weightAccu += gift.getWeight();
            }
            return weightAccu;
        }
    }
}
