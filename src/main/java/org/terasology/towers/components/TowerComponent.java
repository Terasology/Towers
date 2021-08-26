// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Component for the abstract tower entity.
 * Stores the IDs of all the blocks that make up the tower.
 * <p>
 * Only collates together the component parts of the tower.
 */
public class TowerComponent implements Component<TowerComponent> {
    public EntityRef effector;
    public EntityRef targeter;

    @Override
    public void copyFrom(TowerComponent other) {
        this.effector = other.effector;
        this.targeter = other.targeter;
    }
}
