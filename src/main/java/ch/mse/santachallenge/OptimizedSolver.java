package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ISolver;
import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.utils.Solution;

import java.util.ArrayList;
import java.util.List;

public class OptimizedSolver implements ISolver {
    //This solver takes some properties of the specific problem into account (Like having alot of gifts @ north pole)
    public List<ITrip> Solve(Iterable<Gift> gifts){
        var solver = new MinimalisticSliceSolver();
        List<ITrip> solution = new ArrayList<>();
        ArrayList<Gift> southPole = new ArrayList<>();
        ArrayList<Gift> others = new ArrayList<>();
        for(var gift : gifts){
            if(gift.getLocation().getLatitude() < -60.0){
                southPole.add(gift);
            }else{
                others.add(gift);
            }
        }
        var optimizer3 = new MergeTripOptimizer();

        List<ITrip> othersSolution = solver.Solve(others);
        List<ITrip> southPoleSolution = solver.Solve(southPole);

        var southPoleCost = Solution.totalCostOf(southPoleSolution);
        var cost = southPoleCost + Solution.totalCostOf(othersSolution);
        System.out.println("Initial cost: " + cost);
        for (int i = 0; i < 1000; i++){
            othersSolution = optimizer3.optimize(othersSolution);
            cost = southPoleCost + Solution.totalCostOf(othersSolution);
            System.out.println("Current cost after merge: " + cost);
        }
        solution.addAll(southPoleSolution);
        solution.addAll(othersSolution);
        var optimizer1 = new SwapInsideTripOptimizer();
        var optimizer2 = new SwapBetweenTripOptimizer();
        var iteration = 0;
        while(iteration < 10000){
            solution = optimizer1.optimize(solution);
            System.out.println("Current cost after swap inside: " + Solution.totalCostOf(solution));
            solution = optimizer2.optimize(solution);
            System.out.println("Current cost after swap between: " + Solution.totalCostOf(solution));
        }
        return solution;
    }
}
