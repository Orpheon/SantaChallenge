package ch.mse.santachallenge;

import ch.mse.santachallenge.utils.CsvReader;
import ch.mse.santachallenge.utils.Printer;

import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();
        Printer printer = new Printer();
        try {
            List<Gift> gifts = reader.readGifts("gifts.csv");
            new Printer().writeToHtml("solution.html", gifts, null);
            System.out.println("abc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
