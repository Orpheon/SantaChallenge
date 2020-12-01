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
        var optimizer = new SwapInsideTripOptimizer();
        var iteration = 0;
        while(iteration < 10000000){
            solution = optimizer.optimize(solution);
            if(iteration % 1000 == 0){
                System.out.println("Current cost: " + Solution.totalCostOf(solution));
            }
        }
        return solution;
    }
}
