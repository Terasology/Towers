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

import com.google.common.collect.Lists;
import org.terasology.assets.management.AssetManager;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.Location;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Quat4f;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.items.BlockItemComponent;
import org.terasology.world.block.items.OnBlockItemPlaced;
import org.terasology.world.block.items.OnBlockToItem;

@RegisterSystem(RegisterMode.AUTHORITY)
public class TowerAuthoritySystem extends BaseComponentSystem {
    @In
    private AssetManager assetManager;
    @In
    private EntityManager entityManager;

    @ReceiveEvent
    public void onSettingsChanged(SendTowerActivationRequest event, EntityRef player) {
        EntityRef towerEntity = event.towerEntity;
        TowerComponent towerComponent = towerEntity.getComponent(TowerComponent.class);
        towerComponent.isActivated = event.isActivated;
        if (event.isActivated) {
            if (towerComponent.childEntity.equals(EntityRef.NULL)) {
                Prefab rookPrefab = assetManager.getAsset("Towers:testRook", Prefab.class).get();
                EntityBuilder rookEntityBuilder = entityManager.newBuilder(rookPrefab);
                rookEntityBuilder.setOwner(towerEntity);
                rookEntityBuilder.setPersistent(false);
                EntityRef rook = rookEntityBuilder.build();
                towerComponent.childEntity = rook;
                Location.attachChild(towerEntity, rook, new Vector3f(0, 1, 0), new Quat4f(Quat4f.IDENTITY));
            }
        } else {
            towerComponent.childEntity.destroy();
            towerComponent.childEntity = EntityRef.NULL;
        }
        towerEntity.saveComponent(towerComponent);
    }

    @ReceiveEvent(components = {BlockItemComponent.class})
    public void onItemToBlock(OnBlockItemPlaced event, EntityRef itemEntity,
                              TowerComponent towerComponent) {
        EntityRef towerEntity = event.getPlacedBlock();
        if (towerComponent.isActivated && towerComponent.childEntity.equals(EntityRef.NULL)) {
            Prefab rookPrefab = assetManager.getAsset("Towers:testRook", Prefab.class).get();
            EntityBuilder rookEntityBuilder = entityManager.newBuilder(rookPrefab);
            rookEntityBuilder.setOwner(towerEntity);
            rookEntityBuilder.setPersistent(false);
            EntityRef rook = rookEntityBuilder.build();
            towerComponent.childEntity = rook;
            Location.attachChild(towerEntity, rook, new Vector3f(0, 1, 0), new Quat4f(Quat4f.IDENTITY));
        }
        towerEntity.addOrSaveComponent(towerComponent);
    }

    @ReceiveEvent
    public void onBlockToItem(OnBlockToItem event, EntityRef blockEntity, TowerComponent towerComponent) {
        towerComponent.childEntity.destroy();
        towerComponent.childEntity = EntityRef.NULL;
        event.getItem().addComponent(towerComponent);
    }

    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH, components = {TowerComponent.class, BlockComponent.class})
    public void onTowerActivated(OnActivatedComponent event, EntityRef towerEntity,
                                 TowerComponent towerComponent) {
        if (towerComponent.isActivated && towerComponent.childEntity.equals(EntityRef.NULL)) {
            Prefab rookPrefab = assetManager.getAsset("Towers:testRook", Prefab.class).get();
            EntityBuilder rookEntityBuilder = entityManager.newBuilder(rookPrefab);
            rookEntityBuilder.setOwner(towerEntity);
            rookEntityBuilder.setPersistent(false);
            EntityRef rook = rookEntityBuilder.build();
            towerComponent.childEntity = rook;
            Location.attachChild(towerEntity, rook, new Vector3f(0, 1, 0), new Quat4f(Quat4f.IDENTITY));
        }
    }
}