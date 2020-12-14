package ch.mse.sam;

/**
 * A simpla class, which contains two unique positions, which should be swapped.
 * 
 * @author Sam
 *
 */
public class Swap {
    public int posA;
    public int posB;

    /**
     * @param posA the first position
     * @param posB the second position
     * @throws IllegalArgumentException when the two positions are equal
     */
    public Swap(int posA, int posB) {
        super();
        if (posA == posB) {
            throw new IllegalArgumentException();
        }
        this.posA = posA;
        this.posB = posB;
    }

    public int getPosA() {
        return posA;
    }

    public int getPosB() {
        return posB;
    }

    @Override
    public String toString() {
        return "Swap [posA=" + posA + ", posB=" + posB + "]";
    }
}
