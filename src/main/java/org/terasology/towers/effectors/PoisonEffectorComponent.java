// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.towers.EffectCount;
import org.terasology.towers.EffectDuration;

/**
 * Deals an initial damage and then a smaller damage over time.
 *
 */
public class PoisonEffectorComponent extends DamageEffectorComponent {
    /**
     * The damage dealt by each iteration of the poisoning
     */
    public int poisonDamage;
    /**
     * How long the poison will last for
     * given in milliseconds
     */
    public int poisonDuration;

    @Override
    public EffectCount getEffectCount() {
        return EffectCount.PER_SHOT;
    }

    @Override
    public EffectDuration getEffectDuration() {
        return EffectDuration.PERMANENT;
    }

}
