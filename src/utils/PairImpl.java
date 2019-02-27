package utils;

/**
 * a class implementing the interface Pair<X, Y>.
 * @param <X> the type of the first element of the pair
 * @param <Y> the type of the second element of the pair
 */
public class PairImpl<X, Y> implements Pair<X, Y> {

    private final X firstElem;
    private final Y secondElem;

    /**
     * builds a new PairImpl.
     * @param firstElem the first element of the pair
     * @param secondElem the second element of the pair
     */
    public PairImpl(final X firstElem, final Y secondElem) {
        this.firstElem = firstElem;
        this.secondElem = secondElem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X getX() {
        return this.firstElem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Y getY() {
        return this.secondElem;
    }

    // TODO: implements equals and hashCode methods

}
