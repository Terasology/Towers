// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.logic.ingame.towers;

import org.terasology.engine.core.modes.loadProcesses.AwaitedLocalCharacterSpawnEvent;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.AliveCharacterComponent;
import org.terasology.engine.logic.characters.CharacterComponent;
import org.terasology.engine.logic.health.BeforeDestroyEvent;
import org.terasology.engine.logic.players.PlayerCharacterComponent;
import org.terasology.engine.logic.players.event.OnPlayerRespawnedEvent;
import org.terasology.engine.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;

import java.util.HashSet;
import java.util.Set;

@Share(value = PlayerManager.class)
@RegisterSystem(RegisterMode.AUTHORITY)
public class PlayerManager extends BaseComponentSystem {
    @In
    private EntityManager entityManager;

    private Set<EntityRef> aliveCharacters = new HashSet<>();

    @Override
    public void initialise() {
        if (entityManager.getCountOfEntitiesWith(AliveCharacterComponent.class) != 0) {
            for (EntityRef player: entityManager.getEntitiesWith(AliveCharacterComponent.class)) {
                aliveCharacters.add(player);
            }
        }
    }

    @ReceiveEvent //Spawn, initial spawn on joining a server
    public void onPlayerSpawnEvent(OnPlayerSpawnedEvent event, EntityRef player) {
            aliveCharacters.add(player);
    }

    @ReceiveEvent //Spawn, initial spawn on joining a server
    public void onPlayerReSpawnEvent(OnPlayerRespawnedEvent event, EntityRef player) {
        aliveCharacters.add(player);
    }

    @ReceiveEvent
    public void onAwaitedLocalCharacterSpawnEvent(AwaitedLocalCharacterSpawnEvent event, EntityRef player) {
        if (entityManager.getCountOfEntitiesWith(AliveCharacterComponent.class) != 0) {
            for (EntityRef aliveCharacter: entityManager.getEntitiesWith(AliveCharacterComponent.class)) {
                aliveCharacters.add(aliveCharacter);
            }
        }
    }


    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH, components = {CharacterComponent.class,
            AliveCharacterComponent.class, PlayerCharacterComponent.class})
    public void beforeDestroy(BeforeDestroyEvent event, EntityRef player) {
        aliveCharacters.remove(player);
    }

    public Set<EntityRef> getAliveCharacters() {
        return aliveCharacters;
    }
}
