// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.targeters;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.towers.EnemyManager;
import org.terasology.towers.SelectionMethod;
import org.terasology.towers.components.TowerTargeter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A base system for tower targeters that provides common methods.
 * This system is not required to be extended by tower systems.
 *
 */
public class BaseTargeterSystem extends BaseComponentSystem {

    /**
     * Picks the target from all within range based upon the selection method
     *
     * @param targets         All enemies within range
     * @param selectionMethod The selection method
     * @return The single target, according to the selection method
     */
    protected EntityRef getSingleTarget(Set<EntityRef> targets, SelectionMethod selectionMethod) {
        if (selectionMethod == SelectionMethod.RANDOM) {
            List<EntityRef> listTargets = new ArrayList<>(targets);
            Collections.shuffle(listTargets);
            return listTargets.get(0);
        }
        return null;
    }

    /**
     * Checks if the enemy from last round can be reused.
     *
     * @param targeterPos       The position of the target
     * @param targeterComponent The targeter
     * @return True if the targeter can attack the enemy
     */
    private boolean canUseTarget(EntityRef target, Vector3f targeterPos, TowerTargeter targeterComponent) {
        return target.exists()
                && target.getComponent(LocationComponent.class)
                .getWorldPosition(new Vector3f())
                .distance(targeterPos) < targeterComponent.range;
    }

    /**
     * Gets a single targetable enemy within the tower's range
     * <p>
     * Attempts to use the entity that was targeted last round.
     * If that is not possible it picks an enemy in range based on the selection method listed
     *
     * @param targeterPos       The position of the targeter block
     * @param targeterComponent The targeter component on the targeter
     * @param enemyManager      The enemy manager to use if a new enemy needs to be picked
     * @return A suitable enemy in range, or the null entity if none was found
     */
    protected EntityRef getTarget(Vector3f targeterPos, SingleTargeterComponent targeterComponent, EnemyManager enemyManager) {
        EntityRef target = targeterComponent.lastTarget;

        if (!canUseTarget(target, targeterPos, targeterComponent)) {
            Set<EntityRef> enemiesInRange = enemyManager.getEnemiesInRange(
                    targeterPos,
                    targeterComponent.range);
            target = getSingleTarget(enemiesInRange, targeterComponent.selectionMethod);
        }
        return target;
    }
}
