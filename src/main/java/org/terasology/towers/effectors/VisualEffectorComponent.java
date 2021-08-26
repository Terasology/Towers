// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.towers.EffectCount;
import org.terasology.towers.EffectDuration;
import org.terasology.towers.components.TowerEffector;

/**
 * Test effector.
 * Simply increases the size of the enemy in order to help identify it for debugging.
 *
 * @see TowerEffector
 */
public class VisualEffectorComponent extends TowerEffector<VisualEffectorComponent> {
    @Override
    public EffectCount getEffectCount() {
        return EffectCount.CONTINUOUS;
    }

    @Override
    public EffectDuration getEffectDuration() {
        return EffectDuration.LASTING;
    }

    @Override
    public void copyFrom(VisualEffectorComponent other) { }
}
