package io.github.srcimon.screwbox.core.ui;

import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.TextDrawOptions;

import static io.github.srcimon.screwbox.core.assets.FontsBundle.BOLDZILLA;
import static io.github.srcimon.screwbox.core.graphics.Color.WHITE;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;

public class SimpleUiRenderer implements UiRenderer {

    private static final TextDrawOptions SELECTABLE = TextDrawOptions.font(BOLDZILLA.white()).scale(2).alignCenter();
    private static final TextDrawOptions SELECTED = TextDrawOptions.font(BOLDZILLA.customColor(YELLOW)).scale(2.5).alignCenter();
    private static final TextDrawOptions INACTIVE = TextDrawOptions.font(BOLDZILLA.customColor(WHITE.opacity(0.2))).scale(2).alignCenter();

    @Override
    public void renderSelectableItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, SELECTABLE);
    }

    @Override
    public void renderSelectedItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, SELECTED);
    }

    @Override
    public void renderInactiveItem(String label, ScreenBounds bounds, Screen screen) {
        screen.drawText(bounds.center(), label, INACTIVE);
    }

}
