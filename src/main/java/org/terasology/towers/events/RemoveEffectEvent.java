// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

/**
 * Event sent to remove an effect from the target
 * Sent against the Effector that applied the effect.
 *
 */
public class RemoveEffectEvent implements Event {
    private final EntityRef target;
    private final float multiplier;

    public RemoveEffectEvent(EntityRef target, float multiplier) {
        this.target = target;
        this.multiplier = multiplier;
    }

    /**
     * @return The entity to remove the effect from.
     */
    public EntityRef getTarget() {
        return target;
    }

    /**
     * @return The moderating damage multiplier to use for this effect
     */
    public float getMultiplier() {
        return multiplier;
    }
}
