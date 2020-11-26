package ch.mse.sam;

import java.util.LinkedList;

import ch.mse.santachallenge.Gift;

public interface Solvable {

    LinkedList<LinkedList<Gift>> solve(LinkedList<Gift> gifts);

}