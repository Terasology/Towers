// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.targeters;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.players.PlayerCharacterComponent;
import org.terasology.engine.registry.In;
import org.terasology.towers.events.SelectEnemiesEvent;


/**
 * Targets a single enemy within range.
 *
 */
@RegisterSystem
public class SingleTargeterSystem extends BaseComponentSystem {

    @In
    private EntityManager entityManager;
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
        for (EntityRef player : entityManager.getEntitiesWith(PlayerCharacterComponent.class)) {
            Vector3f playerLoc = new Vector3f();
            Vector3f towerLoc = new Vector3f();
            player.getComponent(LocationComponent.class).getWorldPosition(playerLoc);
            locationComponent.getWorldPosition(towerLoc);
            float distance = playerLoc.distance(towerLoc);
            if (distance < 15) {
                event.addToList(player);
                break;
            }
        }
    }

}
