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
package org.terasology.rendering.nui.towers;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.ingame.towers.ActivateTowerRequest;
import org.terasology.logic.ingame.towers.TowerComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;
import org.terasology.rendering.nui.BaseInteractionScreen;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UICheckbox;

public class TowerSettingsScreen extends BaseInteractionScreen {
    private UICheckbox isActivated;
    private UIButton cancelButton;
    private UIButton saveButton;

    private EntityRef towerRoot;
    private TowerComponent towerComponent;
    private LocationComponent locationComponent;

    @In
    private NUIManager nuiManager;
    @In
    private LocalPlayer localPlayer;

    @Override
    public void initialise() {
        isActivated = find("isActivated", UICheckbox.class);
        cancelButton = find("cancelButton", UIButton.class);
        saveButton = find("saveButton", UIButton.class);
        if (saveButton != null) {
            saveButton.subscribe(this::onSaveButton);
        }

        if (cancelButton != null) {
            cancelButton.subscribe(this::onCancelButton);
        }
    }

    @Override
    protected void initializeWithInteractionTarget(EntityRef interactionTarget) {
        towerRoot = interactionTarget;
        towerComponent = interactionTarget.getComponent(TowerComponent.class);
        locationComponent = interactionTarget.getComponent(LocationComponent.class);
        isActivated.setChecked(towerComponent.isActivated);
    }

    private void onSaveButton(UIWidget button) {
        localPlayer.getClientEntity().send(new ActivateTowerRequest(towerRoot, isActivated.isChecked()));
        getManager().popScreen();
    }

    private void onCancelButton(UIWidget button) {
        getManager().popScreen();
    }
}
