// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.targeters;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.towers.components.TowerTargeter;

/**
 * Targets a single enemy within range.
 *
 * @see SingleTargeterSystem
 * @see TowerTargeter
 */
public class SingleTargeterComponent extends TowerTargeter {
    /**
     * The enemy attacked last attack
     * Not always applicable
     */
    public EntityRef lastTarget = EntityRef.NULL;

    @Override
    public float getMultiplier() {
        return 1;
    }
}
