package de.suzufa.screwbox.core.resources;

import de.suzufa.screwbox.core.entityengine.Entity;

public interface EntityConverter<T> {

    @Deprecated
    boolean accepts(final T object);

    Entity convert(final T object);
}
