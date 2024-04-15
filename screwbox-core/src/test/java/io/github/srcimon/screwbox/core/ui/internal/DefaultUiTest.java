package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.scenes.internal.DefaultScenes;
import io.github.srcimon.screwbox.core.ui.*;
import io.github.srcimon.screwbox.core.utils.Latch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUiTest {

    @InjectMocks
    DefaultUi ui;

    @Mock
    Engine engine;

    @Mock
    DefaultScenes scenes;

    @Mock
    UiInteractor interactor;

    @Mock
    UiLayouter layouter;

    @Mock
    UiRenderer renderer;

    @BeforeEach
    void beforeEach() {
        ui
                .setInteractor(interactor)
                .setLayouter(layouter)
                .setRenderer(renderer);
    }

    @Test
    void update_noMenu_noInteractionOrRendering() {
        ui.update();

        verify(interactor, never()).interactWith(any(), any(), any());
        verify(layouter, never()).calculateBounds(any(), any(), any());
        verify(renderer, never()).renderInactiveItem(any(), any(), any());
        verify(renderer, never()).renderSelectableItem(any(), any(), any());
        verify(renderer, never()).renderSelectedItem(any(), any(), any());
    }

    @Test
    void update_interactorDisablesCurrentSelection_switchesToNextMenuItem() {
        when(scenes.isShowingLoading()).thenReturn(false);
        var activated = Latch.of(false, true);

        Graphics graphics = mock(Graphics.class);
        Screen screen = mock(Screen.class);
        when(graphics.screen()).thenReturn(screen);
        when(engine.graphics()).thenReturn(graphics);
        UiMenu menu = new UiMenu();
        UiMenuItem firstItem = menu.addItem("some button").activeCondition(e -> activated.active())
                .onActivate(engine -> activated.toggle());
        menu.addItem("some button");
        ScreenBounds layoutBounds = new ScreenBounds(Offset.at(20, 40), Size.of(100, 20));
        when(screen.isVisible(layoutBounds)).thenReturn(true);
        when(layouter.calculateBounds(firstItem, menu, screen)).thenReturn(layoutBounds);
        ui.openMenu(menu);

        assertThat(menu.activeItemIndex()).isZero();

        ui.update();

        assertThat(menu.activeItemIndex()).isEqualTo(1);
    }

    @Test
    void update_menuPresent_interactsAndRendersMenu() {
        when(scenes.isShowingLoading()).thenReturn(false);
        Graphics graphics = mock(Graphics.class);
        Screen screen = mock(Screen.class);
        when(graphics.screen()).thenReturn(screen);
        when(engine.graphics()).thenReturn(graphics);
        UiMenu menu = new UiMenu();
        UiMenuItem item = menu.addItem("some button");
        ScreenBounds layoutBounds = new ScreenBounds(Offset.at(20, 40), Size.of(100, 20));
        when(screen.isVisible(layoutBounds)).thenReturn(true);
        when(layouter.calculateBounds(item, menu, screen)).thenReturn(layoutBounds);
        ui.openMenu(menu);

        ui.update();

        verify(interactor).interactWith(menu, layouter, engine);
        verify(renderer).renderSelectedItem("some button", layoutBounds, screen);
    }

    @Test
    void currentMenu_menuOpen_returnsMenu() {
        UiMenu menu = new UiMenu();

        ui.openMenu(menu);

        assertThat(ui.currentMenu()).hasValue(menu);
    }

    @Test
    void currentMenu_noOpenMenu_isEmpty() {
        assertThat(ui.currentMenu()).isEmpty();
    }

    @Test
    void openPreviousMenu_noPreviousMenu_throwsException() {
        assertThatThrownBy(() -> ui.openPreviousMenu())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("there is no previous menu to navigate back to");
    }

    @Test
    void openPreviousMenu_previousMenuPresent_switchesBackToPreviousMenu() {
        UiMenu optionsMenu = new UiMenu();
        ui.openMenu(optionsMenu);
        ui.openMenu(new UiMenu());

        ui.openPreviousMenu();

        assertThat(ui.currentMenu()).contains(optionsMenu);
    }

    @Test
    void openMenu_menuConsumerGiven_addsCustomizedMenu() {
        ui.openMenu(menu -> {
            menu.addItem("test1");
            menu.addItem("test2");
        });

        assertThat(ui.currentMenu()).isPresent();
        assertThat(ui.currentMenu().get().itemCount()).isEqualTo(2);
    }

}
