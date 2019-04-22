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
        final double slope = Math.tan(belowBody.getAngle());
        if (Double.compare(slope, 0) == 0) {
            return Math.abs((aboveBody.getPosition().getRight() - aboveBody.getDimensions().getRight() / 2)
                            - contactPoint.getRight()) < PRECISION
                   && Math.abs(contactPoint.getRight()
                               - (belowBody.getPosition().getRight() + belowBody.getDimensions().getRight() / 2)) < PRECISION;
        }
        final double interPerp = contactPoint.getRight() + (1 / slope) * contactPoint.getLeft();
        final double interParalAbove = aboveBody.getPosition().getRight() - slope * aboveBody.getPosition().getLeft();
        final double newXAbove = (interPerp - interParalAbove) * slope / (1 + Math.pow(slope, 2));
        final double newYAbove = slope * newXAbove + interParalAbove;
        final Vector2 rotatedHalfAboveHeight = new Vector2(0, -aboveBody.getDimensions().getRight() / 2)
                                               .rotate(aboveBody.getAngle())
                                               .add(newXAbove, newYAbove);
        final double interParalBelow = belowBody.getPosition().getRight() - slope * belowBody.getPosition().getLeft();
        final double newXBelow = (interPerp - interParalBelow) * slope / (1 + Math.pow(slope, 2));
        final double newYBelow = slope * newXBelow + interParalBelow;
        final Vector2 rotatedHalfBelowHeight = new Vector2(0, belowBody.getDimensions().getRight() / 2)
                                               .rotate(belowBody.getAngle())
                                               .add(newXBelow, newYBelow);
        return Math.abs(rotatedHalfAboveHeight.y - contactPoint.getRight()) < PRECISION
               && Math.abs(contactPoint.getRight() - rotatedHalfBelowHeight.y) < PRECISION;
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
        final double slope = Math.tan(aboveBody.getAngle());
        if (Double.compare(slope, 0) == 0) {
            return Math.abs((aboveBody.getPosition().getRight() - aboveBody.getDimensions().getRight() / 2)
                            - contactPoint.getRight()) < PRECISION
                   && (contactPoint.getRight()
                       >= (belowBody.getPosition().getRight() + belowBody.getDimensions().getRight() / 4));
        }
        final double interPerp = contactPoint.getRight() + (1 / slope) * contactPoint.getLeft();
        final double interParalAbove = aboveBody.getPosition().getRight() - slope * aboveBody.getPosition().getLeft();
        final double newXAbove = ((interPerp - interParalAbove) * slope) / (1 + Math.pow(slope, 2));
        final double newYAbove = slope * newXAbove + interParalAbove;
        final Vector2 rotatedHalfAboveHeight 
            = new Vector2(0, -(aboveBody.getDimensions().getRight() / 2)).rotate(belowBody.getAngle())
                                                                         .add(newXAbove, newYAbove);
        return Math.abs(rotatedHalfAboveHeight.y - contactPoint.getRight()) < PRECISION
               && (contactPoint.getRight()
                   >= (belowBody.getPosition().getRight() + belowBody.getDimensions().getRight() / 4));
    }
}
