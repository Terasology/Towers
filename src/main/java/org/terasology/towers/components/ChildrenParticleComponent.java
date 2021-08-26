// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import com.google.common.collect.Maps;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Marks that an entity has one or more child particle entities.
 * Also provides a reference to them.
 *
 */
public class ChildrenParticleComponent implements Component<ChildrenParticleComponent> {
    /**
     * The particles attached to the entity.
     * The key used for this map is the prefab of the entity
     */
    public Map<String, EntityRef> particleEntities = new HashMap<>();

    @Override
    public void copyFrom(ChildrenParticleComponent other) {
        this.particleEntities = Maps.newHashMap(other.particleEntities);
    }

}
