package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.srcimon.screwbox.core.Bounds.$$;
import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Vector.$;
import static io.github.srcimon.screwbox.core.graphics.CameraShakeOptions.infinite;
import static io.github.srcimon.screwbox.core.graphics.CameraShakeOptions.lastingForDuration;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCameraTest {

    @Mock
    DefaultWorld world;

    @InjectMocks
    DefaultCamera camera;

    @Test
    void moveCameraWithinVisualBounds_cameraIsWithinBounds_updatesCameraPosition() {
        camera.updatePosition($(10, 10));
        when(world.visibleArea()).thenReturn($$(-50, -50, 100, 100));

        var result = camera.moveWithinVisualBounds($(20, 20), $$(-200, -20, 400, 400));

        verify(world).updateCameraPosition($(30, 30));
        assertThat(result).isEqualTo($(30, 30));
    }

    @Test
    void updatePosition_positionNull_throwsException() {
        assertThatThrownBy(() -> camera.updatePosition(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("position must not be NULL");
    }


    @Test
    void updateZoomRestriction_minNegative_exception() {
        assertThatThrownBy(() -> camera.updateZoomRestriction(-2, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("min zoom must be positive");
    }

    @Test
    void updateZoomRestriction_maxBelowMin_exception() {
        assertThatThrownBy(() -> camera.updateZoomRestriction(1, 0.3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("max zoom must not be lower than min zoom");
    }

    @Test
    void updateZoomRestriction_validMinAndMax_restrictsZoomRange() {
        camera.updateZoomRestriction(1, 5);

        assertThat(camera.updateZoom(0.2)).isEqualTo(1);
        assertThat(camera.zoom()).isEqualTo(1);

        assertThat(camera.updateZoom(12)).isEqualTo(5);
        assertThat(camera.zoom()).isEqualTo(5);
    }

    @Test
    void isShaking_noActiveShake_isFalse() {
        assertThat(camera.isShaking()).isFalse();
    }

    @Test
    void isShaking_hasActiveShake_isTrue() {
        camera.shake(infinite());

        camera.update();

        assertThat(camera.isShaking()).isTrue();
    }

    @Test
    void isShaking_shakeHasEnded_isFalse() {
        camera.shake(lastingForDuration(ofMillis(10)));
        TestUtil.sleep(ofMillis(20));

        camera.update();

        assertThat(camera.isShaking()).isFalse();
    }
}
