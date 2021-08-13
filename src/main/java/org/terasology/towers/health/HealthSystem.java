// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.health;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.towers.health.events.DamageEntityEvent;
import org.terasology.towers.health.events.EntityDeathEvent;

/**
 * Handles operations involving health on entities.
 *
 * @see HealthComponent
 */
@RegisterSystem
public class HealthSystem extends BaseComponentSystem {

    /**
     * Deals damage to an entity.
     * If the entity's health reaches zero it sends a destruction event to be handled
     * <p>
     * Filters on {@link HealthComponent}
     *
     * @see DamageEntityEvent
     */
    @ReceiveEvent
    public void onDamageEntity(DamageEntityEvent event, EntityRef entity, HealthComponent component) {
        component.health = Math.max(component.health - event.getDamage(), 0);
        if (component.health == 0) {
            entity.send(new EntityDeathEvent());
        }
    }
}
