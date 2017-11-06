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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.objectify.ISO8601DateTranslatorFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an entry in the tracking DB.
 */
@Cache
@Entity
public class WalkNRollEntry extends AbstractPersistentObject<WalkNRollEntry> {

    @Index
    private Ref<EducationalInstitution> institution;
    @Index
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date dateTime;
    private String grade;
    private Map<TransportationMode, Integer> modeDetails = new HashMap<>();

    // Weather information -- added during report generation time.
    @Ignore
    private String weather;

    // Basic audit fields
    private String createdBy;
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date createdAt;
    private String modifiedBy;
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date modifiedAt;

    public WalkNRollEntry() {
        for (TransportationMode mode : TransportationMode.values()) {
            modeDetails.putIfAbsent(mode, 0);
        }
    }

    /**
     * Get the educational institution associated with this record.
     *
     * @return the educational institution associated with this record.
     */
    @JsonIgnore
    public EducationalInstitution getInstitution() {
        return institution.get();
    }

    /**
     * Set the educational institution associated with this record.
     *
     * @param institution the educational institution associated with this record.
     */
    public void setInstitution(EducationalInstitution institution) {
        this.institution = Ref.create(institution);
    }

    /**
     * Get the date-time reference for this record.
     *
     * @return the date-time reference for this record.
     */
    public ISO8601Date getDateTime() {
        return dateTime;
    }

    /**
     * Set the date-time reference for this record.
     *
     * @param dateTime the date-time reference for this record.
     */
    public void setDateTime(ISO8601Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get the grade of the student(s) being represented in this record.
     *
     * @return the grade of the student.
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Set the grade of the student(s) being represented in this record.
     *
     * @param grade the grade of the student.
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * Get the details of the transportation modes used for this report.
     *
     * @return the details of the transportation modes used for this report.
     */
    public Map<TransportationMode, Integer> getModeDetails() {
        return modeDetails;
    }

    /**
     * Set the details of the transportation modes used for this report.
     *
     * @param modeDetails the details of the transportation modes used for this report.
     */
    public void setModeDetails(Map<TransportationMode, Integer> modeDetails) {
        this.modeDetails = modeDetails;
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

    /**
     * Get the weather reported on this day.
     *
     * @return the weather reported on this day.
     */
    public String getWeather() {
        return weather;
    }

    /**
     * Set the weather reported on this day.
     *
     * @param weather the weather reported on this day.
     */
    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public void merge(WalkNRollEntry entity) {
        if (entity.institution != null) {
            this.institution = entity.institution;
        }
        if (entity.dateTime != null) {
            this.dateTime = entity.dateTime;
        }
        if (StringUtils.isNotEmpty(entity.grade)) {
            this.grade = entity.grade;
        }
        if (entity.modeDetails != null) {
            this.modeDetails = entity.modeDetails;
        }
    }

    @OnSave
    public void updateAuditFields() {
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
}
