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

package gov.sanjoseca.programs.walknroll.service;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.googlecode.objectify.Ref;
import gov.sanjoseca.programs.walknroll.model.Actor;
import gov.sanjoseca.programs.walknroll.model.EducationalInstitution;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.rest.PaginatedResponse;
import io.swagger.annotations.Api;

import javax.faces.context.FacesContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;

@Api
@Path("/v1/institutions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EducationalInstitutions {

    private static final Actors ACTORS = new Actors();

    @GET
    public PaginatedResponse<EducationalInstitution> getAllInstitutions(@QueryParam("page") @DefaultValue("1") int page,
                                                                        @QueryParam("pageSize") @DefaultValue("100") int pageSize,

                                                                        @Context UriInfo uriInfo) {
        if (page < 1) {
            page = 1;
        }
        int offset = (page - 1) * pageSize;


        List<EducationalInstitution> results =
                PersistentDataManager.getInstance(EducationalInstitution.class).getItems(offset, pageSize, null);
        return new PaginatedResponse<>(results, page, pageSize, results.size(), uriInfo);
    }

    @GET
    @Path("/id/{id}")
    public EducationalInstitution getInstitutionById(@PathParam("id") Long id) {
        return PersistentDataManager.getInstance(EducationalInstitution.class).getItemById(id);
    }

    @GET
    @Path("/name/{name}")
    public EducationalInstitution getInstanceByName(@PathParam("name") String name) {
        Query.Filter filter = new FilterPredicate("name", FilterOperator.EQUAL, name);
        return PersistentDataManager.getInstance(EducationalInstitution.class).getItemByFilter(filter);
    }

    @GET
    @Path("/user/{emailId}")
    public PaginatedResponse<EducationalInstitution> getInstitutionsForUser(@PathParam("emailId") String emailId,
                                                                            @Context UriInfo uriInfo) {
        List<EducationalInstitution> results = Collections.emptyList();
        if (emailId != null) {
            Query.Filter filter = new FilterPredicate("authorizedUsers", FilterOperator.IN,
                    Collections.singleton(emailId));
            results = PersistentDataManager.getInstance(EducationalInstitution.class).getItems(0, 100, filter);
        }
        return new PaginatedResponse<>(results, 1, results.size(), results.size(), uriInfo);
    }

    @POST
    public void createInstitution(List<EducationalInstitution> institutions) {
        // Check if there is already an institution or not.
        institutions.forEach(institution -> {
            EducationalInstitution entry = getInstanceByName(institution.getName());
            if (entry != null) {
                entry.merge(institution);
            } else {
                entry = institution;
            }
            PersistentDataManager.getInstance(EducationalInstitution.class).persist(entry);
            // Persist the actor references as well.
            updateActorMappings(entry);
        });
    }

    public void deleteInstitution(EducationalInstitution institution) {
        PersistentDataManager.getInstance(EducationalInstitution.class).delete(institution);
    }

    private void updateActorMappings(final EducationalInstitution institution) {
        institution.getAuthorizedUsers().forEach(user -> {
            Actor actor = new Actor();
            actor.setName(user);
            actor.setType(Actor.Type.INSTRUCTOR);
            actor.getInstitutions().add(Ref.create(institution));

            ACTORS.createEntry(actor);
        });


    }
}
