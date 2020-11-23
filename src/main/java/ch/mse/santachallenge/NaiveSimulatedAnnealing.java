package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;

import java.util.List;

public class NaiveSimulatedAnnealing {
    // Configuration, that are modified on initialization
    private double maxTemperature = 0.4;
    private double minTemperature = 0.0001;
    private double decayFactor = 1.1;
    private int decayDelay = 1000;
    private double newTripGenerationChance = 0.01;
    
    // Variables that change over time
    private double temperature;
    private double decayTimer;

    public NaiveSimulatedAnnealing() {
        reset();
    }

    public List<ITrip> optimize(List<ITrip> trips) {
        reset();

        while (true) {
            // Select two random trips, select a random gift from one and place it in a random location in the other
            ITrip srcTrip = trips.get((int) (Math.random() * trips.size()));
            Gift gift = srcTrip.get((int) (Math.random() * srcTrip.size()));
            ITrip destTrip = trips.get((int) (Math.random() * trips.size()));
            boolean newDestTrip = false;
            if (
                    destTrip.totalWeight() + gift.getWeight() > Constants.maxWeight ||
                    Math.random() < newTripGenerationChance
            ) {
                // The destination trip is already too heavy, generate a new trip
                destTrip = new Trip();
                newDestTrip = true;
            }
            // TODO: Switch between these at some point?
//            int pos = selectRandomInsertionPosition(destTrip.size());
            int pos = selectDescendingWeightInsertionPosition(destTrip, gift);

            // Check if this improves total cost
            double originalCost = srcTrip.cost() + destTrip.cost();
            double newCost = srcTrip.costSkipGift(gift) + destTrip.costExtraGift(gift, pos);

            // If yes, or if temperature allows us to jump, move
            if (newCost <= originalCost || Math.random() <= temperature) {
                srcTrip.remove(gift);
                // This shouldn't be necessary but it covers inconsistent behavior when destTrip is empty and also otherwise
                if (pos > destTrip.size()) {
                    destTrip.add(gift);
                } else {
                    destTrip.add(pos, gift);
                }
                if (srcTrip.isEmpty()) {
                    trips.remove(srcTrip);
                }

                if (newDestTrip) {
                    trips.add(destTrip);
                }
            }
            // Otherwise skip and hope that next selection will work better
            // Then update our temperature
            --decayTimer;
            if (decayTimer <= 0) {
                decayTimer = decayDelay;
                temperature /= decayFactor;
                if (temperature < minTemperature) {
                    return trips;
                }
            }
        }
    }

    private int selectRandomInsertionPosition(int maxSize) {
        // + 1 because we could place the gift at the start and at the end, which gives us extra location
        return (int) (Math.random() * (maxSize + 1));
    }

    private int selectDescendingWeightInsertionPosition(ITrip trip, Gift newGift) {
        // Sort destinations of a trip by descending weight
        trip.sort(
                // Reversed order because we want descending sort
                (Gift g1, Gift g2) -> Double.compare(g2.getWeight(), g1.getWeight())
        );
        for (int i = 0; i < trip.size(); ++i) {
            Gift gift = trip.get(i);
            if (gift.getWeight() < newGift.getWeight()) {
                // We don't have any other gift with this weight, which means the insertion position has to be here
                return i;
            } else if (gift.getWeight() == newGift.getWeight()) {
                // We're at the start of the range of potential positions
                // where to insert our gift keeping with descending weights
                // Select the best one
                int bestPos = 0;
                double bestCost = Double.POSITIVE_INFINITY;
                for (;; ++i) {
                    // TODO This is very inefficient, but premature optimization root of evil etc
                    double cost = trip.costExtraGift(gift, i);
                    if (cost < bestCost) {
                        bestPos = i;
                        bestCost = cost;
                    }
                    if (i >= trip.size() || trip.get(i).getWeight() != gift.getWeight()) {
                        break;
                    }
                }
                return bestPos;
            }
        }
        // We can insert our gift at the end
        return trip.size();
    }

    public void setTemperatureLimits(double minTemperature, double maxTemperature) throws IllegalArgumentException {
        if (maxTemperature < minTemperature) {
            throw new IllegalArgumentException("Minimum temperature is larger than maximum temperature");
        }
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public void setDecay(double decayFactor, int decayDelay) throws IllegalArgumentException {
        if (decayFactor < 1.0 || decayDelay < 0) {
            throw new IllegalArgumentException();
        }

        this.decayFactor = decayFactor;
        this.decayDelay = decayDelay;
    }

    public void setNewTripGenerationChance(double newTripGenerationChance) {
        this.newTripGenerationChance = newTripGenerationChance;
    }

    public void reset() {
        temperature = maxTemperature;
        decayTimer = decayDelay;
    }
}
