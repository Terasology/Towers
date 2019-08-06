/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.logic.ingame.towers;

import org.terasology.engine.modes.loadProcesses.AwaitedLocalCharacterSpawnEvent;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.AliveCharacterComponent;
import org.terasology.logic.characters.CharacterComponent;
import org.terasology.logic.health.BeforeDestroyEvent;
import org.terasology.logic.players.PlayerCharacterComponent;
import org.terasology.logic.players.event.OnPlayerRespawnedEvent;
import org.terasology.logic.players.event.OnPlayerSpawnedEvent;
import org.terasology.registry.In;
import org.terasology.registry.Share;

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
