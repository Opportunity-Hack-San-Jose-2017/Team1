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

package gov.sanjoseca.programs.walknroll.view.pages;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Ref;
import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.model.Actor;
import gov.sanjoseca.programs.walknroll.model.EducationalInstitution;
import gov.sanjoseca.programs.walknroll.model.WalkNRollEntry;
import gov.sanjoseca.programs.walknroll.service.Actors;
import gov.sanjoseca.programs.walknroll.service.EducationalInstitutions;
import gov.sanjoseca.programs.walknroll.service.WalkNRollReports;
import org.apache.commons.lang3.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Bean for managing the submit data page.
 */
@SessionScoped
@ManagedBean
public class SubmitDataBean implements Serializable {

    private static final String CLASS_NAME = SubmitDataBean.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final EducationalInstitutions INSTITUTIONS = new EducationalInstitutions();
    private static final Actors ACTORS = new Actors();
    private static final WalkNRollReports REPORTS = new WalkNRollReports();

    private List<EducationalInstitution> institutions;
    private EducationalInstitution selectedInstitution;
    private WalkNRollEntry entry = new WalkNRollEntry();

    private Date date;
    private String grade;
    private int numberOfStudents;

    public List<EducationalInstitution> getInstitutions() {
        final String METHOD_NAME = "getInstitutions";

        if (institutions == null) {
            UserService userService = UserServiceFactory.getUserService();
            if (userService.isUserAdmin()) {
                // Select all.
                institutions = INSTITUTIONS.getAllInstitutions(1, 1000, null).getItems();
            } else {
                // Get the current user's mail ID and then search for the schools that have authorized this id.
                Actor actor = ACTORS.getInstanceByName(userService.getCurrentUser().getEmail());
                if (actor != null) {
                    institutions = actor.getInstitutions().stream().map(Ref::get).collect(Collectors.toList());
                } else {
                    institutions = Collections.emptyList();
                }
            }
            if (institutions.size() == 1) {
                selectedInstitution = institutions.get(0);
            }
        }
        return institutions;
    }

    public boolean isSchoolSelectionEnabled() {
        return getInstitutions().size() > 1;
    }

    public EducationalInstitution getSelectedInstitution() {
        return selectedInstitution;
    }

    public void setSelectedInstitution(EducationalInstitution selectedInstitution) {
        this.selectedInstitution = selectedInstitution;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    /**
     * Get the report that is being worked on currently.
     */
    public WalkNRollEntry getEntry() {
        return entry;
    }

    public void createNewEntry(ActionEvent event) {
        this.entry = new WalkNRollEntry();
    }

    public void saveReport() {
        entry.setInstitution(selectedInstitution);
        entry.setDateTime(new ISO8601Date(date.getTime()));
        entry.setGrade(grade);
        // Change all numbers from their string form.
        for (Map.Entry e : entry.getModeDetails().entrySet()) {
            if (e.getValue() instanceof String) {
                String str = (String) e.getValue();
                // Convert it to a number
                if (StringUtils.isEmpty((String) e.getValue())) {
                    e.setValue(0);
                } else {
                    e.setValue(Integer.parseInt(str));
                }
            }
        }
        REPORTS.createEntry(entry);
        createNewEntry(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Operation successful", "New instance created successfully."));
    }

}
