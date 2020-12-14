/**
 * 
 */
package ch.mse.sam;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;
import ch.mse.santachallenge.utils.CsvReader;
import ch.mse.santachallenge.utils.Printer;

/**
 * Main program, which is used to solve the problem with the evolutionary
 * algorithm.
 * 
 * @author Sam
 *
 */
public class ProgramSam {

    public static final int CARGO_LIMIT = 1000;

    public static final int SLEIGHT_BASE_WEIGHT = 10;

    public static final Location NORTH_POLE = new Location(0, 90);

    public static final double SLEIGTH_BASE_WEIGHT = 10;

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CsvReader reader = new CsvReader();
        List<Gift> readGifts = reader.readGifts("gifts.csv");
        // uncomment to allow all gifts
//        readGifts = readGifts.subList(0, 40000);
        LinkedList<Gift> gifts = new LinkedList<>(readGifts);
        SolutionSam.setGifts(new HashSet<>(gifts));
        boolean doSearch = false;
        if (doSearch) {
            double start = 80; // inclusive
            double end = 300; // exclusiv
            int steps = 20;
            double step = (end - start) / steps;
            double value = start;
            do {
                // Solvable solver = new SolverGenetic(new SolverRandom(), 40, 120, 12 /
                // (double) 12, false, 0.2, 0.05, false); //10000
//                Solvable solver = new SolverGenetic(new SolverRandom(), 100, 120, 12 / (double) 12, true, 0.2, 0.05, false); //20000
//                Solvable solver = new SolverGenetic(new SolverRandom(), 100, 120, 12 / (double) 12, true, 0.2, 0.05, !doSearch); //40000
                Solvable solver = new SolverGenetic(new SolverRandom(), 120, (int) value, 12 / (double) 12, true, 0.2,
                        0.05, !doSearch); // 100000
                SolutionSam solution = solver.solve(gifts);
                System.out.println(String.format("%.2f %.0f", value, solution.getReindeerWeariness()));
                value += step;
            } while (value <= end);
        } else {
            // Solvable solver = new SolverRandom();
            Solvable solver = new SolverGenetic(new SolverRandom(), 100, 10000, 12 / (double) 12, true, 0.2, 0.05,
                    !doSearch);
            SolutionSam solution = solver.solve(gifts);
            Printer printer = new Printer(1, 8);
            printer.writeToHtml2("test.html", gifts, solution.getTours());
            System.out.println("Solution is valid " + solution.isValid(true, true));
        }
    }
}
