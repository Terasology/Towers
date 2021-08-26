// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers;

import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.delay.DelayManager;
import org.terasology.engine.logic.delay.PeriodicActionTriggeredEvent;
import org.terasology.engine.registry.In;
import org.terasology.towers.components.TowerComponent;
import org.terasology.towers.components.TowerEffector;
import org.terasology.towers.components.TowerTargeter;
import org.terasology.towers.events.ApplyEffectEvent;
import org.terasology.towers.events.RemoveEffectEvent;
import org.terasology.towers.events.SelectEnemiesEvent;
import org.terasology.towers.events.TowerCreatedEvent;
import org.terasology.towers.events.TowerDestroyedEvent;

import java.util.HashSet;
import java.util.Set;

@RegisterSystem
public class TowerManager extends BaseComponentSystem {
    private static final String EVENT_ID = "towerAttack";

    private final Set<EntityRef> towerEntities = new HashSet<>();
    @In
    private DelayManager delayManager;
    @In
    private EntityManager entityManager;

    /**
     * Remove all scheduled delays before the game is shutdown.
     */
    @Override
    public void shutdown() {
        for (EntityRef tower : towerEntities) {
            TowerComponent towerComponent = tower.getComponent(TowerComponent.class);
            for (EntityRef targeter : towerComponent.targeter) {
                delayManager.cancelPeriodicAction(tower, buildEventId(targeter));
            }
            tower.destroy();
        }
    }


    /**
     * Called when a tower is created.
     * Adds the tower to the list and sets the periodic actions for it's attacks
     * <p>
     * Filters on {@link TowerComponent}
     *
     * @see TowerCreatedEvent
     */
    @ReceiveEvent
    public void onTowerCreated(TowerCreatedEvent event, EntityRef towerEntity, TowerComponent towerComponent) {
        towerEntities.add(towerEntity);
        for (EntityRef targeter : towerComponent.targeter) {
            TowerTargeter targeterComponent = DefenceField.getComponentExtending(targeter, TowerTargeter.class);
            delayManager.addPeriodicAction(towerEntity,
                    buildEventId(targeter),
                    targeterComponent.attackSpeed,
                    targeterComponent.attackSpeed);
        }
    }

    /**
     * Called when a tower is destroyed.
     * Removes all the periodic actions and the tower from the store.
     * <p>
     * Filters on {@link TowerComponent}
     */
    @ReceiveEvent
    public void onTowerDestroyed(TowerDestroyedEvent event, EntityRef towerEntity, TowerComponent towerComponent) {
        for (EntityRef targeter : towerComponent.targeter) {
            handleTargeterRemoval(towerEntity, targeter);
        }
        towerEntities.remove(towerEntity);
    }

    /**
     * Called every attack cycle per targeter.
     * Checks if the tower can fire, and if so, fires that targeter.
     * <p>
     * Filters on {@link TowerComponent}
     *
     * @see PeriodicActionTriggeredEvent
     */
    @ReceiveEvent
    public void onPeriodicActionTriggered(PeriodicActionTriggeredEvent event, EntityRef entity, TowerComponent component) {
        if (isEventIdCorrect(event.getActionId())) {
            EntityRef targeter = getTargeterId(event.getActionId());
            handleTowerShooting(component, targeter);
        }
    }

    /**
     * Handles the removal of a targeter from a tower.
     * Does this by calling the tower to end the effects on the enemies where appropriate.
     *
     * @param tower    The main tower entity to remove the targeter from.
     * @param targeter The targeter to remove
     */
    private void handleTargeterRemoval(EntityRef tower, EntityRef targeter) {

        delayManager.cancelPeriodicAction(tower, buildEventId(targeter));

        TowerComponent towerComponent = tower.getComponent(TowerComponent.class);
        TowerTargeter<?> targeterComponent = DefenceField.getComponentExtending(targeter, TowerTargeter.class);
        for (EntityRef enemy : targeterComponent.affectedEnemies) {
            endEffects(towerComponent.effector, enemy, targeterComponent.getMultiplier());
        }
    }

    /**
     * Handles the steps involved in making a targeter shoot.
     *
     * @param towerComponent The TowerComponent of the tower entity shooting.
     * @param targeter       The targeter that's shooting
     */
    private void handleTowerShooting(TowerComponent towerComponent, EntityRef targeter) {
        Set<EntityRef> currentTargets = getTargetedEnemies(targeter);
        TowerTargeter towerTargeter = DefenceField.getComponentExtending(targeter, TowerTargeter.class);

        applyEffectsToTargets(towerComponent.effector, currentTargets, towerTargeter);

        towerTargeter.affectedEnemies = currentTargets;
    }

