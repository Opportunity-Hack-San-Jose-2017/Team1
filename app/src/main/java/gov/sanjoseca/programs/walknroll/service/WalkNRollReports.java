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

import gov.sanjoseca.programs.walknroll.model.WalkNRollEntry;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.rest.PaginatedResponse;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Api
@Path("/v1/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WalkNRollReports {

    @GET
    public PaginatedResponse<WalkNRollEntry> getEntries(@QueryParam("page") @DefaultValue("1") int page,
                                                        @QueryParam("pageSize") @DefaultValue("100") int pageSize,
                                                        @Context UriInfo uriInfo) {
        int offset = (page - 1) * pageSize;
        if (offset < 1) {
            offset = 0;
        }

        List<WalkNRollEntry> results =
                PersistentDataManager.getInstance(WalkNRollEntry.class).getItems(offset, pageSize, null);
        return new PaginatedResponse<>(results, page, pageSize, results.size(), uriInfo);
    }

    @GET
    @Path("/id/{id}")
    public WalkNRollEntry getInstanceById(@PathParam("id") Long id) {
        return PersistentDataManager.getInstance(WalkNRollEntry.class).getItemById(id);
    }

    @POST
    public void createEntry(WalkNRollEntry actor) {
        // Check if there is already an institution or not.
        WalkNRollEntry entry = null; // getInstanceByName(actor.getName());
        if (entry != null) {
            // entry.setType(actor.getType());
        } else {
            entry = actor;
        }
        PersistentDataManager.getInstance(WalkNRollEntry.class).persist(entry);
    }
}
