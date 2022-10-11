package de.suzufa.screwbox.core.physics.internal;

import static de.suzufa.screwbox.core.utils.MathUtil.modifier;
import static de.suzufa.screwbox.core.utils.MathUtil.haveSameSign;
import static java.lang.Math.abs;

import de.suzufa.screwbox.core.Vector;

public final class CollisionResolver {

    private CollisionResolver() {
    }

    public static void resolveCollision(final CollisionCheck check, final double updateFactor) {
        final Vector correction = getResolveVector(check);
        if (correction.x() == 0) {
            reactOnVerticalCollision(check, updateFactor);
        } else {
            reactOnHorizontalCollision(check, correction);
        }
        check.physicsBounds().bounds = check.physicsBounds().bounds.moveBy(correction);
    }

    private static Vector getResolveVector(final CollisionCheck check) {
        final var colliderBounds = check.colliderBounds();
        final var entityBounds = check.physicsBounds().bounds;

        final boolean colliderBelowPhysics = abs(colliderBounds.maxY() - entityBounds.minY()) < abs(
                colliderBounds.minY() - entityBounds.maxY());

        final double deltaY = colliderBelowPhysics
                ? colliderBounds.maxY() - entityBounds.minY()
                : colliderBounds.minY() - entityBounds.maxY();

        final boolean colliderLeftOfPhysics = abs(colliderBounds.maxX() - entityBounds.minX()) < abs(
                colliderBounds.minX() - entityBounds.maxX());

        final double deltaX = colliderLeftOfPhysics
                ? colliderBounds.maxX() - entityBounds.minX()
                : colliderBounds.minX() - entityBounds.maxX();

        if (abs(deltaY) < abs(deltaX)) {
            return Vector.yOnly(deltaY);
        }
        return Vector.xOnly(deltaX);
    }

    private static void reactOnHorizontalCollision(final CollisionCheck pair, final Vector correction) {
        final var physicsBodyComponent = pair.physicsBodyComponent();
        final var colliderComponent = pair.colliderComponent();
        // prevents getting stuck when going off a cliff
        if (haveSameSign(correction.x(), physicsBodyComponent.momentum.x())) {
            return;
        }
        final Vector newMomentum = Vector.of(
                physicsBodyComponent.momentum.x() * -1 * colliderComponent.bounce.value(),
                physicsBodyComponent.momentum.y());
        physicsBodyComponent.momentum = newMomentum;
    }

    private static void reactOnVerticalCollision(final CollisionCheck pair, final double updateFactor) {
        final var physicsBodyComponent = pair.physicsBodyComponent();
        final var colliderComponent = pair.colliderComponent();
        final Vector newMomentum = Vector.of(
                physicsBodyComponent.momentum.x(),
                physicsBodyComponent.momentum.y() * -1 * colliderComponent.bounce.value());

        final double absX = abs(newMomentum.x());
        final double friction = Math.min(absX, colliderComponent.friction * updateFactor);

        physicsBodyComponent.momentum = newMomentum.addX(modifier(newMomentum.x()) * friction * -1);
    }
}
