// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.targeters;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.towers.EnemyManager;
import org.terasology.towers.InWorldRenderer;
import org.terasology.towers.events.SelectEnemiesEvent;

/**
 * Targets a single enemy within range.
 *
 */
@RegisterSystem
public class SingleTargeterSystem extends BaseTargeterSystem {

    @In
    protected EnemyManager enemyManager;
    @In
    private InWorldRenderer inWorldRenderer;

    /**
     * Determine which enemies should be attacked.
     * Called against the targeter entity.
     * <p>
     * Filters on {@link LocationComponent} and {@link SingleTargeterComponent}
     *
     * @see SelectEnemiesEvent
     */
    @ReceiveEvent
    public void onDoSelectEnemies(SelectEnemiesEvent event, EntityRef entity, LocationComponent locationComponent, SingleTargeterComponent targeterComponent) {
        EntityRef target = getTarget(locationComponent.getWorldPosition(new Vector3f()), targeterComponent, enemyManager);

        if (target.exists()) {
            event.addToList(target);
        }
        targeterComponent.lastTarget = target;

    }


}
