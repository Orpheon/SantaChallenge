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
 * @author Sam
 *
 */
public class ProgramSam {

    public static final int CARGO_LIMIT = 1000;

    public static final int SLEIGHT_BASE_WEIGHT = 10;

    public static final Location NORTH_POLE = new Location(0, 90);

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
        Solvable solverRandom = new SolverRandom();
        LinkedList<LinkedList<Gift>> solution = solverRandom.solve(gifts);
        Printer printer = new Printer(2, 8);
        LinkedList<Iterable<Gift>> tmp = new LinkedList<>();
        for (LinkedList<Gift> linkedList : solution) {
            tmp.add(linkedList);
        }
        printer.writeToHtml2("test.html", gifts, tmp);
        boolean tourValid = Utils.isTourValid(gifts, solution);
        System.out.println(tourValid);
    }
}
