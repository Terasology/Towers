// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.events.PlayerDeathEvent;
import org.terasology.engine.logic.location.Location;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.towers.components.ChildrenParticleComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles misc rendering duties for the module.
 * These involve rendering stuff in world for systems that provide functionality.
 * <p>
 * Also handles application and removal of particle effects and shot entities.
 */
@RegisterSystem
@Share(InWorldRenderer.class)
public class InWorldRenderer extends BaseComponentSystem {
    @In
    private EntityManager entityManager;
    private final Map<EntityRef, EntityRef> bullets = new HashMap<>();


    /**
     * Called when the entity is having the particle component removed.
     * Removes any latent particle entities.
     * <p>
     * Filters on {@link ChildrenParticleComponent}
     *
     * @see BeforeDeactivateComponent
     */
    @ReceiveEvent(components = ChildrenParticleComponent.class)
    public void onBeforeDeactivateComponent(BeforeDeactivateComponent event, EntityRef entity, ChildrenParticleComponent childrenParticleComponent) {
        if (!childrenParticleComponent.particleEntities.isEmpty()) {
            removeAllParticleEffects(entity);
        }
    }

    /**
     * Adds a child particle effect to the entity
     *
     * @param target         The entity to add the effect to
     * @param particlePrefab The name of the particle prefab to add
     */
    public void addParticleEffect(EntityRef target, String particlePrefab) {
        if (target.exists()) {
            ChildrenParticleComponent particleComponent = getParticleComponent(target);
            Map<String, EntityRef> particleMap = particleComponent.particleEntities;
            if (!particleMap.containsKey(particlePrefab)) {
                EntityRef particleEntity = entityManager.create(particlePrefab);
                particleMap.put(particlePrefab, particleEntity);

                LocationComponent targetLoc = target.getComponent(LocationComponent.class);
                LocationComponent childLoc = particleEntity.getComponent(LocationComponent.class);
                childLoc.setWorldPosition(targetLoc.getWorldPosition(new Vector3f()));
                Location.attachChild(target, particleEntity);
                particleEntity.setOwner(target);

                target.addOrSaveComponent(particleComponent);
            }
        }
    }

    /**
     * Removes a particle child from the target.
     *
     * @param target         The entity to remove the child from
     * @param particlePrefab The name of the prefab to remove.
     */
    public void removeParticleEffect(EntityRef target, String particlePrefab) {
        Map<String, EntityRef> particleMap = getParticleComponent(target).particleEntities;
        EntityRef child = particleMap.remove(particlePrefab);
        if (child != null) {
            child.destroy();
        }
        if (particleMap.isEmpty()) {
            target.removeComponent(ChildrenParticleComponent.class);
        }
    }

    /**
     * Removes all particle effects from the target.
     * Handles the case where the target has no particle effects
     *
     * @param target The entity to remove the effects from.
     */
    public void removeAllParticleEffects(EntityRef target) {
        if (target.hasComponent(ChildrenParticleComponent.class)) {
            ChildrenParticleComponent component = target.getComponent(ChildrenParticleComponent.class);
            component.particleEntities.values().forEach(EntityRef::destroy);
            component.particleEntities.clear();
            target.removeComponent(component.getClass());
        }
    }

    /**
     * Checks if the target has a child entity of the given prefab
     *
     * @param target         The entity to check for children on
     * @param particlePrefab The name of the prefab to check for
     * @return True if the entity has a child with that prefab.
     */
    public boolean hasParticleEffect(EntityRef target, String particlePrefab) {
        Map<String, EntityRef> particleMap = getParticleComponent(target).particleEntities;
        return particleMap.containsKey(particlePrefab);
    }

    /**
     * Called when an entity dies
     * Handles destroying any child particle components it may have had.
     * <p>
     * Filters on {@link ChildrenParticleComponent}
     * Sent against the dying entity
     *
     */
    @ReceiveEvent
    public void onPlayerDeath(PlayerDeathEvent event, EntityRef entity, ChildrenParticleComponent component) {
        component.particleEntities.values().forEach(EntityRef::destroy);
    }

    private ChildrenParticleComponent getParticleComponent(EntityRef target) {
        ChildrenParticleComponent particleComponent;
        if (target.hasComponent(ChildrenParticleComponent.class)) {
            particleComponent = target.getComponent(ChildrenParticleComponent.class);
        } else {
            particleComponent = new ChildrenParticleComponent();
        }
        return particleComponent;
    }
}
