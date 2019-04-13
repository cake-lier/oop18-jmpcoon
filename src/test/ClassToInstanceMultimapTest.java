package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import model.ClassToInstanceMultimapImpl;
import model.ClassToInstanceMultimap;

/**
 * Test for checking he correctness of a {@link ClassToInstanceMultimap}.
 */
public class ClassToInstanceMultimapTest {
    private final Integer integer = Integer.valueOf(0);
    private final Double firstDouble = new Double(0);
    private final Double secondDouble = new Double(1);
    private final ClassToInstanceMultimap<Number> testMultimap = new ClassToInstanceMultimapImpl<>();

    /**
     * Tests to check whether a newly created {@link ClassToInstanceMultimap} is initially empty or not.
     */
    @Test
    public void emptyMultimapTest() {
        final ClassToInstanceMultimap<Number> newMultimap = new ClassToInstanceMultimapImpl<>();
        assertTrue("The ClassToInstanceMultimap is initially not empty", newMultimap.isEmpty());
        final ClassToInstanceMultimap<Number> suppliedMultimap 
                                                   = new ClassToInstanceMultimapImpl<Number>(MultimapBuilder.linkedHashKeys()
                                                                                                            .arrayListValues()
                                                                                                            .build());
        assertTrue("The ClassToInstanceMultimap is initially not empty", suppliedMultimap.isEmpty());
    }

    /**
     * Default initialization of a {@link ClassToInstanceMultimap}, it's emptied and then three dummy objects are added, 
     * an {@link Integer} one and two {@link Double} ones.
     */
    private void initializeMultimap() {
        this.testMultimap.clear();
        this.testMultimap.putInstance(Integer.class, integer);
        this.testMultimap.putInstance(Double.class, firstDouble);
        this.testMultimap.putInstance(Double.class, secondDouble);
    }

    /**
     * Tests if the {@link ClassToInstanceMultimap} adds elements to itself correctly and return them as they originally were 
     * using {@link ClassToInstanceMultimap#putInstance(Class, Object)} and {@link ClassToInstanceMultimap#getInstances(Class)}
     * methods.
     */
    @Test
    public void instancesInsertionTest() {
        this.initializeMultimap();
        assertEquals("There aren't three elements currently in the test multimap", 3, this.testMultimap.size());
        assertFalse("The multimap should not be empty", this.testMultimap.isEmpty());
        final Collection<Integer> integers = this.testMultimap.getInstances(Integer.class);
        assertEquals("There isn't only one instance of the Integer class", 1, integers.size());
        assertTrue("The Integer instance in the multimap is not the one previously inserted",
                   Arrays.asList(this.integer).containsAll(integers));
        assertTrue("There shouldn't be any Byte instance in the multimap",
                   this.testMultimap.getInstances(Byte.class).isEmpty());
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
        assertTrue("The 'put' method didn't insert elements correctly or the 'values' method didn't return the correct values for"
                   + " this multimap", Arrays.asList(this.integer, this.firstDouble, this.secondDouble).containsAll(values));
        assertEquals("There should be three values in this multimap", 3, values.size());
        final Collection<Number> doubles = multimap.get(Double.class);
        assertTrue("The 'get' method didn't obtain the two instances of Double present",
                   Arrays.asList(this.firstDouble, this.secondDouble).containsAll(doubles));
        assertEquals("There should be two instances of Double into the multimap", 2, doubles.size());
        multimap.putAll(Float.class, Arrays.asList(new Float(0), new Float(1)));
        assertEquals("There should be two instances of Float in the multimap", 2, multimap.get(Float.class).size());
        final ClassToInstanceMultimap<Integer> extMultimap = new ClassToInstanceMultimapImpl<>();
        extMultimap.put(Integer.class, Integer.valueOf(1));
        multimap.putAll(extMultimap);
        assertEquals("There should be two Integer instances now", 2, multimap.get(Integer.class).size());
    }

    /**
     * Tests if the incorrect use of the {@link ClassToInstanceMultimap#put(Object, Object)} raises the {@link ClassCastException}
     * exception as it should do.
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
        this.testMultimap.put(null, new Double(0));
    }

    /**
     * Tests if the incorrect use of the {@link ClassToInstanceMultimap#putAll(Object, Iterable)} raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeMultipleInsertionTest() {
        this.testMultimap.putAll(Integer.class, Arrays.asList(new Float(0), new Float(0)));
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
        extMultimap.put(Integer.class, new Double(0));
        this.testMultimap.putAll(extMultimap);
    }

    /**
     * Tests if the removal methods of the {@link ClassToInstanceMultimap} are all working as intended.
     */
    @Test
    public void removalTest() {
        this.initializeMultimap();
        this.testMultimap.remove(Integer.class, this.integer);
        assertEquals("There aren't only two elements in the multimap now", 2, this.testMultimap.size());
        assertTrue("The two elements left aren't the two Double instances inserted before", 
                   this.testMultimap.values().containsAll(Arrays.asList(this.firstDouble, this.secondDouble)));
        this.testMultimap.putInstance(Integer.class, Integer.valueOf(-1));
        this.testMultimap.removeAll(Double.class);
        assertTrue("No Double instance should be left inside the multimap",
                   this.testMultimap.getInstances(Double.class).isEmpty());
        this.testMultimap.clear();
        assertTrue("The multimap should be empty now", this.testMultimap.isEmpty());
    }

