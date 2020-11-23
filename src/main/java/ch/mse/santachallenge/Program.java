package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;
import ch.mse.santachallenge.utils.CsvReader;
import ch.mse.santachallenge.utils.Printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        CsvReader reader = new CsvReader();
        Printer printer = new Printer();
        try {
            List<Gift> gifts = reader.readGifts("gifts.csv");
            List<ITrip> trips = constructRandomSolution(gifts);

            double totalCost = 0.0;
            for (ITrip trip : trips) {
                totalCost += trip.cost();
            }
            System.out.println("Total cost of random solution: " + totalCost);
            new Printer().writeToHtml("random solution.html", gifts, trips);
            NaiveSimulatedAnnealing optimizer = new NaiveSimulatedAnnealing();
            int cycleCount = 0;
            while (true) {
                trips = optimizer.optimize(trips);
                totalCost = 0.0;
                int longestTripLength = 0;
                for (ITrip trip : trips) {
                    totalCost += trip.cost();
                    if (trip.size() > longestTripLength) {
                        longestTripLength = trip.size();
                    }
                }
                System.out.println(
                        "Cycle " + cycleCount +
                        " cost: " + totalCost +
                        "; n tours: " + trips.size() +
                        "; mean tour length: " + (gifts.size() / trips.size()) +
                        "; max tour length: " + longestTripLength
                );
                new Printer().writeToHtml("solution"+cycleCount+".html", gifts, trips);
                ++cycleCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ITrip> constructRandomSolution(List<Gift> gifts) {
        List<ITrip> trips = new ArrayList<>(100);
        // Add first trip with id 0
        trips.add(new Trip());
        for (Gift gift : gifts) {
            // Add each gift to a random trip. If the trip is already full, create a new trip and add the gift there
            ITrip trip = trips.get((int) (Math.random() * trips.size()));
            if (trip.totalWeight() + gift.getWeight() <= Constants.maxWeight) {
                trip.add(gift);
            } else {
                Trip newTrip = new Trip();
                newTrip.add(gift);
                trips.add(newTrip);
            }
        }
        return trips;
    }
}
