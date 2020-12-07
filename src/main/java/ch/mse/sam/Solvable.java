package ch.mse.sam;

import java.util.LinkedList;

import ch.mse.santachallenge.Gift;

public interface Solvable {

    /**
     * Creates tours, that all gifts are delivered.
     * 
     * @param gifts the gifts to deliver
     * @return a solution
     */
    SolutionSam solve(LinkedList<Gift> gifts);
}