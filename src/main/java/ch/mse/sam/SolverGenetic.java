/**
 * 
 */
package ch.mse.sam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import ch.mse.santachallenge.Gift;

/**
 * Solver, which uses the selection of the fittest to improve a set of
 * solutions, which is generated by a provided solver.
 * 
 * @author Sam
 *
 */
public class SolverGenetic implements Solvable {

    private int numberOfSolutions;
    private Solvable startSolver;
    private static Random random = new Random();

    public SolverGenetic(Solvable startSolver, int numberOfSolutions) {
        super();
        this.numberOfSolutions = numberOfSolutions;
        this.startSolver = startSolver;
    }

    @Override
    public SolutionSam solve(LinkedList<Gift> gifts) {
        LinkedList<SolutionSam> solutions = new LinkedList<>();
        long lastTimeMillis = System.currentTimeMillis();
        // generate initial solutions
        for (int i = 0; i < numberOfSolutions; i++) {
            solutions.add(startSolver.solve(gifts));
        }
        Collections.sort(solutions);
        // pick to 10 percent
        for (int generation = 0; generation < 500; generation++) {
            long currTimeMillis = System.currentTimeMillis();
            printFitness(solutions, generation, currTimeMillis - lastTimeMillis);
            lastTimeMillis = currTimeMillis;
            int newGenSize = solutions.size() / 10;
            List<SolutionSam> mates = solutions.subList(0, newGenSize);
            LinkedList<SolutionSam> newGeneration = new LinkedList<>();
            do {
                LinkedList<Integer> ids = getUniqueRandomNumbers(2, newGenSize, false);
                SolutionSam male = mates.get(ids.get(0));
                SolutionSam female = mates.get(ids.get(1));
                // combime them into new solutions
                SolutionSam child = mate(male, female);
                newGeneration.add(child);
            } while (newGeneration.size() < numberOfSolutions);
            Collections.sort(newGeneration);
            solutions = newGeneration;
        }
        return solutions.getLast();
    }

    private void printFitness(LinkedList<SolutionSam> solutions, int generation, long duration) {
        String sep = ";";
        if (generation == 0) {
            System.out.println("generation" + sep + "best fitness" + sep + "median fitness" + sep + "worst fitness"
                    + sep + "duration/ms");
        }
        double fitnessMedian = solutions.get(solutions.size() / 2).getReindeerWeariness();
        double fitnessBest = solutions.getFirst().getReindeerWeariness();
        double fitnessWorst = solutions.getLast().getReindeerWeariness();
        System.out.println(generation + sep + fitnessBest + sep + fitnessMedian + sep + fitnessWorst + sep + duration);
    }

    /**
     * Calculates the partially mapped crossover.
     * 
     * @param male       the tour, of which the elements outside of the range are
     *                   copied from
     * @param female     the tour, of which the range is copied from
     * @param rangeStart the start of the range, inclusive
     * @param rangeEnd   the end of the range, exclusive
     * @return the child
     */
    public static ArrayList<GiftTour> partiallyMappedCrossover(ArrayList<GiftTour> male, ArrayList<GiftTour> female,
            int rangeStart, int rangeEnd) {
        if (rangeStart >= rangeEnd) {
            throw new IllegalArgumentException(
                    "The start of the range (" + rangeStart + ") has to be smaller than the end (" + rangeEnd + ").");
        }
        if (male == null || male.isEmpty() || female == null || female.isEmpty()) {
            throw new IllegalArgumentException("male and female do have to contain data.");
        }
        GiftTour[] child = new GiftTour[male.size()];
        HashSet<GiftTour> genomsCopied = new HashSet<>();
        HashMap<GiftTour, GiftTour> mapping = new HashMap<>();
        // insert range from second parent
        for (int i = rangeStart; i < rangeEnd; i++) {
            GiftTour genom = female.get(i);
            child[i] = genom;
            genomsCopied.add(genom);
            mapping.put(female.get(i), male.get(i));
        }
        LinkedList<Integer> uncopiedIndexes = new LinkedList<>();
        Consumer<Integer> copy = (i) -> {
            GiftTour genom = male.get(i);
            if (!genomsCopied.contains(genom)) {
                child[i] = genom;
                genomsCopied.add(genom);
            } else {
                uncopiedIndexes.add(i);
            }
        };
        // copy all elements form first parent that do not have a conflict
        for (int i = 0; i < rangeStart; i++) {
            copy.accept(i);
        }
        for (int i = rangeEnd; i < child.length; i++) {
            copy.accept(i);
        }
        // add remaining elements according to the mapping
        for (Integer i : uncopiedIndexes) {
            GiftTour value = male.get(i);
            GiftTour gene;
            do {
                gene = mapping.get(value);
                value = gene;
            } while (genomsCopied.contains(gene));
            child[i] = gene;
        }
        return new ArrayList<>(Arrays.asList(child));
    }

    /**
     * Mates two specimens and creates a valid child in the process.
     * 
     * @param male   the first solution
     * @param female the second solution
     * @return the newly created solution
     */
    private SolutionSam mate(SolutionSam male, SolutionSam female) {
        SolutionSam retVal;
        do {
            int numGifts = male.getGiftsTour().size();
            LinkedList<Integer> randomNumbers = getUniqueRandomNumbers(2, numGifts, true);
            ArrayList<GiftTour> child = partiallyMappedCrossover(male.getGiftsTour(), female.getGiftsTour(),
                    randomNumbers.get(0), randomNumbers.get(1));
            int numMutations = random.nextInt(3);
            LinkedList<Swap> swaps = new LinkedList<Swap>();
            for (int i = 0; i < numMutations; i++) {
                LinkedList<Integer> pos = getUniqueRandomNumbers(2, numGifts, false);
                swaps.add(new Swap(pos.get(0), pos.get(1)));
            }
            child = mutate(child, swaps);
            retVal = new SolutionSam(child);
        } while (!retVal.isValid(false));
        return retVal;
    }

    /**
     * Mutates the entry at the provided positions.
     * 
     * @param child     the tour to mutate
     * @param positions the indexes to use
     * @return the mutated tour
     */
    public static ArrayList<GiftTour> mutate(ArrayList<GiftTour> child, Collection<Swap> positions) {
        for (Swap swap : positions) {
            GiftTour posA = child.set(swap.getPosA(), child.get(swap.getPosB()));
            child.set(swap.getPosB(), posA);
        }
        return child;
    }

    /**
     * Generates n unique random numbers from 0 to max exclusive.
     * 
     * @param n    the desired number of random numbers
     * @param max
     * @param sort if true, then the result is sorted in ascending order
     */
    public static LinkedList<Integer> getUniqueRandomNumbers(int n, int max, boolean sort) {
        if (n > max) {
            throw new IllegalArgumentException(
                    "Not enough numbers available in range (available " + max + ", desired " + n + ").");
        }
        LinkedList<Integer> results = new LinkedList<>();
        results.add(random.nextInt(max));
        for (int i = 0; i < n - 1; i++) {
            int tmp;
            do {
                tmp = random.nextInt(max);
            } while (results.contains(tmp));
            results.add(tmp);
        }
        if (sort) {
            Collections.sort(results);
        }
        return results;
    }
}