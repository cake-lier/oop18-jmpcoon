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
 * The class implementation of {@link ClassToInstanceMultimap}. It also extends a {@link ForwardingMultimap} because it's the 
 * multimap which has to be used in cases we want to wrap another multimap and delegate to it all or part of the methods we want
 * to define.
 * @param <B> An upper bound supertype shared by all the instances in the multimap.
 */
public final class ClassToInstanceMultimapImpl<B> extends ForwardingMultimap<Class<? extends B>, B> 
                                                  implements ClassToInstanceMultimap<B> {
    private static final long serialVersionUID = -9047286057610567233L;

    private final Multimap<Class<? extends B>, B> backingMap;

    /**
     * General constructor which accepts a backing map to wrap and use as a support. For consistency reasons, it rejects from
     * being a backing map all multimaps which entries don't respect the rule for which the key is the class of the value
     * instance before using the map.
     * @param backingMap The backing map to wrap.
     * @throws ClassCastException If the multimap passed doesn't respect the rule underlined before.
     */
    public ClassToInstanceMultimapImpl(final Multimap<Class<? extends B>, B> backingMap) throws ClassCastException {
        super();
        this.checkMultimapEntries(Objects.requireNonNull(backingMap));
        this.backingMap = backingMap;
    }

    /**
     * Default constructor which chooses itself the map to rely on, which is a
     * {@link SetMultimap} with keys stored in a hash set.
     */
    public ClassToInstanceMultimapImpl() {
        this(MultimapBuilder.hashKeys().hashSetValues().build());
    }

    /*
     * A method for checking all if the entries of a multimap follow the rule for which the key is the class of the value
     * instance before using the map.
     * "multimap": The multimap to check.
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
     * "type": The type to use while trying to cast the value.
     * "value": The value to be casted.
     */
    private <T extends B> T cast(final Class<T> type, final B value) {
        return Primitives.wrap(type).cast(value);
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException If the value passed isn't of the type specified by key.
     */
    @Override
    public boolean put(final Class<? extends B> key, final B value) throws ClassCastException {
        final Class<? extends B> copyKey = Objects.requireNonNull(key);
        return super.put(copyKey, this.cast(copyKey, Objects.requireNonNull(value)));
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException If the multimap passed doesn't respect the rule which states that every value should be of the
     * type of the key associated with it.
     */
    @Override
    public boolean putAll(final Multimap<? extends Class<? extends B>, ? extends B> multimap) throws ClassCastException {
        final Multimap<Class<? extends B>, B> copy = MultimapBuilder.hashKeys()
                                                                    .hashSetValues()
                                                                    .build(Objects.requireNonNull(multimap));
        this.checkMultimapEntries(copy);
        return super.putAll(copy);
    }

    /*
     * Given an iterable and a type, checks if all the values inside the iterable are of the same type specified.
     * "type": the type of the value inside the iterable
     * "values": the iterable to check
     */
    private void checkIterableValues(final Class<? extends B> type, final Iterable<? extends B> values) {
        StreamSupport.stream(Objects.requireNonNull(values).spliterator(), true)
                     .forEach(value -> this.cast(Objects.requireNonNull(type), value));
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException If the values inside the iterable aren't all of the same type specified by key.
     */
    @Override
    public boolean putAll(final Class<? extends B> key, final Iterable<? extends B> values) throws ClassCastException {
        this.checkIterableValues(key, values);
        return super.putAll(key, values);
    }

    /**
     * {@inheritDoc}
     * @throws ClassCastException If the values inside the iterable aren't all of the same type specified by key.
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
}
