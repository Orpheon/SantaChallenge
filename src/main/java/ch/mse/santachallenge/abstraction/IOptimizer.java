package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.Gift;

import java.util.Collection;
import java.util.List;

public interface IOptimizer {
    List<ITrip> optimize(Collection<ITrip> currentSolution);
}
