package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

public interface Viewport {

    Offset toCanvas(Vector position);

    Bounds visibleArea();

    Vector toWorld(Offset offset);

    ScreenBounds toCanvas(Bounds bounds);

    int toCanvas(double worldDistance);

    ScreenBounds toCanvas(Bounds bounds, double parallaxX, double parallaxY);

    Camera camera();

    Canvas canvas();
}