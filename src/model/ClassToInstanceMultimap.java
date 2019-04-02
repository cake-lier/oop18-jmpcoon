package model;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.collect.Multimap;

/**
 * A {@link Multimap} with the same properties as a {@link ClassToInstanceMap}. It models a data structure following the item
 * "Consider Typesafe Heterogeneous Containers" described by Joshua Bloch into his "Effective Java" book, but with the possibility
 * of assigning more than one instance value to the same instance class key. The methods are then the same as the ones into the 
 * {@link ClassToInstanceMap} interface, except for the fact that you can get more than one instance for a given class, so the
 * {@link ClassToInstanceMultimap#getInstances(Class)} returns a {@link Collection} instead of a simple value.
 * @param <B> An upper bound supertype shared by all the instances in the multimap.
 */
public interface ClassToInstanceMultimap<B> extends Multimap<Class<? extends B>, B>, Serializable {

    /**
     * Gets the {@link Collection} of instances contained within this multimap with the given type T.
     * @param <T> The type of the instances to be returned.
     * @param type The {@link Class} object representing the type of the instances to be returned.
     * @return The {@link Collection} of instances with the given type.
     */
    <T extends B> Collection<T> getInstances(Class<T> type);

    /**
     * Puts an instance of an object into this multimap while associating it with its own type.
     * @param <T> The type of the instance to be inserted.
     * @param type The {@link Class} object representing the type of the instance to be inserted.
     * @param value The instance to be inserted.
     * @return {@code true} if the method increased the size of the multimap, or false if the multimap already contained the
     * key-value pair and doesn't allow duplicates.
     */
    <T extends B> boolean putInstance(Class<T> type, T value);
}
