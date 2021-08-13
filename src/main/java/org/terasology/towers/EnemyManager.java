// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.towers;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.delay.DelayManager;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles all enemy based actions.
 */
@Share(EnemyManager.class)
@RegisterSystem
public class EnemyManager extends BaseComponentSystem {

    private final Set<EntityRef> enemies = new HashSet<>();

    @In
    private EntityManager entityManager;
    @In
    private DelayManager delayManager;

    /**
     * Obtain all the enemies that are within range of the given position.
     *
     * @param pos   The position to look for
     * @param range The range to search in.
     * @return A set of all enemies found within this range.
     */
    public Set<EntityRef> getEnemiesInRange(Vector3f pos, float range) {
        float rangeSqr = range * range;
        Set<EntityRef> result = new HashSet<>();
        for (EntityRef enemy : enemies) {
            Vector3f enemyPos = enemy.getComponent(LocationComponent.class).getWorldPosition(new Vector3f());
            if (enemyPos.distanceSquared(pos) <= rangeSqr) {
                result.add(enemy);
            }
        }
        return result;
    }
}
