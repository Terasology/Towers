// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.towers.EffectCount;
import org.terasology.towers.EffectDuration;
import org.terasology.towers.components.TowerEffector;

/**
 * Effector that only deals damage to the enemies.
 *
 * @see DamageEffectorSystem
 * @see TowerEffector
 */
public class DamageEffectorComponent extends TowerEffector {
    /**
     * The damage to apply to the targets.
     */
    public int damage;

    @Override
    public EffectCount getEffectCount() {
        return EffectCount.PER_SHOT;
    }

    @Override
    public EffectDuration getEffectDuration() {
        return EffectDuration.INSTANT;
    }
}
