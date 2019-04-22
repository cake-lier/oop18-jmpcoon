package it.unibo.jmpcoon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import it.unibo.jmpcoon.model.ClassToInstanceMultimap;
import it.unibo.jmpcoon.model.ClassToInstanceMultimapImpl;

/**
 * Test for checking he correctness of a {@link ClassToInstanceMultimap}.
 */
public class ClassToInstanceMultimapTest {
    private static final String NOT_EMPTY = "The multimap should be empty";
    private static final String EMPTY = "The multimap should not be empty";
    private static final String NOT_SAME_NUMBER_ELEMENTS = "There isn't the same number of elements as presumed";
    private static final String NOT_SAME_ELEMENTS = "Some or all the elements previously inserted are not the same as before";
    private static final String PUT_OR_VALUES_FAILED = "The 'put' method didn't insert elements correctly or the 'values' "
                                                       + "method didn't return the correct values for this multimap";
    private static final String NOT_SAME_NUMBER_VALUES = "There aren't the same number of values as presumed";
    private static final String NOT_SAME_VALUES = "The values aren't the same as before";
    private static final String GET_FAILED = "The 'get' method didn't obtain the two instances of Double present";
    private static final String MORE_INSTANCES = "No instances of this type should be inside the multimap";
    private static final String NO_INSTANCES = "The multimap should contain an instance of this type";
    private static final String NOT_SAME_NUMBER_DISTINCT_KEYS = "The distinct keys aren't in the same number as presumed";
    private static final String NOT_SAME_DISTINCT_KEYS = "The distinct keys aren't the same as before";
    private static final String NOT_SAME_NUMBER_KEYS = "The total keys aren't in the same number as presumed";
    private static final String NOT_SAME_KEYS = "The total keys aren't the same as before";
    private static final String NOT_SAME_NUMBER_ENTRIES = "There isn't the same number of entries as presumed";
    private static final String NO_SAME_ENTRIES = "The multimap should contain every entry which already contains";
    private static final String NOT_SAME_KEYS_IN_ENTRIES = "The keys in the entries aren't the same as before";
    private static final String NOT_SAME_VALUES_IN_ENTRIES = "The values in the entries aren't the same as before";
    private static final String NO_NEW_ELEMENTS = "The multimap shouldn't already contain a newly generated instance";
    private static final String NO_REPLACED_DOUBLE = "There isn't the newly replaced Double instance inside the multimap";

    private final Integer integer;
    private final Double firstDouble;
    private final Double secondDouble;
    private ClassToInstanceMultimap<Number> testMultimap;

    /**
     * Initializes the three dummy elements which will be inserted into the multimap: an {@link Integer} one and two
     * {@link Double} ones.
     */
    public ClassToInstanceMultimapTest() {
        this.integer = Integer.valueOf(0);
        this.firstDouble = Double.valueOf(0);
        this.secondDouble = Double.valueOf(1);
    }

    /**
     * Default initialization of a {@link ClassToInstanceMultimap}, it's created and then the three elements are added.
     */
    @Before
    public void initializeMultimap() {
        this.testMultimap = new ClassToInstanceMultimapImpl<>();
        this.testMultimap.putInstance(Integer.class, this.integer);
        this.testMultimap.putInstance(Double.class, this.firstDouble);
        this.testMultimap.putInstance(Double.class, this.secondDouble);
    }

    /**
     * Tests to check whether a newly created {@link ClassToInstanceMultimap} is initially empty or not.
     */
    @Test
    public void emptyMultimapTest() {
        final ClassToInstanceMultimap<Number> newMultimap = new ClassToInstanceMultimapImpl<>();
        assertTrue(NOT_EMPTY, newMultimap.isEmpty());
        final ClassToInstanceMultimap<Number> suppliedMultimap 
            = new ClassToInstanceMultimapImpl<Number>(MultimapBuilder.linkedHashKeys().arrayListValues().build());
        assertTrue(NOT_EMPTY, suppliedMultimap.isEmpty());
    }