    /**
     * Calls on the targeter to obtain the enemies it's targeting.
     *
     * @param targeter The targeter to call on
     * @return All entities targeted by that targeter.
     * @see TowerTargeter
     */
    private Set<EntityRef> getTargetedEnemies(EntityRef targeter) {
        SelectEnemiesEvent shootEvent = new SelectEnemiesEvent();
        targeter.send(shootEvent);
        return shootEvent.getTargets();
    }

    /**
     * Applies all the effects on a tower to the targeted enemies
     *
     * @param effectors      The effectors on the tower
     * @param currentTargets The current targets of the tower
     * @param towerTargeter  The targeter shooting
     */
    private void applyEffectsToTargets(Set<EntityRef> effectors, Set<EntityRef> currentTargets, TowerTargeter towerTargeter) {
        Set<EntityRef> exTargets = Sets.difference(towerTargeter.affectedEnemies, currentTargets);

        /* Apply effects to targeted enemies */
        currentTargets.forEach(target -> applyEffects(effectors,
                target,
                towerTargeter.getMultiplier(),
                !towerTargeter.affectedEnemies.contains(target)));

        /* Process all the enemies that are no longer targeted */
        for (EntityRef exTarget : exTargets) {
            endEffects(effectors, exTarget, towerTargeter.getMultiplier());
        }
    }

    /**
     * Applies all the effects on a tower to an enemy.
     *
     * @param effectors   The effectors to use to apply the effects
     * @param target      The target enemy
     * @param multiplier  The multiplier from the targeter
     * @param isTargetNew Indicates if the enemy is newly targeted. Used to filter effectors
     */
    private void applyEffects(Set<EntityRef> effectors, EntityRef target, float multiplier, boolean isTargetNew) {
        ApplyEffectEvent event = new ApplyEffectEvent(target, multiplier);

        for (EntityRef effector : effectors) {
            TowerEffector effectorComponent = DefenceField.getComponentExtending(effector, TowerEffector.class);
            switch (effectorComponent.getEffectCount()) {
                case CONTINUOUS:
                    if (isTargetNew) {
                        effector.send(event);
                    }
                    break;
                case PER_SHOT:
                    effector.send(event);
                    break;
                default:
                    throw new EnumConstantNotPresentException(EffectCount.class, effectorComponent.getEffectCount().toString());
            }
        }
    }

    /**
     * Calls on each effector to end the effect on a target, where applicable.
     *
     * @param effectors  The effectors to check through
     * @param oldTarget  The target to remove the effects from
     * @param multiplier The effect multiplier to apply to the event
     */
    private void endEffects(Set<EntityRef> effectors, EntityRef oldTarget, float multiplier) {
        RemoveEffectEvent event = new RemoveEffectEvent(oldTarget, multiplier);
        for (EntityRef effector : effectors) {
            TowerEffector effectorComponent = DefenceField.getComponentExtending(effector, TowerEffector.class);
            switch (effectorComponent.getEffectDuration()) {
                case LASTING:
                    effector.send(event);
                    break;
                case INSTANT:
                case PERMANENT:
                    break;
                default:
                    throw new EnumConstantNotPresentException(EffectDuration.class, effectorComponent.getEffectCount().toString());
            }
        }
    }

    /**
     * Checks that the periodic event is intended for the given tower.
     *
     * @param eventId The id of the periodic event
     * @return True if the event belongs to the tower
     * @see PeriodicActionTriggeredEvent
     */
    private boolean isEventIdCorrect(String eventId) {
        return eventId.startsWith(EVENT_ID);
    }

    /**
     * Gets the targeter entity from the periodic event id.
     *
     * @param eventId The id of the periodic event
     * @return The targeter entity encoded in the event.
     * @see PeriodicActionTriggeredEvent
     */
    private EntityRef getTargeterId(String eventId) {
        String id = eventId.substring(eventId.indexOf('|') + 1);
        return entityManager.getEntity(Long.parseLong(id));
    }

    /**
     * Creates the periodic event id for the targeter on a tower
     *
     * @param targeter The targeter the event is sending for
     * @return The id for that periodic action event.
     * @see PeriodicActionTriggeredEvent
     */
    private String buildEventId(EntityRef targeter) {
        return EVENT_ID + "|" + targeter.getId();
    }
}
