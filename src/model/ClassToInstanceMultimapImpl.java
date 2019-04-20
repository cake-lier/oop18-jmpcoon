package model;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.primitives.Primitives;

/**
 * The class implementation of {@link ClassToInstanceMultimap}. It also extends a {@link ForwardingMultimap} because it wraps
 * another multimap and delegate to it the job of storing values, leaving to itself the job of checking the correctness of the
 * entries inserted. It doesn't support {@code null} as a key or value, because the point of a multimap like this one is to
 * store instances along with their type. An object in Java can't have no type. Every object belongs at least at the supertype
 * {@link Object} and because of that a type of an object can't be unknown. As for the values of the entries, they also can't be
 * null, because the idea behind this data structure is that it should contain instances of some class and {@code null} is not an
 * instance, is an absence of instance, because it references to nothing. Hence, a {@code null} as a value can't be inserted.
 * @param <B> an upper bound supertype shared by all the instances in the multimap
 */
public final class ClassToInstanceMultimapImpl<B> extends ForwardingMultimap<Class<? extends B>, B> 
                                                  implements ClassToInstanceMultimap<B> {
    private static final long serialVersionUID = -9047286057610567233L;

    private final Multimap<Class<? extends B>, B> backingMap;

    /**
     * General constructor which accepts a backing map to wrap and use as a support. For consistency reasons, it rejects from
     * being a backing map all multimaps which entries don't respect the rule for which the key is the class of the value
     * instance before using the map.
     * @param backingMap the backing map to wrap
     * @throws ClassCastException if the multimap passed doesn't respect the rule underlined before
     */
    public ClassToInstanceMultimapImpl(final Multimap<Class<? extends B>, B> backingMap) throws ClassCastException {
        super();
        this.checkMultimapEntries(Objects.requireNonNull(backingMap));
        this.backingMap = backingMap;
    }

    /**
     * Default constructor which chooses itself the map to rely on, which is a {@link com.google.common.collect.SetMultimap} with
     * keys stored in a hash set, for the greatest performances in terms of speed and memory usage.
     */
    public ClassToInstanceMultimapImpl() {
        this(MultimapBuilder.hashKeys().hashSetValues().build());
    }

    /*
     * A method for checking all if the entries of a multimap follow the rule for which the key is the class of the value
     * instance before using the map.
     */
    private void checkMultimapEntries(final Multimap<Class<? extends B>, B> multimap) {
        multimap.entries()
                .forEach(entry -> this.cast(entry.getKey(), entry.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Multimap<Class<? extends B>, B> delegate() {
        return this.backingMap;
    }

    /*
     * The method used instead of make a cast. It's copied from the MutableClassToInstanceMap implementation in the Guava
     * library, although this it isn't static. It returns "value" casted to "type".
     */
    private <T extends B> T cast(final Class<T> type, final B value) {
        return Primitives.wrap(type).cast(value);
    }

    /**
     * {@inheritDoc}
     * This particular type of multimap doesn't accept a {@code null} as a key or value. For a greater safety and a better
     * understandability, use {@link #putInstance(Class, Object)} instead. For "overwriting" the contract placed by
     * {@link ForwardingMultimap}, input {@code null} values can't throw a {@link NullPointerException}, because they were
     * accepted, they have to be specifically rejected by a {@link IllegalArgumentException}.
     * @throws ClassCastException if the value passed isn't of the type specified by key
     * @throws IllegalArgumentException if the key passed is {@code null}
     */
    @Override
    public boolean put(final Class<? extends B> key, final B value) throws ClassCastException, IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("A null key is not accepted!");
        }
        return super.put(key, this.cast(key, Objects.requireNonNull(value)));
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException if the multimap passed doesn't respect the rule which states that every value should be of the
     * type of the key associated with it
     */
    @Override
    public boolean putAll(final Multimap<? extends Class<? extends B>, ? extends B> multimap) throws ClassCastException {
        final Multimap<Class<? extends B>, B> copy = MultimapBuilder.hashKeys()
                                                                    .hashSetValues()
                                                                    .build(Objects.requireNonNull(multimap));
        this.checkMultimapEntries(copy);
        return super.putAll(copy);
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException if the values inside the iterable aren't all of the same type specified by key
     */
    @Override
    public boolean putAll(final Class<? extends B> key, final Iterable<? extends B> values) throws ClassCastException {
        this.checkIterableValues(key, values);
        return super.putAll(key, values);
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException if the values inside the iterable aren't all of the same type specified by key
     */
    @Override
    public Collection<B> replaceValues(final Class<? extends B> key, final Iterable<? extends B> values)
                         throws ClassCastException {
        this.checkIterableValues(key, values);
        return super.replaceValues(key, values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends B> Collection<T> getInstances(final Class<T> type) {
        final Class<T> copyType = Objects.requireNonNull(type);
        return this.get(copyType).stream()
                                 .map(value -> this.cast(copyType, value))
                                 .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends B> boolean putInstance(final Class<T> type, final T value) {
        return this.put(Objects.requireNonNull(type), Objects.requireNonNull(value));
    }

    /*
     * Given an iterable and a type, checks if all the values inside the iterable are of the same type specified.
     */
    private void checkIterableValues(final Class<? extends B> type, final Iterable<? extends B> values) {
        StreamSupport.stream(Objects.requireNonNull(values).spliterator(), true)
                     .forEach(value -> this.cast(Objects.requireNonNull(type), value));
    }
}
