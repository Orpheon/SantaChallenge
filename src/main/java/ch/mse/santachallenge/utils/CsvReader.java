package ch.mse.santachallenge.utils;

import ch.mse.santachallenge.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public List<Gift> readGifts(String path) throws IOException {
        File file = new File(path).getAbsoluteFile();
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String row;
        ArrayList<Gift> gifts = new ArrayList<>();
        csvReader.readLine(); //Skip header line
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            gifts.add(new Gift(Integer.parseInt(data[0]),new Location(Double.parseDouble(data[2]), Double.parseDouble(data[1])), Double.parseDouble(data[3])));
        }
        csvReader.close();
        return gifts;
    }
}
