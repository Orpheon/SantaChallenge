package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.Gift;

import java.util.List;

public interface IOptimizer {
    List<ITrip> optimize(Iterable<ITrip> currentSolution);
}
