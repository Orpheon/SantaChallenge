package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.utils.Solution;
import jdk.dynalink.linker.ConversionComparator;

import java.util.*;

public class EarthPartitionSolver {
    public Iterable<ITrip> solve(Collection<Partition> partitions){
        var unusedPartitions = new TreeMap<Double, Partition>();
        for(var partition : partitions){
            unusedPartitions.put(partition.getAvgLatitude(), partition);
        }

        var southPole = unusedPartitions.pollFirstEntry().getValue();
        var solver = new MinimalisticSliceSolver();
        List<ITrip> solution = new ArrayList<ITrip>();
        while(!unusedPartitions.isEmpty()){
            var partition = unusedPartitions.pollFirstEntry().getValue();
            var newSolutions = solver.Solve(partition);
            var lastSolution = newSolutions.get(newSolutions.size() - 1);
            var lastSolutionAvgLongitude = lastSolution.avgLongitude();
            var topMostGift = Collections.max(lastSolution, Comparator.comparingDouble(o -> o.getLocation().getLatitude()));
            newSolutions.remove(lastSolution);
            solution.addAll(newSolutions);
            var found = false;
            for(var unusedPartition : unusedPartitions.values()){
                for(var gift : unusedPartition){
                    if(gift.getLocation().getLatitude() > topMostGift.getLocation().getLatitude() && Math.abs(gift.getLocation().getLongitude() - lastSolutionAvgLongitude) < 1.0){
                        found = true;
                    }
                }
                if(found){
                    unusedPartition.addAll(lastSolution);
                    break;
                }
            }
            if(!found){
                southPole.addAll(lastSolution);
            }
        }

        solution.addAll(solver.Solve(southPole));
        var result = Solution.totalCostOf(solution);
        var opt = new NearestNeighbourOptimizer();
        var newSolution = opt.optimize(solution);
        var newResult = Solution.totalCostOf(newSolution);
        Solution.assertValid(solution);
        var optimizer = new SwapBetweenTripOptimizer();
        for(int i = 0; i < 1; i++){
            solution = optimizer.optimize(solution);
            System.out.println("Cost = " + Solution.totalCostOf(solution));
        }
        return solution;
    }
}
