package de.suzufa.screwbox.playground.debo.menues;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.ui.UiMenu;
import de.suzufa.screwbox.core.ui.UiMenuItem;
import de.suzufa.screwbox.playground.debo.scenes.GameScene;
import de.suzufa.screwbox.playground.debo.scenes.StartScene;

public class PauseMenu extends UiMenu {

    private static final String SAVEGAME_NAME = "savegame.sav";

    public PauseMenu() {
        add(new PauseMenuResumeGame());
        add(new UiMenuItem("Save Game") {

            @Override
            public void onActivate(Engine engine) {
                engine.savegame().create(SAVEGAME_NAME, GameScene.class);
                new PauseMenuResumeGame().onActivate(engine);
            }

        });
        add(new UiMenuItem("Load Game") {

            @Override
            public void onActivate(Engine engine) {
                engine.savegame().load(SAVEGAME_NAME, GameScene.class);
                new PauseMenuResumeGame().onActivate(engine);

            }

        }.activeCondition(engine -> engine.savegame().exists(SAVEGAME_NAME)));

        add(new UiMenuItem("Options") {

            @Override
            public void onActivate(Engine engine) {
                engine.ui().openMenu(new OptionsMenu(new PauseMenu()));
            }
        });
        add(new UiMenuItem("Back to menu") {

            @Override
            public void onActivate(Engine engine) {
                engine.scenes().switchTo(StartScene.class);
            }
        });
        add(new UiMenuItem("Quit Game") {

            @Override
            public void onActivate(Engine engine) {
                engine.stop();
            }
        });
    }
}
