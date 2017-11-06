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

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an actor within the system. This could be a Student or Teacher.
 */
@Entity
@Cache
public class Actor extends AbstractPersistentObject<Actor> {

    private Type type;
    @Index
    private String name;
    private Set<Ref<EducationalInstitution>> institutions = new HashSet<>();

    /**
     * Get the name associated with this actor.
     *
     * @return the name associated with this actor.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name associated with this actor.
     *
     * @param name the name associated with this actor.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the type of this actor.
     *
     * @return the type of this actor.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the type of this actor.
     *
     * @param type the type of this actor.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Get the collection of institutions that this actor is a member of.
     *
     * @return collection of institutions.
     */
    public Set<Ref<EducationalInstitution>> getInstitutions() {
        return institutions;
    }

    /**
     * Set the collection of institutions that this actor is a member of.
     *
     * @param institutions collection of institutions.
     */
    public void setInstitutions(Set<Ref<EducationalInstitution>> institutions) {
        this.institutions = institutions;
    }

    @Override
    public void merge(Actor entity) {
        if (entity.institutions != null) {
            if (this.institutions == null) {
                this.institutions = new HashSet<>();
            }
            this.institutions.addAll(entity.institutions);
        }
    }

    /**
     * Identifies the type of an actor within the system.
     */
    public enum Type {
        /**
         * Type for identifying a student.
         */
        STUDENT,

        /**
         * Type for identifying an instructor.
         */
        INSTRUCTOR
    }

    //
    //  Equals & hashcode.
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;

        Actor actor = (Actor) o;

        if (type != actor.type) return false;
        return name != null ? name.equals(actor.name) : actor.name == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
