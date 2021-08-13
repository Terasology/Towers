// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers.components;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.Component;

/**
 * Stores information on the speed, and goal to move an entity towards.
 *
 * @see MovementSystem
 */
public class MovementComponent implements Component {
    public float speed;
    public Vector3f goal = new Vector3f();
    public float reachedDistance = 0.1f;


}
