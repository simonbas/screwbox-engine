package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.graphics.Camera;
import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultViewportTest {

    @Mock
    Canvas canvas;

    @Mock
    Camera camera;

    DefaultViewport viewport;

    @BeforeEach
    void setUp() {
        viewport = new DefaultViewport(canvas, camera);
    }

    @Test
    void toCanvas_boundsIntersectingCanvas_returnsBounds() {
        when(camera.focus()).thenReturn($(70, 180));
        when(camera.zoom()).thenReturn(1.5);
        when(canvas.width()).thenReturn(90);
        when(canvas.height()).thenReturn(50);

        var result = viewport.toCanvas($$(40, 100, 100, 400));

        assertThat(result).isEqualTo(new ScreenBounds(0, -95, 150, 600));
    }
}
