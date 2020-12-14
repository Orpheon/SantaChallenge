package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.utils.Solution;
import jdk.dynalink.linker.ConversionComparator;

import java.util.*;

public class EarthPartitionSolver {
    public Iterable<ITrip> solve(Collection<Partition> partitions){
        var unusedPartitions = new TreeMap<Double, Partition>(Comparator.reverseOrder());
        for(var partition : partitions){
            unusedPartitions.put(partition.getAvgLatitude(), partition);
        }
        var southPole = unusedPartitions.pollLastEntry().getValue();
        var solver = new MinimalisticSliceSolver();
        var solution = new ArrayList<ITrip>();
        for(var partition : partitions){
            solution.addAll(solver.Solve(partition));
        }
        var result = Solution.totalCostOf(solution);
        return solution;
    }
}
