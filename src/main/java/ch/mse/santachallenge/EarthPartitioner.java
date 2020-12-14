package ch.mse.santachallenge;

import java.util.*;

public class EarthPartitioner {
    private final double slizeWidth = 2.5;
    private final double separationWidth = 10.0;
    private final TreeMap<Double, Gift> unusedGifts = new TreeMap<>();
    private final List<Partition> partitions = new ArrayList<>();
    private final List<Partition> activePartitions = new ArrayList<>();
    private final List<Partition> nextPartitions = new ArrayList<>();

    public Collection<Partition> partition(Iterable<Gift> gifts){
        unusedGifts.clear();
        partitions.clear();
        activePartitions.clear();
        nextPartitions.clear();
        Partition southPole = new Partition();
        var others = new ArrayList<Gift>();
        for(var gift : gifts){
            if(gift.getLocation().getLatitude() < -60.0){
                southPole.add(gift);
            }else{
                others.add(gift);
            }
        }
        for(var gift : others){
            unusedGifts.put(gift.getLocation().getLongitude(), gift);
        }
        while(!unusedGifts.isEmpty()) {
            var slice = getNextSlice(unusedGifts);
            var newPartitions = new ArrayList<Partition>();
            while(!slice.isEmpty()){
                newPartitions.add(getNextPartitionOfSlice(slice));
            }
            var remainingNewPartitions = new ArrayList<Partition>(newPartitions);
            var overlapFound = true;
            while(overlapFound){
                overlapFound = false;
                var bestOverlap = 0.0;
                Partition bestOverlapActive = null;
                Partition bestOverlapNew = null;
                for(var newPartition : remainingNewPartitions){
                    var active = getPartitionWithBestOverlap(newPartition, activePartitions);
                    if (active != null){
                        overlapFound = true;
                        var overlap = newPartition.getEdgeOverlapWith(active);
                        if(overlap > bestOverlap){
                            bestOverlap = overlap;
                            bestOverlapActive = active;
                            bestOverlapNew = newPartition;
                        }
                    }
                }
                if(overlapFound){
                    remainingNewPartitions.remove(bestOverlapNew);
                    activePartitions.remove(bestOverlapActive);
                    bestOverlapActive.add(bestOverlapNew);
                    nextPartitions.add(bestOverlapActive);
                }
            }
            partitions.addAll(remainingNewPartitions);
            nextPartitions.addAll(remainingNewPartitions);
            activePartitions.clear();
            activePartitions.addAll(nextPartitions);
            nextPartitions.clear();
        }
        var partitionsToRemove = new ArrayList<Partition>();
        for(var partition : partitions){
            if(partition.size() < 5){
                partitionsToRemove.add(partition);
                for(var gift : partition){
                    southPole.add(gift);
                }
            }
        }
        for(var partition : partitionsToRemove){
            partitions.remove(partition);
        }
        partitions.add(southPole);
        return partitions;
    }

    private void addToActivePartition(Partition activePartition, Partition partition) {
        activePartition.add(partition);
        activePartitions.remove(activePartition);
        nextPartitions.add(activePartition);
    }

    private void addNewPartition(Partition partition) {
        nextPartitions.add(partition);
        partitions.add(partition);
    }

    private Partition getPartitionWithBestOverlap(Partition partition, Iterable<Partition> others) {
        var maxOverlap = 0.0;
        Partition maxOverlapPartition = null;
        for(var other : others){
            var overlap = partition.getEdgeOverlapWith(other);
            if(overlap > maxOverlap){
                maxOverlap = overlap;
                maxOverlapPartition = other;
            }
        }
        return maxOverlapPartition;
    }

    private Partition getNextPartitionOfSlice(TreeMap<Double, Gift> slice) {
        var partition = new Partition();
        Map.Entry<Double, Gift> nextEntry;
        do{
            nextEntry = slice.pollFirstEntry();
        }while (nextEntry != null && partition.tryAdd(nextEntry.getValue(), separationWidth));
        return partition;
    }


    private TreeMap<Double, Gift> getNextSlice(TreeMap<Double, Gift> unusedGifts) {
        var maxLongitude = unusedGifts.firstKey() + slizeWidth;
        var currentLongitude = unusedGifts.firstKey();
        TreeMap<Double, Gift> slice = new TreeMap<>(Comparator.reverseOrder());
        maxLongitude += slizeWidth;
        while (currentLongitude < maxLongitude && !unusedGifts.isEmpty()) {
            var entry = unusedGifts.pollFirstEntry();
            var gift = entry.getValue();
            slice.put(gift.getLocation().getLatitude(), gift);
            currentLongitude = entry.getKey();
        }
        return slice;
    }
}
