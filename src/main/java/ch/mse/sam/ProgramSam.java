/**
 * 
 */
package ch.mse.sam;

import java.io.IOException;
import java.util.LinkedList;

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
        LinkedList<Gift> gifts = new LinkedList<>(reader.readGifts("gifts.csv").subList(0, 1000));
        SolverRandom solverRandom = new SolverRandom();
        LinkedList<LinkedList<Gift>> solution = solverRandom.solve(gifts);
        Printer printer = new Printer(2, 10);
        LinkedList<Iterable<Gift>> test = new LinkedList<>();
        for (LinkedList<Gift> linkedList : solution) {
            test.add(linkedList);
        }
        printer.writeToHtml2("test.html", gifts, test);
        System.out.println("test");
    }
}
