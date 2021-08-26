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

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.gestalt.entitysystem.component.Component;

@ForceBlockActive
public class TowerComponent implements Component<TowerComponent> {
    @Replicate
    public boolean isActivated = false;
    @Replicate
    public int range = 10;
    @Replicate
    public EntityRef childEntity = EntityRef.NULL;

    @Override
    public void copyFrom(TowerComponent other) {
        this.isActivated = other.isActivated;
        this.range = other.range;
        this.childEntity = other.childEntity;
    }
}
