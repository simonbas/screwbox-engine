package de.suzufa.screwbox.core.entityengine.components;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.entityengine.Component;

public class ForwardSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final List<Integer> listenerIds = new ArrayList<>();
}
