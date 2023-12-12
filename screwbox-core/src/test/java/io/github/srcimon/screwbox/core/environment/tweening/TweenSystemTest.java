package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.internal.DefaultEnvironment;
import io.github.srcimon.screwbox.core.loop.Loop;
import io.github.srcimon.screwbox.core.test.EnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(EnvironmentExtension.class)
class TweenSystemTest {

    @Test
    void update_updatesProgression(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now());

        Entity smoke = new Entity().add(new TweenComponent(Duration.ofMillis(200), false, false));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(smoke.get(TweenComponent.class).progress.isMinValue()).isFalse();
    }

    @Test
    void update_progressionMax_entityRemoved(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now().plusSeconds(4));

        Entity smoke = new Entity().add(new TweenComponent(Duration.ofMillis(200), false, false));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(environment.entities()).isEmpty();
    }

    @Test
    void update_loopedInterval_entityNotRemoved(DefaultEnvironment environment, Loop loop) {
        when(loop.lastUpdate()).thenReturn(Time.now().plusSeconds(4));

        Entity smoke = new Entity().add(new TweenComponent(Duration.ofMillis(200), true, false));

        environment
                .addEntity(smoke)
                .addSystem(new TweenSystem());

        environment.update();

        assertThat(environment.entities()).contains(smoke);
    }
}