package ch.mse.santachallenge;

public class Gift {
    private final int id;
    private final Location location;
    private final double weight;

    public Gift(int id, Location location, double weight) {
        this.id = id;
        this.location = location;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Gift [id=" + id + ", location=" + location + ", weight=" + weight + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Gift other = (Gift) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
