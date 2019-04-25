package it.unibo.jmpcoon.model.physics;

import org.apache.commons.lang3.tuple.Pair;
import org.dyn4j.geometry.Vector2;

/**
 * A utility class with methods for calculating positions of {@link PhysicalBody}s with respect to one another.
 */
public final class PhysicsUtils {
    private static final double PRECISION = 0.03;

    private PhysicsUtils() {
    }

    /**
     * Calculates if the first {@link PhysicalBody} is over the top of the other, as the one which is below is a rectangle and
     * they can make contact on the top side.
     * @param aboveBody the {@link PhysicalBody} which should be above
     * @param belowBody the {@link PhysicalBody} which should be below
     * @param contactPoint the contact point in world coordinates
     * @return true if the first {@link PhysicalBody} is above the second {@link PhysicalBody} below 
     */
    public static boolean isBodyOnTop(final PhysicalBody aboveBody, final PhysicalBody belowBody,
                                      final Pair<Double, Double> contactPoint) {
        return isContactAtEdgeBody(aboveBody, contactPoint, true) && isContactAtEdgeBody(belowBody, contactPoint, false);
    }

    /**
     * Calculates if the first {@link PhysicalBody} is overlapping with the second {@link PhysicalBody} in a point
     * which is below the center of the second {@link PhysicalBody}, ignoring angles of rotation.
     * @param bottomBody the body which should be at the bottom
     * @param topBody the body which should be at the top
     * @return true if the first {@link PhysicalBody} is at the bottom half of the second {@link PhysicalBody}
     */
    public static boolean isBodyAtBottomHalf(final PhysicalBody bottomBody, final PhysicalBody topBody) {
        return (bottomBody.getPosition().getRight() + bottomBody.getDimensions().getRight() / 2)
               <= topBody.getPosition().getRight();
    }

    /**
     * Calculates if the first {@link PhysicalBody} is inside the shape of the other {@link PhysicalBody}. This is true
     * if the center of the first {@link PhysicalBody} is distant from the center of the second {@link PhysicalBody} less or
     * equal than a quarter of the width of the second {@link PhysicalBody}.
     * @param insideBody the {@link PhysicalBody} that should be contained in the shape of the second {@link PhysicalBody}
     * @param outsideBody the {@link PhysicalBody} that should contain with its shape the first
     * @return true if the first {@link PhysicalBody} is inside the shape of the second
     */
    public static boolean isBodyInside(final PhysicalBody insideBody, final PhysicalBody outsideBody) {
        return Math.abs(insideBody.getPosition().getLeft() - outsideBody.getPosition().getLeft()) 
               <= outsideBody.getDimensions().getLeft() / 4;
    }

    /**
     * Calculates if the first {@link PhysicalBody} is over the top of the other, as the one which is below is a circle and
     * they can make contact on any point on the upper half of the body which should be below.
     * @param aboveBody the {@link PhysicalBody} which should be above
     * @param belowBody the {@link PhysicalBody} which should be below
     * @param contactPoint the contact point in world coordinates
     * @return true if the first {@link PhysicalBody} is above the second {@link PhysicalBody} below 
     */
    public static boolean isBodyAbove(final PhysicalBody aboveBody, final PhysicalBody belowBody,
                                      final Pair<Double, Double> contactPoint) {
        return isContactAtEdgeBody(aboveBody, contactPoint, true)
               && contactPoint.getRight() >= (belowBody.getPosition().getRight() + belowBody.getDimensions().getRight() / 4);
    }

    /*
     * Calculates if a given contact point is at the edge of a physical body, considering also its rotation with respect to
     * the world axis, on its top or on its bottom depending on the passed parameter.
     */
    private static boolean isContactAtEdgeBody(final PhysicalBody body, final Pair<Double, Double> contact, final boolean onTop) {
        final double slope = Math.tan(body.getAngle());
        if (Double.compare(slope, 0) == 0) {
            return Math.abs((body.getPosition().getRight() + (onTop ? -1 : 1) * body.getDimensions().getRight() / 2)
                            - contact.getRight()) < PRECISION;
        }
        final double interPerp = contact.getRight() + (1 / slope) * contact.getLeft();
        final double interParalAbove = body.getPosition().getRight() - slope * body.getPosition().getLeft();
        final double newXAbove = (interPerp - interParalAbove) * slope / (1 + Math.pow(slope, 2));
        final double newYAbove = slope * newXAbove + interParalAbove;
        final Vector2 rotatedHalfHeight = new Vector2(0, (onTop ? -1 : 1) * body.getDimensions().getRight() / 2)
                                          .rotate(body.getAngle())
                                          .add(newXAbove, newYAbove);
        return Math.abs(rotatedHalfHeight.y - contact.getRight()) < PRECISION;
    }
}
