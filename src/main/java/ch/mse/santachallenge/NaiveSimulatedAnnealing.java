package ch.mse.santachallenge;

import ch.mse.santachallenge.abstraction.ITrip;

import java.util.List;

public class NaiveSimulatedAnnealing {
    // Configuration, that are modified on initialization
    private double maxTemperature = 0.4;
    private double minTemperature = 0.0001;
    private double decayFactor = 1.1;
    private int decayDelay = 1000;
    
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
            if (destTrip.totalWeight() + gift.getWeight() > Constants.maxWeight) {
                // The destination trip is already too heavy, generate a new trip
                destTrip = new Trip();
                newDestTrip = true;
            }
            // + 1 because we could place the gift at the start and at the end, which gives us extra location
            int pos = (int) (Math.random() * (destTrip.size() + 1));

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

    public void reset() {
        temperature = maxTemperature;
        decayTimer = decayDelay;
    }
}
