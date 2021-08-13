// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.health.events;

import org.terasology.engine.entitySystem.event.Event;

/**
 * Event sent when an entity reaches zero health.
 * Sent against the dead entity.
 *
 * @see DamageEntityEvent
 */
public class EntityDeathEvent implements Event {
}
