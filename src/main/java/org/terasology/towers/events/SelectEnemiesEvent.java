// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.events;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;

import java.util.HashSet;
import java.util.Set;

/**
 * Event sent to select the enemies that will be attacked.
 * Sent against the targeter blocks in the tower.
 *
 */
public class SelectEnemiesEvent implements Event {
    private final Set<EntityRef> targets = new HashSet<>();

    /**
     * This method should only be used by the sending system after the event has been sent and processed
     *
     * @return The targets that have been selected by this event.
     */
    public Set<EntityRef> getTargets() {
        return targets;
    }

    /**
     * @param target The single enemy to add to this list
     */
    public void addToList(EntityRef target) {
        this.targets.add(target);
    }
}