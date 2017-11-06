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
 * Represents the response returned by the WeatherUnderground API calls.
 */
public class WeatherCondition extends AbstractRestResponse {

    private String city;
    private String state;
    private String country;
    private float temp_f;
    private float temp_c;
    private String weather;

    @JsonProperty("current_observation")
    private void mapAttributes(Map<String, Object> jsonData) {
        this.temp_f = Float.parseFloat(String.valueOf(jsonData.get("temp_f")));
        this.temp_c = Float.parseFloat(String.valueOf(jsonData.get("temp_c")));
        this.weather = String.valueOf(jsonData.get("weather"));

        Map<String, Object> locationData = (Map<String, Object>) jsonData.get("display_location");
        this.city = String.valueOf(locationData.get("city"));
        this.state = String.valueOf(locationData.get("state"));
        this.country = String.valueOf(locationData.get("country"));

    }

    /**
     * Get the city associated with this weather reading.
     *
     * @return the city associated with this weather reading.
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the state associated with this weather reading.
     *
     * @return the state associated with this weather reading.
     */
    public String getState() {
        return state;
    }

    /**
     * Get the country associated with this weather reading.
     *
     * @return the country associated with this weather reading.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get the temperature reading in Fahrenheit.
     *
     * @return the temperature reading in Fahrenheit.
     */
    public float getTemp_f() {
        return temp_f;
    }

    /**
     * Get the temperature reading in Celsius.
     *
     * @return the temperature reading in Celsius.
     */
    public float getTemp_c() {
        return temp_c;
    }

    /**
     * Get the text representation of the weather reading. E.g., 'Mostly cloudy'.
     *
     * @return the text representation of the weather reading.
     */
    public String getWeather() {
        return weather;
    }

    @Override
    public String toString() {
        return "WeatherCondition [" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", temp_f=" + temp_f +
                ", temp_c=" + temp_c +
                ", weather='" + weather + '\'' +
                ']';
    }
}
