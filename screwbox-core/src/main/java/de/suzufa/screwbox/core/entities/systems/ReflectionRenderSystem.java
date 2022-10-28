package de.suzufa.screwbox.core.entities.systems;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ReflectionComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.graphics.SpriteBatch;

public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, SpriteComponent.class);

    @Override
    public void update(Engine engine) {
        List<Entity> reflectableEntities = engine.entities().fetchAll(RELECTED_ENTITIES);
        for (Entity reflectionArea : engine.entities().fetchAll(REFLECTING_AREAS)) {
            ReflectionComponent reflection = reflectionArea.get(ReflectionComponent.class);
            double waveSeed = engine.loop().lastUpdate().milliseconds() / 500.0;
            Bounds reflectionAreaBounds = reflectionArea.get(TransformComponent.class).bounds;
            var reflectedArea = reflectionAreaBounds
                    .moveBy(Vector.yOnly(-reflectionAreaBounds.height()))
                    .inflatedTop(reflection.useWaveEffect ? 2 : 0);
            final SpriteBatch spriteBatch = new SpriteBatch();
            for (var reflectableEntity : reflectableEntities) {
                var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    final SpriteComponent spriteComponent = reflectableEntity.get(SpriteComponent.class);
                    final var spriteSize = spriteComponent.sprite.size();
                    final var spriteBounds = Bounds.atOrigin(
                            reflectableBounds.position().x() - spriteSize.width() / 2.0,
                            reflectableBounds.position().y() - spriteSize.height() / 2.0,
                            spriteSize.width() * spriteComponent.scale,
                            spriteSize.height() * spriteComponent.scale);

                    Vector oldPosition = spriteBounds.position();
                    double actualY = reflectionAreaBounds.minY() +
                            (reflectionAreaBounds.minY() - oldPosition.y());
                    var actualPosition = Vector.of(oldPosition.x(), actualY);

                    double waveMovementEffectX = reflection.useWaveEffect
                            ? Math.sin(waveSeed + actualY / 16) * 2
                            : 0;
                    double waveMovementEffectY = reflection.useWaveEffect
                            ? Math.sin(waveSeed) * 2
                            : 0;

                    Vector waveEffectPosition = actualPosition.addX(waveMovementEffectX).addY(waveMovementEffectY);

                    Bounds reflectionBounds = spriteBounds.moveTo(waveEffectPosition);
                    if (reflectionBounds.intersects(engine.graphics().world().visibleArea())) {
                        Percentage opacity = spriteComponent.opacity
                                .multiply(reflection.opacityModifier.value())
                                .multiply(reflection.useWaveEffect ? Math.sin(waveSeed) * 0.25 + 0.75 : 1);

                        spriteBatch.addEntry(
                                spriteComponent.sprite,
                                reflectionBounds.origin(),
                                spriteComponent.scale,
                                opacity,
                                spriteComponent.rotation,
                                spriteComponent.flipMode.invertVertical(),
                                spriteComponent.drawOrder);
                    }
                }
            }
            engine.graphics().world().drawSpriteBatch(spriteBatch, reflectionAreaBounds);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_EFFECTS;
    }

}