    /**
     * Tests if the {@link ClassToInstanceMultimap} adds elements to itself correctly and return them as they originally were 
     * using {@link ClassToInstanceMultimap#putInstance(Class, Object)} and {@link ClassToInstanceMultimap#getInstances(Class)}
     * methods.
     */
    @Test
    public void instancesInsertionTest() {
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 3, this.testMultimap.size());
        assertFalse(EMPTY, this.testMultimap.isEmpty());
        final Collection<Integer> integers = this.testMultimap.getInstances(Integer.class);
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 1, integers.size());
        assertTrue(NOT_SAME_ELEMENTS, Arrays.asList(this.integer).containsAll(integers));
        assertTrue(MORE_INSTANCES, this.testMultimap.getInstances(Byte.class).isEmpty());
    }

    /**
     * Tests if the {@link ClassToInstanceMultimap} adds elements to itself correctly and return them as they originally were
     * using methods inherited from {@link Multimap}.
     */
    @Test
    public void multimapInsertionTest() {
        final ClassToInstanceMultimap<Number> multimap = new ClassToInstanceMultimapImpl<>();
        multimap.put(Integer.class, this.integer);
        multimap.put(Double.class, this.firstDouble);
        multimap.put(Double.class, this.secondDouble);
        final Collection<Number> values = multimap.values();
        assertTrue(PUT_OR_VALUES_FAILED, Arrays.asList(this.integer, this.firstDouble, this.secondDouble).containsAll(values));
        assertEquals(NOT_SAME_NUMBER_VALUES, 3, values.size());
        final Collection<Number> doubles = multimap.get(Double.class);
        assertTrue(GET_FAILED, Arrays.asList(this.firstDouble, this.secondDouble).containsAll(doubles));
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 2, doubles.size());
        multimap.putAll(Float.class, Arrays.asList(Float.valueOf(0), Float.valueOf(1)));
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 2, multimap.get(Float.class).size());
        final ClassToInstanceMultimap<Integer> extMultimap = new ClassToInstanceMultimapImpl<>();
        extMultimap.put(Integer.class, Integer.valueOf(1));
        multimap.putAll(extMultimap);
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 2, multimap.get(Integer.class).size());
    }

    /**
     * Tests if the incorrect use of the {@link ClassToInstanceMultimap#put(Object, Object)} raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeInsertionTest() {
        this.testMultimap.put(Double.class, Integer.valueOf(0));
    }

    /**
     * Tests if the insertion with the {@link ClassToInstanceMultimap#put(Object, Object)} with a {@code null} value raises the
     * {@link IllegalArgumentException} exception as it should do.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullInsertionTest() {
        this.testMultimap.put(null, Double.valueOf(0));
    }

    /**
     * Tests if the incorrect use of the {@link ClassToInstanceMultimap#putAll(Object, Iterable)} raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeMultipleInsertionTest() {
        this.testMultimap.putAll(Integer.class, Arrays.asList(Float.valueOf(0), Float.valueOf(0)));
    }

    /**
     * Tests if the incorrect use of the {@link ClassToInstanceMultimap#putAll(Multimap)} raises the {@link ClassCastException}
     * exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeInsertionFromOtherMapTest() {
        final Multimap<Class<? extends Number>, Number> extMultimap = MultimapBuilder.linkedHashKeys()
                                                                                     .linkedHashSetValues()
                                                                                     .build();
        extMultimap.put(Integer.class, Double.valueOf(0));
        this.testMultimap.putAll(extMultimap);
    }

    /**
     * Tests if the removal methods of the {@link ClassToInstanceMultimap} are all working as intended.
     */
    @Test
    public void removalTest() {
        this.testMultimap.remove(Integer.class, this.integer);
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 2, this.testMultimap.size());
        assertTrue(NOT_SAME_ELEMENTS, this.testMultimap.values().containsAll(Arrays.asList(this.firstDouble, this.secondDouble)));
        this.testMultimap.putInstance(Integer.class, Integer.valueOf(-1));
        this.testMultimap.removeAll(Double.class);
        assertTrue(MORE_INSTANCES, this.testMultimap.getInstances(Double.class).isEmpty());
        this.testMultimap.clear();
        assertTrue(NOT_EMPTY, this.testMultimap.isEmpty());
    }

    /**
     * Tests the classic functionalities that a map can provide such as getting the keys, the values, the entries and the ones
     * specifically relative to multimaps such as getting the keys in total.
     */
    @Test
    public void mapAndMultimapGettersTest() {
        assertEquals(NOT_SAME_NUMBER_DISTINCT_KEYS,  2, this.testMultimap.keySet().size());
        assertTrue(NOT_SAME_DISTINCT_KEYS, this.testMultimap.keySet().containsAll(Arrays.asList(Integer.class, Double.class)));
        assertEquals(NOT_SAME_NUMBER_KEYS, 3, this.testMultimap.keys().size());
        assertTrue(NOT_SAME_KEYS, this.testMultimap.keys()
                                                   .containsAll(Arrays.asList(Integer.class, Double.class, Double.class)));
        assertEquals(NOT_SAME_NUMBER_ENTRIES, 3, this.testMultimap.entries().size());
        assertTrue(NOT_SAME_KEYS_IN_ENTRIES, this.testMultimap.entries().stream()
                                                                        .map(entry -> entry.getKey())
                                                                        .collect(Collectors.toList())
                                                                        .containsAll(this.testMultimap.keys()));
        assertTrue(NOT_SAME_VALUES_IN_ENTRIES, this.testMultimap.entries().stream()
                                                                          .map(entry -> entry.getValue())
                                                                          .collect(Collectors.toList())
                                                                          .containsAll(this.testMultimap.values()));
        this.testMultimap.entries().forEach(entry -> entry.getKey().cast(entry.getValue()));
    }

    /**
     * Tests conversion from a multimap to a map of {@link Collection}s.
     */
    @Test
    public void multimapToMapConversionTest() {
        final Map<Class<? extends Number>, Collection<Number>> map = this.testMultimap.asMap();
        assertEquals(NOT_SAME_NUMBER_DISTINCT_KEYS, this.testMultimap.keySet().size(), map.keySet().size());
        assertTrue(NOT_SAME_DISTINCT_KEYS, map.keySet().containsAll(this.testMultimap.keySet()));
        assertEquals(NOT_SAME_NUMBER_VALUES, this.testMultimap.size(), map.values().stream()
                                                                                   .flatMap(c -> c.stream())
                                                                                   .collect(Collectors.toList())
                                                                                   .size());
        assertTrue(NOT_SAME_VALUES, map.values().stream()
                                                .flatMap(c -> c.stream())
                                                .collect(Collectors.toList())
                                                .containsAll(this.testMultimap.values()));
    }

    /**
     * Tests if the {@link Map} inherited "contains" family methods should work as expected. They are used to test whether a key,
     * a value or an entry is inside this {@link ClassToInstanceMultimap}.
     */
    @Test
    public void containsMethodsTest() {
        assertTrue(NO_INSTANCES, this.testMultimap.containsKey(Integer.class));
        assertFalse(MORE_INSTANCES, this.testMultimap.containsKey(Float.class));
        assertTrue(NO_INSTANCES, this.testMultimap.containsValue(this.firstDouble));
        assertFalse(NO_NEW_ELEMENTS, this.testMultimap.containsValue(Integer.valueOf(-1)));
        this.testMultimap.entries().forEach(entry -> {
            assertTrue(NO_SAME_ENTRIES, this.testMultimap.containsEntry(entry.getKey(), entry.getValue()));
        });
        assertFalse(NO_NEW_ELEMENTS, this.testMultimap.containsEntry(Integer.class, Integer.valueOf(-1)));
    }

    /**
     * Tests the correct behavior of the {@link ClassToInstanceMultimap#replaceValues(Object, Iterable)} method.
     */
    @Test(expected = ClassCastException.class)
    public void replaceMethodTest() {
        final Double newDouble = Double.valueOf(-1);
        this.testMultimap.replaceValues(Double.class, Arrays.asList(newDouble));
        assertEquals(NOT_SAME_NUMBER_ELEMENTS, 2, this.testMultimap.size());
        assertTrue(NO_REPLACED_DOUBLE, this.testMultimap.containsValue(newDouble));
        this.testMultimap.replaceValues(Double.class, Arrays.asList(Integer.valueOf(0)));
    }
}
