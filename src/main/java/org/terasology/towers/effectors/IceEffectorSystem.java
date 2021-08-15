// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.effectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.CharacterMovementComponent;
import org.terasology.engine.registry.In;
import org.terasology.towers.InWorldRenderer;
import org.terasology.towers.events.ApplyEffectEvent;
import org.terasology.towers.events.RemoveEffectEvent;

/**
 * Slows the target enemy by the given amount.
 *
 * @see IceEffectorComponent
 */
@RegisterSystem
public class IceEffectorSystem extends BaseComponentSystem {
    @In
    private InWorldRenderer inWorldRenderer;
    private static final Logger logger = LoggerFactory.getLogger(IceEffectorSystem.class);

    /**
     * Applies the slow effect to the target
     * <p>
     * Filters on {@link IceEffectorComponent}
     *
     * @see ApplyEffectEvent
     */
    @ReceiveEvent
    public void onApplyEffect(ApplyEffectEvent event, EntityRef entity, IceEffectorComponent component) {
        EntityRef enemy = event.getTarget();
        CharacterMovementComponent movementComponent = enemy.getComponent(CharacterMovementComponent.class);
        double reducedSpeed = movementComponent.speedMultiplier * component.slow;
        movementComponent.speedMultiplier = (float) reducedSpeed;
        enemy.saveComponent(movementComponent);
        inWorldRenderer.addParticleEffect(enemy, "Towers:IceParticleEffect");
    }

    /**
     * Removes the slow effect from the target
     * <p>
     * Filters on {@link IceEffectorComponent}
     *
     * @see RemoveEffectEvent
     */
    @ReceiveEvent
    public void onRemoveEffect(RemoveEffectEvent event, EntityRef entity, IceEffectorComponent component) {
        EntityRef enemy = event.getTarget();
        CharacterMovementComponent movementComponent = enemy.getComponent(CharacterMovementComponent.class);
        movementComponent.speedMultiplier = movementComponent.speedMultiplier / component.slow;
        inWorldRenderer.removeParticleEffect(enemy, "Towers:IceParticleEffect");
    }
}
