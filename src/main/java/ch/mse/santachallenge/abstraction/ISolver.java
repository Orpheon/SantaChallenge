package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.*;

import java.util.Enumeration;
import java.util.List;

public interface ISolver {
    List<ITrip> Solve(Iterable<Gift> gifts);
}
