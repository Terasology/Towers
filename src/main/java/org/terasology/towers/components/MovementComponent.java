// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import org.joml.Vector3f;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Stores information on the speed, and goal to move an entity towards.
 *
 * @see MovementSystem
 */
public class MovementComponent implements Component<MovementComponent> {
    public float speed;
    public Vector3f goal = new Vector3f();
    public float reachedDistance = 0.1f;

    @Override
    public void copyFrom(MovementComponent other) {
        this.speed = other.speed;
        this.goal.set(other.goal);
        this.reachedDistance = other.reachedDistance;
    }
}
