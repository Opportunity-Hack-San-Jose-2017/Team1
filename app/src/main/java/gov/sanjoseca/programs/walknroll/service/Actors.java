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
import gov.sanjoseca.programs.walknroll.model.Actor;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.rest.PaginatedResponse;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Api
@Path("/v1/actors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Actors {

    @GET
    public PaginatedResponse<Actor> getEntries(@QueryParam("page") @DefaultValue("1") int page,
                                               @QueryParam("pageSize") @DefaultValue("100") int pageSize,
                                               @Context UriInfo uriInfo) {
        int offset = (page - 1) * pageSize;

        List<Actor> results = PersistentDataManager.getInstance(Actor.class).getItems(offset, pageSize, null);
        return new PaginatedResponse<>(results, page, pageSize, results.size(), uriInfo);
    }

    @GET
    @Path("/id/{id}")
    public Actor getInstitutionById(@PathParam("id") Long id) {
        return PersistentDataManager.getInstance(Actor.class).getItemById(id);
    }

    @GET
    @Path("/name/{name}")
    public Actor getInstanceByName(@PathParam("name") String name) {
        Query.Filter filter = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name);
        return PersistentDataManager.getInstance(Actor.class).getItemByFilter(filter);
    }

    @POST
    public void createEntry(Actor actor) {
        // Check if there is already an institution or not.
        Actor entry = getInstanceByName(actor.getName());
        if (entry != null) {
            entry.merge(actor);
        } else {
            entry = actor;
        }
        PersistentDataManager.getInstance(Actor.class).persist(entry);
    }

}
