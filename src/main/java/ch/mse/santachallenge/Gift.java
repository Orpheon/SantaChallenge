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
}
