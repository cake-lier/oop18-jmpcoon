package model.entities;

/**
 * an enumeration representing the possible types of an {@link Entity}.
 */
public enum EntityType {
    /**
     * 
     */
    LADDER(Ladder.class, "Ladder"),
    /**
     * 
     */
    PLAYER(Player.class, "Player"),
    /**
     * 
     */
    PLATFORM(Platform.class, "Platform"),
    /**
     * 
     */
    POWERUP(PowerUp.class, "PowerUp"),
    /**
     * 
     */
    ROLLING_ENEMY(RollingEnemy.class, "RollingEnemy"),
    /**
     * 
     */
    WALKING_ENEMY(WalkingEnemy.class, "WalkingEnemy");

    private final Class<? extends Entity> typeClass;
    private final String typeName;
    /**
     * Creates a new element of this enum by registering its associated class and its
     * name.
     * @param typeClass The class associated with the element.
     * @param typeName The element name.
     */
    EntityType(final Class<? extends Entity> typeClass, final String typeName) {
        this.typeClass = typeClass;
        this.typeName = typeName;
    }
    /**
     * Gets the class of this type of {@link Entity}.
     * @return The associated Entity subtype.
     */
    public Class<? extends Entity> getTypeClass() {
        return this.typeClass;
    }
    /**
     * Gets the name of this type of {@link Entity}.
     * @return The associated name.
     */
    public String getTypeName() {
        return this.typeName;
    }
}