    /**
     * Tests the classic functionalities that a map can provide such as getting the keys, the values, the entries and the ones
     * specifically relative to multimaps such as getting the keys in total.
     */
    @Test
    public void mapAndMultimapGettersTest() {
        this.initializeMultimap();
        assertEquals("There aren't two distinct keys in the multimap",  2, this.testMultimap.keySet().size());
        assertTrue("The distinct keys in the multimap aren't the same as before",
                   this.testMultimap.keySet().containsAll(Arrays.asList(Integer.class, Double.class)));
        assertEquals("The total keys in this multimap aren't three", 3, this.testMultimap.keys().size());
        assertTrue("The total keys in this multimap aren't the one inserted before",
                   this.testMultimap.keys().containsAll(Arrays.asList(Integer.class, Double.class, Double.class)));
        assertEquals("There aren't three entries in the multimap", 3, this.testMultimap.entries().size());
        assertTrue("The keys in the entries aren't the same as before", this.testMultimap.entries()
                                                                                         .stream()
                                                                                         .map(entry -> entry.getKey())
                                                                                         .collect(Collectors.toList())
                                                                                         .containsAll(this.testMultimap.keys()));
        assertTrue("The values in the entries aren't the same as before", this.testMultimap.entries()
                                                                                           .stream()
                                                                                           .map(entry -> entry.getValue())
                                                                                           .collect(Collectors.toList())
                                                                                           .containsAll(this.testMultimap
                                                                                                            .values()));
        this.testMultimap.entries().forEach(entry -> {
            try {
                entry.getKey().cast(entry.getValue());
            } catch (final ClassCastException exception) {
                fail("The 'entries' method should leave unchanged the entries");
            } catch (final Exception exception) {
                fail("Any exception different from ClassCastException should not be thrown");
            }
        });
    }

    /**
     * Tests conversion from a multimap to a map of {@link Collection}s.
     */
    @Test
    public void multimapToMapConversionTest() {
        this.initializeMultimap();
        final Map<Class<? extends Number>, Collection<Number>> map = this.testMultimap.asMap();
        assertEquals("The distrinct keys in the map aren't in the same number as before", this.testMultimap.keySet().size(), 
                     map.keySet().size());
        assertTrue("The distinct keys aren't the same as before", map.keySet().containsAll(this.testMultimap.keySet()));
        assertEquals("The values aren't in the same number as before", this.testMultimap.size(), map.values()
                                                                                                    .stream()
                                                                                                    .flatMap(c -> c.stream())
                                                                                                    .collect(Collectors.toList())
                                                                                                    .size());
        assertTrue("The values aren't the same as before", map.values().stream()
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
        this.initializeMultimap();
        assertTrue("The multimap should contain the Integer class key", this.testMultimap.containsKey(Integer.class));
        assertFalse("The multimap shouldn't contain the Float class key", this.testMultimap.containsKey(Float.class));
        assertTrue("The multimap should contain the Double instance previously inserted",
                   this.testMultimap.containsValue(this.firstDouble));
        assertFalse("The multimap shouldn't contain a Player instance newly generated",
                    this.testMultimap.containsValue(Integer.valueOf(-1)));
        this.testMultimap.entries().forEach(entry -> {
            assertTrue("The multimap should contain every entry which already contains",
                       this.testMultimap.containsEntry(entry.getKey(), entry.getValue()));
        });
        assertFalse("The multimap shouldn't contain an entry made with a never inserted value",
                    this.testMultimap.containsEntry(Integer.class, Integer.valueOf(-1)));
    }

    /**
     * Tests the correct behavior of the {@link ClassToInstanceMultimap#replaceValues(Object, Iterable)} method.
     */
    @Test(expected = ClassCastException.class)
    public void replaceMethodTest() {
        this.initializeMultimap();
        final Double newDouble = new Double(-1);
        this.testMultimap.replaceValues(Double.class, Arrays.asList(newDouble));
        assertEquals("There aren't two values in the multimap as it should be", 2, this.testMultimap.size());
        assertTrue("There isn't the newly replaced Double instance inside the multimap",
                   this.testMultimap.containsValue(newDouble));
        this.testMultimap.replaceValues(Double.class, Arrays.asList(Integer.valueOf(0)));
    }
}
