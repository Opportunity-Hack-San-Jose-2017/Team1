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

package gov.sanjoseca.programs.walknroll.internal.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import gov.sanjoseca.programs.walknroll.model.AbstractPersistentObject;

import java.util.Objects;

/**
 * Represent a key-value pair in the database.
 */
@JsonSerialize
@JsonDeserialize
@Entity
public class ConfigData extends AbstractPersistentObject<ConfigData> {

    @Index
    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;

    public ConfigData() {
    }

    public ConfigData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void merge(ConfigData entity) {
        if (!Objects.equals(this.key, entity.key)) {
            throw new IllegalArgumentException("Cannot merge data belonging to different keys.");
        }
        if (entity.value != null) {
            this.value = entity.value;
        }
    }
}
