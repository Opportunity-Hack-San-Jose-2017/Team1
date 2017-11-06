/*
 * Copyright 2017, Harsha R.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.sanjoseca.programs.walknroll.model;

/**
 * Enumeration of the various types of transportation modes being considered by this program.
 */
public enum TransportationMode {

    /**
     * Walking.
     */
    WALK("Walk"),

    /**
     * Bicycle.
     */
    BIKE("Bike"),

    /**
     * School bus.
     */
    SCHOOL_BUS("School bus"),

    /**
     * All children belonging to the same family.
     */
    FAMILY_VEHICLE("Family vehicle"),

    /**
     * Car pool includes riding with children from other families.
     */
    CAR_POOL("Car pool"),

    /**
     * City bus, subway, etc.
     */
    PUBLIC_TRANSIT("Public transit"),

    /**
     * Other modes of transportation like skateboards, scooters, etc.
     */
    OTHERS("Others");

    private String message;

    TransportationMode(String message) {
        this.message = message;
    }

    /**
     * Get the display friendly enum message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * EL friendly way of getting the name of the enum.
     */
    public String getName() {
        return name();
    }

    @Override
    public String toString() {
        return message;
    }
}
