package io.github.srcimon.screwbox.helloworld;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.helloworld.archivements.BestClickerArchivement;

import static io.github.srcimon.screwbox.core.assets.FontBundle.BOLDZILLA;

public class HelloWorldApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello World");

        screwBox.archivements().addAllFromClassPackage(BestClickerArchivement.class);

        screwBox.environment().addSystem(engine -> {
            int y = 0;
            for (var archivement : engine.archivements().allArchivements()) {
                engine.graphics().canvas().drawText(engine.graphics().canvas().center().addY(y += 30),
                        archivement.title() + " : " + archivement.score() + " of " + archivement.goal(),
                        TextDrawOptions.font(BOLDZILLA.customColor(archivement.isCompleted() ? Color.GREEN : Color.WHITE.opacity(archivement.progress().add(0.5)))).alignCenter().scale(2));
            }


        });
        screwBox.start();
    }
}