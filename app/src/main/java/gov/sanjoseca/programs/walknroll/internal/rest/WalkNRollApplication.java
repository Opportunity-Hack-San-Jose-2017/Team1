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

package gov.sanjoseca.programs.walknroll.internal.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Initialize the REST server.
 */
@ApplicationPath("/api")
public class WalkNRollApplication extends ResourceConfig {

    public WalkNRollApplication() {
        packages("gov.sanjoseca.programs.walknroll");
        registerClasses(ApiListingResource.class, SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Walk-n-Roll APIs");
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http", "https"});
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("gov.sanjoseca.programs.walknroll");
        beanConfig.setScan(true);
    }
}
