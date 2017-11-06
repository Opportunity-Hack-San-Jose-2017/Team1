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

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.rest.MissingParameterException;
import gov.sanjoseca.programs.walknroll.rest.PaginatedResponse;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Application configuration.
 */
// TODO: Protect this API endpoint.
@Api
@Path("/v1/admin/config")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ApplicationConfiguration {

    private static final ApplicationConfiguration INSTANCE = new ApplicationConfiguration();

    /**
     * Convenience method to get hold of an instance of this class.
     *
     * @return an instance of <code>ApplicationConfiguration</code>.
     */
    public static ApplicationConfiguration getInstance() {
        return INSTANCE;
    }

    @GET
    public PaginatedResponse<ConfigData> getConfigurations(@QueryParam("page") @DefaultValue("1") int page,
                                                           @QueryParam("pageSize") @DefaultValue("100") int pageSize,
                                                           @Context UriInfo uriInfo) {
        int offset = (page - 1) * pageSize;

        List<ConfigData> results = PersistentDataManager.getInstance(ConfigData.class).getItems(offset, pageSize, null);
        return new PaginatedResponse<>(results, page, pageSize, results.size(), uriInfo);
    }

    @GET
    @Path("/{name}")
    public ConfigData getConfiguration(@PathParam("name") String name) {
        Filter filter = new FilterPredicate("key", FilterOperator.EQUAL, name);
        return PersistentDataManager.getInstance(ConfigData.class).getItemByFilter(filter);
    }

    @POST
    public void setConfiguration(ConfigData data) {
        if (data == null) {
            throw new MissingParameterException("No content provided.", null);
        }
        ConfigData entry = getConfiguration(data.getKey());
        if (entry != null) {
            entry.setValue(data.getValue());
        } else {
            entry = data;
        }
        PersistentDataManager.getInstance(ConfigData.class).persist(entry);
    }

    /**
     * Convenience method to get hold of application properties.
     *
     * @param key                  the property key.
     * @param raiseExceptionIfNull set it to true if an exception should be raised if the value is null.
     * @return the value.
     * @throws IllegalStateException if the value was null and raiseExceptionIfNull parameter was set to true.
     */
    public String getApplicationConfiguration(String key, boolean raiseExceptionIfNull) {
        ConfigData configData = ApplicationConfiguration.getInstance().getConfiguration(key);
        String value = configData != null ? configData.getValue() : null;
        if (StringUtils.isEmpty(value) && raiseExceptionIfNull) {
            throw new IllegalStateException("Unable to process task. Value for " + key +
                    " was null. Please specify a valid value.");
        }
        return value;
    }

}
