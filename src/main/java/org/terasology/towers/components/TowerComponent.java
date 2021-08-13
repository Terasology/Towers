// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;

import java.util.HashSet;
import java.util.Set;

/**
 * Component for the abstract tower entity.
 * Stores the IDs of all the blocks that make up the tower.
 * <p>
 * Only collates together the component parts of the tower.
 */
public class TowerComponent implements Component {
    public Set<EntityRef> cores = new HashSet<>();
    public Set<EntityRef> effector = new HashSet<>();
    public Set<EntityRef> targeter = new HashSet<>();
    public Set<EntityRef> plains = new HashSet<>();
}
