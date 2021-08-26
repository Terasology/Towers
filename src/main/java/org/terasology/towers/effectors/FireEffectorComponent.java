// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.towers.EffectCount;
import org.terasology.towers.EffectDuration;

/**
 * Deals a damage over time to the entity, that has a chance of spreading to other enemies.
 * @see FireEffectorSystem
 */
public class FireEffectorComponent extends DamageEffectorComponent {
    /**
     * How long the enemy is on fire for.
     * given in milliseconds
     */
    public int fireDuration;

    @Override
    public EffectCount getEffectCount() {
        return EffectCount.CONTINUOUS;
    }

    @Override
    public EffectDuration getEffectDuration() {
        return EffectDuration.PERMANENT;
    }

}
