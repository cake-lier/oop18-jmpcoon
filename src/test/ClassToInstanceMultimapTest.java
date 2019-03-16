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
import model.entities.Enemy;
import model.entities.Entity;
import model.entities.Platform;
import model.entities.Player;

/**
 * Test for checking he correctness of a {@link ClassToInstanceMultimap}.
 */
public class ClassToInstanceMultimapTest {
    private final Player player = new Player();
    private final Enemy firstEnemy = new Enemy();
    private final Enemy secondEnemy = new Enemy();
    private final ClassToInstanceMultimap<Entity> testMultimap
                                                  = new ClassToInstanceMultimapImpl<>();
    /**
     * Tests to check whether a newly created {@link ClassToInstanceMultimap} is initially
     * empty or not.
     */
    @Test
    public void emptyMultimapTest() {
        final ClassToInstanceMultimap<Entity> newMultimap
                                              = new ClassToInstanceMultimapImpl<>();
        assertTrue("The ClassToInstanceMultimap is initially not empty",
                   newMultimap.isEmpty());
        final ClassToInstanceMultimap<Entity> suppliedMultimap
              = new ClassToInstanceMultimapImpl<Entity>(MultimapBuilder.linkedHashKeys()
                                                                       .arrayListValues()
                                                                       .build());
        assertTrue("The ClassToInstanceMultimap is initially not empty",
                   suppliedMultimap.isEmpty());
    }
    /**
     * Default initialization of a {@link ClassToInstanceMultimap}, it's emptied and then
     * three dummy objects are added, a Player one and two Enemy ones.
     */
    private void initializeMultimap() {
        this.testMultimap.clear();
        this.testMultimap.putInstance(Player.class, player);
        this.testMultimap.putInstance(Enemy.class, firstEnemy);
        this.testMultimap.putInstance(Enemy.class, secondEnemy);
    }
    /**
     * Tests if the {@link ClassToInstanceMultiMap} adds elements to itself correctly
     * and return them as they originally were using
     * {@link ClassToInstanceMultimap#putInstance(Class, Object)} and
     * {@link ClassToInstanceMultimap#getInstances(Class)} methods.
     */
    @Test
    public void instancesInsertionTest() {
        this.initializeMultimap();
        assertEquals("There aren't three elements currently in the test multimap",
                     3, this.testMultimap.size());
        assertFalse("The multimap should not be empty",
                    this.testMultimap.isEmpty());
        final Collection<Player> players = this.testMultimap.getInstances(Player.class);
        assertEquals("There isn't only one instance of the Player class",
                     1, players.size());
        assertTrue("The Player instance in the multimap is not the one previously "
                   + "inserted", Arrays.asList(player).containsAll(players));
        assertTrue("There shouldn't be any Platform instance in the multimap",
                   this.testMultimap.getInstances(Platform.class).isEmpty());
    }
    /**
     * Tests if the {@link ClassToInstanceMultiMap} adds elements to itself correctly
     * and return them as they originally were using methods inherited from
     * {@link Multimap}.
     */
    @Test
    public void multimapInsertionTest() {
        final ClassToInstanceMultimap<Entity> multimap
                                              = new ClassToInstanceMultimapImpl<>();
        multimap.put(Player.class, this.player);
        multimap.put(Enemy.class, this.firstEnemy);
        multimap.put(Enemy.class, this.secondEnemy);
        final Collection<Entity> values = multimap.values();
        assertTrue("The 'put' method didn't insert elements correctly or the 'values'"
                   + "method didn't return the correct values for this multimap",
                   Arrays.asList(this.player, this.firstEnemy, this.secondEnemy)
                         .containsAll(values));
        assertEquals("There should be three values in this multimap",
                     3, values.size());
        final Collection<Entity> enemies = multimap.get(Enemy.class);
        assertTrue("The 'get' method didn't obtain the two instances of Enemy present",
                   Arrays.asList(this.firstEnemy, this.secondEnemy).containsAll(enemies));
        assertEquals("There should be two instances of Enemy into the multimap",
                     2, enemies.size());
        multimap.putAll(Platform.class, Arrays.asList(new Platform(), new Platform()));
        assertEquals("There should be two istances of Platform in the multimap",
                     2, multimap.get(Platform.class).size());
        final ClassToInstanceMultimap<Player> extMultimap
                                              = new ClassToInstanceMultimapImpl<>();
        extMultimap.put(Player.class, new Player());
        multimap.putAll(extMultimap);
        assertEquals("There should be two Player instances now",
                     2, multimap.get(Player.class).size());
    }
    /**
     * Tests if the incorrect use of the
     * {@link ClassToInstanceMultimap#put(Object, Object)} raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeInsertionTest() {
        this.testMultimap.put(Enemy.class, new Player());
    }
    /**
     * Tests if the incorrect use of the
     * {@link ClassToInstanceMultimap#putAll(Object, Iterable)} raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeMultipleInsertionTest() {
        this.testMultimap.putAll(Player.class,
                                 Arrays.asList(new Platform(), new Platform()));
    }
    /**
     * Tests if the incorrect use of the
     * {@link ClassToInstanceMultimap#putAll(Multimap) raises the
     * {@link ClassCastException} exception as it should do.
     */
    @Test(expected = ClassCastException.class)
    public void wrongTypeInsertionFromOtherMapTest() {
        final Multimap<Class<? extends Entity>, Entity> extMultimap =
              MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
        extMultimap.put(Player.class, new Enemy());
        this.testMultimap.putAll(extMultimap);
    }
    /**
     * Tests if the removal methods of the {@link ClassToInstanceMultimap} are all working
     * as intended.
     */
    @Test
    public void removalTest() {
        this.initializeMultimap();
        this.testMultimap.remove(Player.class, player);
        assertEquals("There aren't only two elements in the multimap now",
                     2, this.testMultimap.size());
        assertTrue("The two elements left aren't the two Enemy instances inserted before",
                   this.testMultimap.values().containsAll(Arrays.asList(
                                                                 this.firstEnemy,
                                                                 this.secondEnemy)));
        this.testMultimap.putInstance(Player.class, new Player());
        this.testMultimap.removeAll(Enemy.class);
        assertTrue("No Enemy instance should be left inside the multimap",
                   this.testMultimap.getInstances(Enemy.class).isEmpty());
        this.testMultimap.clear();
        assertTrue("The multimap should be empty now",
                   this.testMultimap.isEmpty());
    }
    /**
     * Tests the classic functionalities that a map can provide such as getting the
     * keys, the values, the entries and the ones specifically relative to multimaps
     * such as getting the keys in total.
     */
    @Test
    public void mapAndMultimapGettersTest() {
        this.initializeMultimap();
        assertEquals("There aren't two distinct keys in the multimap",
                     2, this.testMultimap.keySet().size());
        assertTrue("The distinct keys in the multimap aren't the same as before",
                   this.testMultimap.keySet().containsAll(Arrays.asList(
                                                          Player.class, Enemy.class)));
        assertEquals("The total keys in this multimap aren't three",
                     3, this.testMultimap.keys().size());
        assertTrue("The total keys in this multimap aren't the one inserted before",
                   this.testMultimap.keys().containsAll(Arrays.asList(Player.class,
                                                                      Enemy.class,
                                                                      Enemy.class)));
        assertEquals("There aren't three entries in the multimap",
                     3, this.testMultimap.entries().size());
        assertTrue("The keys in the entries aren't the same as before",
                   this.testMultimap.entries()
                                    .stream()
                                    .map(entry -> entry.getKey())
                                    .collect(Collectors.toList())
                                    .containsAll(this.testMultimap.keys()));
        assertTrue("The values in the entries aren't the same as before",
                   this.testMultimap.entries()
                                    .stream()
                                    .map(entry -> entry.getValue())
                                    .collect(Collectors.toList())
                                    .containsAll(this.testMultimap.values()));
        this.testMultimap.entries().forEach(entry -> {
            try {
                entry.getKey().cast(entry.getValue());
            } catch (final ClassCastException exception) {
                fail("The 'entries' method should leave unchanged the entries");
            } catch (final Exception exception) {
                fail("Any exception different from ClassCastException should not be "
                     + "thrown");
            }
        });
    }
    /**
     * and getting the conversion from multimap to
     * map of {@link Collection}s.
     */
    @Test
    public void multimapToMapConversionTest() {
        this.initializeMultimap();
        final Map<Class<? extends Entity>, Collection<Entity>> map
        = this.testMultimap.asMap();
        assertEquals("The distrinct keys in the map aren't in the same number as before",
                     this.testMultimap.keySet().size(), map.keySet().size());
        assertTrue("The distinct keys aren't the same as before",
                   map.keySet().containsAll(this.testMultimap.keySet()));
        assertEquals("The values aren't in the same number as before",
                     this.testMultimap.size(), map.values().stream()
                                                           .flatMap(c -> c.stream())
                                                           .collect(Collectors.toList())
                                                           .size());
        assertTrue("The values aren't the same as before",
                   map.values().stream()
                               .flatMap(c -> c.stream())
                               .collect(Collectors.toList())
                               .containsAll(this.testMultimap.values()));
    }
    /**
     * Tests if the {@link Map} inherited "contains" family methods should work as
     * expected. They are used to test whether a key, a value or an entry is inside this 
     * {@link ClassToInstanceMultimap}.
     */
    @Test
    public void containsMethodsTest() {
        this.initializeMultimap();
        assertTrue("The multimap should contain the Player class key",
                   this.testMultimap.containsKey(Player.class));
        assertFalse("The multimap shouldn't contain the Platform class key",
                    this.testMultimap.containsKey(Platform.class));
        assertTrue("The multimap should contain the Enemy instance previously inserted",
                   this.testMultimap.containsValue(this.firstEnemy));
        assertFalse("The multimap shouldn't contain a Player instance newly generated",
                    this.testMultimap.containsValue(new Player()));
        this.testMultimap.entries().forEach(entry -> {
            assertTrue("The multimap should contain every entry which already contains",
                       this.testMultimap.containsEntry(entry.getKey(),
                                                       entry.getValue()));
        });
        assertFalse("The multimap shouldn't contain an entry made with a never inserted "
                    + "value",
                    this.testMultimap.containsEntry(Player.class, new Player()));
    }
    /**
     * Tests the correct behavior of the
     * {@link ClassToInstanceMultimap#replaceValues(Object, Iterable)} method.
     */
    @Test(expected = ClassCastException.class)
    public void replaceMethodTest() {
        this.initializeMultimap();
        final Enemy newEnemy = new Enemy();
        this.testMultimap.replaceValues(Enemy.class, Arrays.asList(newEnemy));
        assertEquals("There aren't two values in the multimap as it should be",
                     2, this.testMultimap.size());
        assertTrue("There isn't the newly replaced Enemy instance inside the multimap",
                   this.testMultimap.containsValue(newEnemy));
        this.testMultimap.replaceValues(Enemy.class, Arrays.asList(new Player()));
    }
}
