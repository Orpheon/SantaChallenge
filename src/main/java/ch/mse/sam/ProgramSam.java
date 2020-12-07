/**
 * 
 */
package ch.mse.sam;

import java.io.IOException;
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
        readGifts = readGifts.subList(0, 1000);
        LinkedList<Gift> gifts = new LinkedList<>(readGifts);
        SolutionSam.setGifts(gifts);
        Solvable solver = new SolverGenetic(new SolverRandom(), 100);
//        Solvable solver = new SolverRandom();
        SolutionSam solution = solver.solve(gifts);
        Printer printer = new Printer(2, 8);
        printer.writeToHtml2("test.html", gifts, solution.getTours());
        System.out.println("Solution is valid " + solution.isValid(true));
    }
}
