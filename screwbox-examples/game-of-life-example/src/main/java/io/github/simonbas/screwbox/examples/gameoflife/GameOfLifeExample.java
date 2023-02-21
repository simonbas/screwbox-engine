package io.github.simonbas.screwbox.examples.gameoflife;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.ScrewBox;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.systems.LogFpsSystem;
import io.github.simonbas.screwbox.core.entities.systems.QuitOnKeyPressSystem;
import io.github.simonbas.screwbox.core.keyboard.Key;
import io.github.simonbas.screwbox.examples.gameoflife.components.GridComponent;
import io.github.simonbas.screwbox.examples.gameoflife.systems.*;

public class GameOfLifeExample {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Game Of Life Example");

        engine.entities()
                .add(new Entity().add(new GridComponent()))
                .add(new GridUpdateSystem())
                .add(new GridRenderSystem())
                .add(new GridInteractionSystem())
                .add(new PauseSystem(Key.SPACE))
                .add(new QuitOnKeyPressSystem(Key.ESCAPE))
                .add(new LogFpsSystem())
                .add(new CameraControlSystem());

        engine.graphics().configuration().setUseAntialiasing(true);

        engine.graphics().updateCameraZoom(6);
        engine.start();
    }
}