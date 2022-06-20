package de.suzufa.screwbox.examples.pathfinding;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL.CollisionMap;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype ENEMIES = Archetype.of(
            EnemyMovementComponent.class, PhysicsBodyComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        Entity worldBounds = engine.entityEngine().forcedFetch(WorldBoundsComponent.class, TransformComponent.class);
        Bounds bounds = worldBounds.get(TransformComponent.class).bounds;
        CollisionMap collisionMap = new CollisionMap(bounds, 16);
        collisionMap.update(engine.entityEngine());

        Entity player = engine.entityEngine().forcedFetch(PlayerMovementComponent.class, TransformComponent.class);
        Vector playerPosition = player.get(TransformComponent.class).bounds.position();

        for (Entity enemy : engine.entityEngine().fetchAll(ENEMIES)) {
            Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();
        }

    }

}
