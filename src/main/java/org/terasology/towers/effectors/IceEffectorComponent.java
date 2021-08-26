// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.towers.EffectCount;
import org.terasology.towers.EffectDuration;
import org.terasology.towers.components.TowerEffector;

/**
 * Slows down an enemy.
 * It does not deal any damage to the enemy.
 *
 * @see IceEffectorSystem
 * @see TowerEffector
 */
public class IceEffectorComponent extends TowerEffector<IceEffectorComponent> {
    /**
     * A multiplier for the enemies speed.
     * 0.9 will make them 10% slower, or 90% of their full speed;
     */
    public float slow;

    @Override
    public EffectCount getEffectCount() {
        return EffectCount.CONTINUOUS;
    }

    @Override
    public EffectDuration getEffectDuration() {
        return EffectDuration.LASTING;
    }

    @Override
    public void copyFrom(IceEffectorComponent other) {
        this.slow = other.slow;
    }
}
