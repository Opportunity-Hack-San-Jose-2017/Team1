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

package gov.sanjoseca.programs.walknroll.internal.wul;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Service for accessing the Weather Underground API.
 */
@Path("/api/{apiKey}")
public interface WeatherUnderground {

    /**
     * The name of the API key that can be used for persisting within the application.
     */
    String API_KEY_NAME = "WEATHER_UNDERGROUND_API_KEY";

    /**
     * Lookup details about the given zip code.
     *
     * @param apiKey  the API key for accessing WUL services.
     * @param zipCode the zip code to lookup.
     * @return the response from WUL.
     */
    @GET
    @Path("/geolookup/q/{zipCode}.json")
    GeoLookup geoLookup(@PathParam("apiKey") String apiKey, @PathParam("zipCode") String zipCode);

    /**
     * Get the current weather conditions for the given zip code.
     *
     * @param apiKey  the API key for accessing WUL services.
     * @param zipCode the zip code to lookup.
     * @return the response from WUL.
     */
    @GET
    @Path("/conditions/q/{zipCode}.json")
    WeatherCondition getWeatherCondition(@PathParam("apiKey") String apiKey, @PathParam("zipCode") String zipCode);
}
