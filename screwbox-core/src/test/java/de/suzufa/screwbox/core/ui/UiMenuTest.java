package de.suzufa.screwbox.core.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.suzufa.screwbox.core.Engine;

@ExtendWith(MockitoExtension.class)
class UiMenuTest {

    UiMenu menu;

    @Mock
    Engine engine;

    @BeforeEach
    void beforeEach() {
        menu = new UiMenu();
    }

    @Test
    void selectedItem_noItems_throwsException() {
        assertThatThrownBy(() -> menu.selectedItem())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("no menu item present");
    }

    @Test
    void selectedItem_itemsPresent_returnsSelectedItem() {
        var optionsItem = menu.addItem("Options");
        menu.addItem("Quit");

        assertThat(menu.selectedItem()).isEqualTo(optionsItem);
    }

    @Test
    void nextItem_itemsPresent_switchesActiveItem() {
        menu.addItem("Options");
        var quitItem = menu.addItem("Quit");

        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(quitItem);
    }

    @Test
    void nextItem_atEndOfList_doenstSwitchItem() {
        menu.addItem("Options");
        var quitItem = menu.addItem("Quit");

        menu.nextItem(engine);
        menu.nextItem(engine);
        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(quitItem);
    }

    @Test
    void itemCount_returnsItemCount() {
        menu.addItem("Options");

        assertThat(menu.itemCount()).isEqualTo(1);
    }

    @Test
    void nextItem_noActiveItem_doenstSwitchItem() {
        var optionsItem = menu.addItem("Options").activeCondition(e -> false);
        menu.addItem("Quit").activeCondition(e -> false);

        menu.nextItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(optionsItem);
    }

    @Test
    void previousItem_noActiveItem_doenstSwitchItem() {
        var optionsItem = menu.addItem("Options");
        menu.addItem("Quit");

        menu.previousItem(engine);

        assertThat(menu.selectedItem()).isEqualTo(optionsItem);
    }

}
