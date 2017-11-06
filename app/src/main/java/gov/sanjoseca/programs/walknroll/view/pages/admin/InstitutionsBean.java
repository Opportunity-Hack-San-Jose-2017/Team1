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

package gov.sanjoseca.programs.walknroll.view.pages.admin;

import gov.sanjoseca.programs.walknroll.model.Address;
import gov.sanjoseca.programs.walknroll.model.EducationalInstitution;
import gov.sanjoseca.programs.walknroll.service.EducationalInstitutions;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Managed bean for educational manager.
 */
@ManagedBean
@SessionScoped
public class InstitutionsBean implements Serializable {

    private static EducationalInstitutions manager = new EducationalInstitutions();
    private EducationalInstitution currentEntry;

    public InstitutionsBean() {
        createNewItem(null);
    }

    public List<EducationalInstitution> getInstitutions() {
        return manager.getAllInstitutions(1, 100, null).getItems();
    }

    public void createNewItem(ActionEvent event) {
        currentEntry = new EducationalInstitution();
        Address address = new Address();
        address.setCity("San Jose");
        address.setState("CA");
        currentEntry.setAddress(address);
    }

    public void selectCurrentEntry(ActionEvent event) {
        EducationalInstitution entry = (EducationalInstitution) event.getComponent().getAttributes().get("entry");
        if (entry == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unable to identify selected record.", null));
        } else {
            currentEntry = entry;
        }
    }

    public void deleteItem(ActionEvent event) {
        if (currentEntry.getId() != null) {
            manager.deleteInstitution(currentEntry);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Operation successful", "Entry deleted successfully."));
            createNewItem(event);
        }
    }

    public EducationalInstitution getCurrentEntry() {
        return currentEntry;
    }

    public void saveEntry(ActionEvent event) {
        manager.createInstitution(Collections.singletonList(currentEntry));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Operation successful", "New instance created successfully."));
        createNewItem(event);
    }
}
