package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.systems.PhysicsDebugSystem;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class DebugConfigSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        if (engine.keyboard().isPressed(Key.O)) {
            engine.entities().toggleSystem(new PhysicsDebugSystem());
        }
        if (engine.keyboard().isPressed(Key.L)) {
            engine.entities().toggleSystem(new ShowFpsSystem());
            engine.loop().setTargetFps(engine.entities().isSystemPresent(ShowFpsSystem.class) ? 120 : 10000);
        }
    }
}
