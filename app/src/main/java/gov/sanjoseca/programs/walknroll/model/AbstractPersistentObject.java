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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * Base class for all persistent objects.
 */
@JsonSerialize
@ApiModel
public abstract class AbstractPersistentObject<E extends PersistentObject<E, Long>>
        implements PersistentObject<E, Long>, Serializable {

    @Load
    @Id
    protected Long id;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the unique identifier for this persistent object.
     *
     * @param id the id of this persistent object.
     */
    public void setId(Long id) {
        this.id = id;
    }

}
