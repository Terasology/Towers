// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.module.health.events.DoDamageEvent;
import org.terasology.towers.events.ApplyEffectEvent;

/**
 * Deals plain damage to the target
 *
 * @see DamageEffectorComponent
 */
@RegisterSystem
public class DamageEffectorSystem extends BaseComponentSystem {
    /**
     * Called to apply the effect to the target of the event.
     * <p>
     * Filters on DamageEffectorComponent
     *
     * @see ApplyEffectEvent
     */
    @ReceiveEvent
    public void onApplyEffect(ApplyEffectEvent event, EntityRef entity, DamageEffectorComponent component) {
        event.getTarget().send(new DoDamageEvent(component.damage));
    }
}
