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
        solution.addAll(solver.Solve(southPole));
        solution.addAll(solver.Solve(others));
        var optimizer1 = new SwapInsideTripOptimizer();
        var optimizer2 = new SwapBetweenTripOptimizer();
        var iteration = 0;
        while(iteration < 10000000){
            solution = optimizer1.optimize(solution);
            System.out.println("Current cost after swap inside: " + Solution.totalCostOf(solution));
            solution = optimizer2.optimize(solution);
            System.out.println("Current cost after swap between: " + Solution.totalCostOf(solution));
        }
        return solution;
    }
}
