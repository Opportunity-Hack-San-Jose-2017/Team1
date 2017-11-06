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
 * A <code>PersistentObject</code> represents data that can be saved to the backend.
 *
 * @param <K> the type used for determining the ID of this attribute.
 */
public interface PersistentObject<E extends PersistentObject<E, K>, K> {

    /**
     * Get an unique identifier for this persistent object.
     *
     * @return an unique identifier for this persistent object.
     */
    K getId();

    /**
     * Merge data from another persistent object in to this object.
     *
     * @param entity the entity from which data needs to be copied over.
     */
    void merge(E entity);
}
