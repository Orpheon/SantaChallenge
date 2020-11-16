package ch.mse.santachallenge.abstraction;

import ch.mse.santachallenge.*;

import java.util.Enumeration;

public interface ISolver {
    Iterable<Trip> Solve(Iterable<Gift> gifts);
}
