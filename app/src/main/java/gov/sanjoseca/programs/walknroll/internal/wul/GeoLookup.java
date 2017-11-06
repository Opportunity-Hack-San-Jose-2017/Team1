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

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.sanjoseca.programs.walknroll.rest.AbstractRestResponse;

import java.util.Map;

/**
 * Encapsulates the response from WUL::geolookup API call.
 */
public class GeoLookup extends AbstractRestResponse {

    private String city;
    private String state;
    private String country;

    @JsonProperty("location")
    private void mapLocationAttributes(Map<String, Object> jsonData) {
        this.city = String.valueOf(jsonData.get("city"));
        this.state = String.valueOf(jsonData.get("state"));
        this.country = String.valueOf(jsonData.get("country"));
    }

    /**
     * Get the city name.
     *
     * @return the city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the state name.
     *
     * @return the state name.
     */
    public String getState() {
        return state;
    }

    /**
     * Get the country name.
     *
     * @return the country name.
     */
    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "GeoLookup [" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ']';
    }
}
