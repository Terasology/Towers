// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;


/**
 * A class that provides Static information about the Defence Field.
 *
 */
public final class DefenceField {

    /**
     * Private constructor as class is a utility class and should not be instantiated.
     */
    private DefenceField() {
    }

    /**
     * Helper method for getting a component given one of its superclasses
     *
     * @param entity     The entity to search on
     * @param superClass The superclass of the component to filter for
     * @param <Y>        The type of the superclass
     * @return The component that extends the superclass
     */
    public static <Y> Y getComponentExtending(EntityRef entity, Class<Y> superClass) {
        if (!entity.exists()) {
            throw new IllegalArgumentException("Component extending " + superClass.getSimpleName() + " requested from a null entity");
        }
        for (Component component : entity.iterateComponents()) {
            if (superClass.isInstance(component)) {
                return superClass.cast(component);
            }
        }
        throw new IllegalArgumentException("Entity didn't have any component extending " + superClass.getSimpleName());
    }
}
