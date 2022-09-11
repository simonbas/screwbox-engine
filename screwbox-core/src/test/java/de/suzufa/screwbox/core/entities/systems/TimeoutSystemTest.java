package de.suzufa.screwbox.core.entities.systems;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;
import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.loop.GameLoop;
import de.suzufa.screwbox.core.test.EntityEngineExtension;

@ExtendWith(EntityEngineExtension.class)
class TimeoutSystemTest {

    private static final Time NOW = Time.now();
    private static final Time LATER = NOW.plusSeconds(1);

    @Test
    void update_removesTimedOutComponents(DefaultEntities entityEngine, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(LATER);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(NOW));
        entityEngine.add(timedOutEntity);

        entityEngine.add(new TimeoutSystem());

        entityEngine.update();

        assertThat(entityEngine.entityCount()).isZero();
    }

    @Test
    void update_dosntTouchNonTimedOutComponents(DefaultEntities entityEngine, GameLoop loop) {
        when(loop.lastUpdate()).thenReturn(NOW);
        Entity timedOutEntity = new Entity().add(new TimeoutComponent(LATER));
        entityEngine.add(timedOutEntity);

        entityEngine.add(new TimeoutSystem());

        entityEngine.update();

        assertThat(entityEngine.entityCount()).isEqualTo(1);
    }
}
