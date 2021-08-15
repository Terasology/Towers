// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;

/**
 * Component for the abstract tower entity.
 * Stores the IDs of all the blocks that make up the tower.
 * <p>
 * Only collates together the component parts of the tower.
 */
public class TowerComponent implements Component {
    public EntityRef effector;
    public EntityRef targeter;
}
