// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.logic.ingame.towers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.gestalt.entitysystem.component.Component;

@ForceBlockActive
public class TowerComponent implements Component<TowerComponent> {
    @Replicate
    public boolean isActivated = false;
    @Replicate
    public int range = 10;
    @Replicate
    public EntityRef childEntity = EntityRef.NULL;

    @Override
    public void copyFrom(TowerComponent other) {
        this.isActivated = other.isActivated;
        this.range = other.range;
        this.childEntity = other.childEntity;
    }
}
