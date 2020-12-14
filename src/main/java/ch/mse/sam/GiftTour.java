package ch.mse.sam;

import ch.mse.santachallenge.Gift;
import ch.mse.santachallenge.Location;

/**
 * Extends the Gift class with the tourId.
 * 
 * @author sam
 *
 */
public class GiftTour extends Gift {
    private int tourId;

    public GiftTour(int id, Location location, double weight, int tourId) {
        super(id, location, weight);
        this.tourId = tourId;
    }

    public GiftTour(Gift gift, int tourId) {
        super(gift.getId(), gift.getLocation(), gift.getWeight());
        this.tourId = tourId;
    }

    public GiftTour(GiftTour gt) {
        super(gt.getId(), gt.getLocation(), gt.getWeight());
        this.tourId = gt.getTourId();
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    @Override
    public String toString() {
        return "GiftTour [giftId=" + getId() + ", tour=" + tourId + ", weight=" + getWeight() + "]";
    }
}