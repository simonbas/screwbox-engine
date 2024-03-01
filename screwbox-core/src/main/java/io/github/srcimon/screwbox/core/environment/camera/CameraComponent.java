package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Lurk;

import java.io.Serial;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;

public final class CameraComponent implements Component {

    //TODO remove camera from Tiled Maps
    @Serial
    private static final long serialVersionUID = 1L;

    public double zoom;
    public Bounds visibleArea;

    public double shakeStrength = 0;
    public Lurk shakeX = Lurk.intervalWithDeviation(ofMillis(100), Percent.half());
    public Lurk shakeY = Lurk.intervalWithDeviation(ofMillis(100), Percent.half());

    public CameraComponent(final double zoom, final Bounds visibleArea) {
        this.zoom = zoom;
        this.visibleArea = visibleArea;
    }

}
