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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.annotation.*;
import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.objectify.ISO8601DateTranslatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents information about an educational institution (e.g., school).
 */
@Entity
@Cache
public class EducationalInstitution extends AbstractPersistentObject<EducationalInstitution> {

    @Index
    private String name;
    private Address address;
    private List<String> authorizedUsers = new ArrayList<>();

    // Basic audit fields
    private String createdBy;
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date createdAt;
    private String modifiedBy;
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date modifiedAt;


    /**
     * Get the name of this educational institution.
     *
     * @return the name of this educational institution.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this educational institution.
     *
     * @param name the name of this educational institution.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the address of this educational institution.
     *
     * @return the address of this educational institution.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address of this educational institution.
     *
     * @param address the address of this educational institution.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Get the users authorized to provide data for this institution.
     *
     * @return the users authorized to provide data for this institution.
     */
    public List<String> getAuthorizedUsers() {
        return authorizedUsers;
    }

    /**
     * the users authorized to provide data for this institution.
     *
     * @param authorizedUsers the users authorized to provide data for this institution.
     */
    public void setAuthorizedUsers(List<String> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    /**
     * Get the details of the user who created this entry.
     *
     * @return the details of the user who created this entry.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Get the details of when this entry was created.
     *
     * @return the details of when this entry was created.
     */
    public ISO8601Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the details of the user who modified this entry last.
     *
     * @return the details of the user who modified this entry last.
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Get the details of when this entry was last modified.
     *
     * @return the details of when this entry was last modified.
     */
    public ISO8601Date getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public void merge(EducationalInstitution entity) {
        this.name = entity.name;
        if (entity.authorizedUsers != null) {
            this.authorizedUsers = entity.authorizedUsers;
        }
        if (entity.address != null) {
            this.address = entity.address;
        }
    }

    @OnSave
    public void updateDataFields() {

        this.authorizedUsers = this.authorizedUsers.stream().distinct().collect(Collectors.toList());
        // Audit related.
        User user = UserServiceFactory.getUserService().getCurrentUser();
        ISO8601Date now = new ISO8601Date();
        if (getId() == null) {
            // Set the created references.
            createdBy = user.getEmail();
            createdAt = now;
        } else {
            // Set the modified by references.
            modifiedBy = user.getEmail();
            modifiedAt = now;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EducationalInstitution)) return false;

        EducationalInstitution that = (EducationalInstitution) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return authorizedUsers != null ? authorizedUsers.equals(that.authorizedUsers) : that.authorizedUsers == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (authorizedUsers != null ? authorizedUsers.hashCode() : 0);
        return result;
    }
}
