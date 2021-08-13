// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.towers;


/**
 * Options for how a targeter should select an enemy
 * <p>
 * This does not apply to all targeters, only ones that select a single enemy at some point
 *
 */
public enum SelectionMethod {
    /**
     * Select the enemy closest to the shrine.
     */
    FIRST {
        @Override
        public String toString() {
            return "First";
        }
    },
    /**
     * Selects the enemy with the least health.
     */
    WEAK {
        @Override
        public String toString() {
            return "Weakest";
        }
    },
    /**
     * Selects the enemy with the most health.
     */
    STRONG {
        @Override
        public String toString() {
            return "Strongest";
        }
    },
    /**
     * Selects a random valid enemy.
     */
    RANDOM {
        @Override
        public String toString() {
            return "Random";
        }
    }
}
