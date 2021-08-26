// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.logic.ingame.towers;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.Event;
import org.terasology.engine.network.ServerEvent;

@ServerEvent
public class ActivateTowerRequest implements Event {
    public EntityRef towerEntity;
    public boolean isActivated;

    public ActivateTowerRequest() {
    }

    public ActivateTowerRequest(EntityRef towerEntity, boolean isActivated) {
        this.towerEntity = towerEntity;
        this.isActivated = isActivated;
    }
}
