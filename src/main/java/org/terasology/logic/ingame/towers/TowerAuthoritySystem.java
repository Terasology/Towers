// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.logic.ingame.towers;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.terasology.alterationEffects.damageOverTime.DamageOverTimeAlterationEffect;
import org.terasology.engine.context.Context;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnActivatedComponent;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.delay.DelayManager;
import org.terasology.engine.logic.delay.PeriodicActionTriggeredEvent;
import org.terasology.engine.logic.location.Location;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.engine.world.block.items.BlockItemComponent;
import org.terasology.engine.world.block.items.OnBlockItemPlaced;
import org.terasology.engine.world.block.items.OnBlockToItem;
import org.terasology.gestalt.assets.management.AssetManager;

import java.util.Set;

@RegisterSystem(RegisterMode.AUTHORITY)
public class TowerAuthoritySystem extends BaseComponentSystem {
    @In
    private AssetManager assetManager;
    @In
    private EntityManager entityManager;
    @In
    private DelayManager delayManager;
    @In
    private PlayerManager playerManager;
    @In
    private Context context;

    @ReceiveEvent
    public void onSettingsChanged(ActivateTowerRequest event, EntityRef player) {
        EntityRef towerEntity = event.towerEntity;
        TowerComponent towerComponent = towerEntity.getComponent(TowerComponent.class);
        towerComponent.isActivated = event.isActivated;
        if (event.isActivated) {
            if (towerComponent.childEntity.equals(EntityRef.NULL)) {
                activateTower(towerEntity, towerComponent);
            }
        } else {
            deactivateTower(towerEntity, towerComponent);
        }
        towerEntity.saveComponent(towerComponent);
    }

    @ReceiveEvent(components = {BlockItemComponent.class})
    public void onItemToBlock(OnBlockItemPlaced event, EntityRef itemEntity, TowerComponent towerComponent) {
        EntityRef towerEntity = event.getPlacedBlock();
        if (towerComponent.isActivated && towerComponent.childEntity.equals(EntityRef.NULL)) {
            activateTower(towerEntity, towerComponent);
        }
        towerEntity.addOrSaveComponent(towerComponent);
    }

    @ReceiveEvent
    public void onBlockToItem(OnBlockToItem event, EntityRef blockEntity, TowerComponent towerComponent) {
        deactivateTower(blockEntity, towerComponent);
        event.getItem().addOrSaveComponent(towerComponent);
    }

    @ReceiveEvent(priority = EventPriority.PRIORITY_HIGH, components = {TowerComponent.class, BlockComponent.class})
    public void onTowerActivated(OnActivatedComponent event, EntityRef towerEntity, TowerComponent towerComponent) {
        if (towerComponent.isActivated && towerComponent.childEntity.equals(EntityRef.NULL)) {
            activateTower(towerEntity, towerComponent);
        }
    }

    @ReceiveEvent
    public void onPeriodicActionTriggered(PeriodicActionTriggeredEvent event, EntityRef towerEntity,
                                          TowerComponent towerComponent, LocationComponent locationComponent) {
        if (getActionId(towerEntity.getId()).equals(event.getActionId())) {
            damageEnemiesInRange(towerEntity, locationComponent, towerComponent, playerManager.getAliveCharacters());
        }
    }

    private void activateTower(EntityRef towerEntity, TowerComponent towerComponent) {
        Prefab rookPrefab = assetManager.getAsset("Towers:testRook", Prefab.class).get();
        EntityBuilder rookEntityBuilder = entityManager.newBuilder(rookPrefab);
        rookEntityBuilder.setOwner(towerEntity);
        rookEntityBuilder.setPersistent(false);
        EntityRef rook = rookEntityBuilder.build();
        towerComponent.childEntity = rook;
        Location.attachChild(towerEntity, rook, new Vector3f(0, 1, 0), new Quaternionf().identity());

        if (!delayManager.hasPeriodicAction(towerEntity, getActionId(towerEntity.getId()))) {
            delayManager.addPeriodicAction(towerEntity, getActionId(towerEntity.getId()),1000,1000);
        }
    }

    private void deactivateTower(EntityRef towerEntity, TowerComponent towerComponent) {
        towerComponent.childEntity.destroy();
        towerComponent.childEntity = EntityRef.NULL;

        if (delayManager.hasPeriodicAction(towerEntity, getActionId(towerEntity.getId()))) {
            delayManager.cancelPeriodicAction(towerEntity, getActionId(towerEntity.getId()));
        }
    }

    private String getActionId(long id) {
        return "targetEnemies_" + id;
    }

    private void damageEnemiesInRange(EntityRef towerEntity, LocationComponent locationComponent, TowerComponent towerComponent,
                                      Set<EntityRef> aliveCharacters) {
        float rangeSqrd = towerComponent.range * towerComponent.range;
        Vector3f towerLocation = locationComponent.getWorldPosition(new Vector3f());
        for (EntityRef aliveCharacter: aliveCharacters) {
            Vector3f playerLocation = aliveCharacter.getComponent(LocationComponent.class).getWorldPosition(new Vector3f());
            if (playerLocation.distanceSquared(towerLocation) <= rangeSqrd) {
                DamageOverTimeAlterationEffect dotEffect = new DamageOverTimeAlterationEffect(context);
                dotEffect.applyEffect(towerEntity, aliveCharacter, 10, 1000);
            }
        }
    }
}
