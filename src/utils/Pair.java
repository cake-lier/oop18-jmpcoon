package utils;

/**
 * an interface representing a pair of values.
 * @param <X> the type of the first element of the pair
 * @param <Y> the type of the second element of the pair
 */
public interface Pair<X, Y> {

    /**
     * @return the first element of the pair
     */
    X getX();

    /**
     * @return the second element of the pair
     */
    Y getY();

}
