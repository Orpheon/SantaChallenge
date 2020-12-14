package ch.mse.santachallenge;

import java.util.ArrayList;

public class Partition extends ArrayList<Gift> {
    private double edgeMaxLatitude = - Double.MAX_VALUE;
    private double edgeMinLatitude = Double.MAX_VALUE;

    public boolean tryAdd(Gift gift, double separationWidth){
        if(gift == null){
            return false;
        }
        var lastGift = size() > 0 ? get(size() - 1) : null;
        if(lastGift == null || lastGift.getLocation().getLatitude() < gift.getLocation().getLatitude() + separationWidth){
            this.add(gift);
            this.edgeMinLatitude = Math.min(this.edgeMinLatitude, gift.getLocation().getLatitude());
            this.edgeMaxLatitude = Math.max(this.edgeMaxLatitude, gift.getLocation().getLatitude());
            return true;
        }else{
            return false;
        }

    }

    public double getEdgeOverlapWith(Partition other){
        var sharedEdgeMax = Math.min(this.edgeMaxLatitude, other.edgeMaxLatitude);
        var sharedEdgeMin = Math.max(this.edgeMinLatitude, other.edgeMinLatitude);
        return sharedEdgeMax - sharedEdgeMin;
    }

    public void add(Partition partition){
        this.edgeMaxLatitude = partition.edgeMaxLatitude;
        this.edgeMinLatitude = partition.edgeMinLatitude;
        this.addAll(partition);
    }

    public double getAvgLatitude(){
        var accu = 0.0;
        for(var gift : this){
            accu += gift.getLocation().getLatitude();
        }
        return accu / size();
    }
}
