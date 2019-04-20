package model.physics;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import model.entities.EntityState;
import model.serializable.SerializableBody;
import model.entities.MovementType;

/**
 * A class representing a {@link PhysicalBody} that can move (players and enemies).
 */
public class DynamicPhysicalBody extends AbstractPhysicalBody {
    private static final long serialVersionUID = -3108712528358435170L;
    private static final double PRECISION = 0.1;
    private static final double MAXVELOCITY_X = 1;
    private static final double MAXVELOCITY_Y = 0.5;
    private static final double CLIMB_DAMPING = 3.7;

    private double maxVelocityX = MAXVELOCITY_X;
    private double maxVelocityY = MAXVELOCITY_Y;
    private final SerializableBody body;
    private EntityState currentState;

    /**
     * Builds a new {@link DynamicPhysicalBody}. This constructor is package protected because it should be only invoked 
     * by the {@link PhysicalFactoryImpl} when creating a new instance of it and no one else.
     * @param body the {@link SerializableBody} encapsulated by this {@link DynamicPhysicalBody}
     */
    DynamicPhysicalBody(final SerializableBody body) {
        super(body);
        this.body = body;
        this.currentState = EntityState.IDLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityState getState() {
        if (Math.abs(this.body.getLinearVelocity().x) <= PRECISION
            && Math.abs(this.body.getLinearVelocity().y) <= PRECISION
            && this.currentState != EntityState.CLIMBING_DOWN
            && this.currentState != EntityState.CLIMBING_UP) {
            return EntityState.IDLE;
        }
        return this.currentState;
    }

    /**
     * Sets entity's {@link EntityState} to idle.
     */
    public void setIdle() {
        this.currentState = EntityState.IDLE;
        this.body.setGravityScale(1);
        this.body.setLinearDamping(Body.DEFAULT_LINEAR_DAMPING);
        this.body.setLinearVelocity(new Vector2(0, 0));
    }

    /**
     * Applies the given movement to this {@link Body},
     * sets the corresponding {@link EntityState}, sets the body's gravity to 0 if climbing.
     * @param movement the kind of movement
     * @param x the horizontal component of the movement
     * @param y the vertical component of the movement
     */
    public void applyMovement(final MovementType movement, final double x, final double y) {
        if ((this.currentState != EntityState.CLIMBING_UP && this.currentState != EntityState.CLIMBING_DOWN)
            && (movement == MovementType.CLIMB_UP || movement == MovementType.CLIMB_DOWN)) {
            this.body.setGravityScale(0);
            this.body.setLinearDamping(CLIMB_DAMPING);
            this.body.setLinearVelocity(0, 0);
        }
        this.currentState = movement.convert();
        this.body.applyImpulse(new Vector2(x, y));
        if (Math.abs(this.body.getLinearVelocity().x) > this.maxVelocityX) {
            this.body.setLinearVelocity(new Vector2(Math.signum(this.body.getLinearVelocity().x) * this.maxVelocityX,
                                                                this.body.getLinearVelocity().y));
        }
        if (Math.abs(this.body.getLinearVelocity().y) > this.maxVelocityY
            && (movement == MovementType.CLIMB_DOWN || movement == MovementType.CLIMB_UP)) {
            this.body.setLinearVelocity(new Vector2(this.body.getLinearVelocity().x, 
                                                    Math.signum(this.body.getLinearVelocity().y) * this.maxVelocityY));
        }
    }

    /**
     * Sets a linear velocity to this {@link Body} and its corresponding {@link State}.
     * @param movement the type of {@link MovementType}
     * @param x the horizontal component of the movement
     * @param y the vertical component of the movement
     */
    public void setFixedVelocity(final MovementType movement, final double x, final double y) {
        this.currentState = movement.convert();
        this.body.setLinearVelocity(new Vector2(x, y));
    }

     /**
     * Modifies the maximum velocity of this {@link DynamicPhysicalBody} to a custom one.
     * This method is protected because the physical parameters of a body shouldn't be changed 
     * by classes that do not manage the physics of the game.
     * @param multiplierX the multiplier for the horizontal maximum velocity
     * @param multiplierY the multiplier for the vertical maximum velocity
     */
    protected void setMaxVelocity(final double multiplierX, final double multiplierY) {
        this.maxVelocityX = MAXVELOCITY_X * multiplierX;
        this.maxVelocityY = MAXVELOCITY_Y * multiplierY;
    }
}
