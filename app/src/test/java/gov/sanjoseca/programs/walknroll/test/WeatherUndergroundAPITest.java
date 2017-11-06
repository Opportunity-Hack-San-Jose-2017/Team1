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

package gov.sanjoseca.programs.walknroll.test;


import gov.sanjoseca.programs.walknroll.internal.wul.GeoLookup;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherCondition;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherUnderground;
import gov.sanjoseca.programs.walknroll.rest.ServiceClients;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class WeatherUndergroundAPITest {

    private static final String WUL_API_BASE = "http://api.wunderground.com";
    private static final String API_KEY = System.getProperty(WeatherUnderground.API_KEY_NAME, null);
    private static final String ZIP_CODE = "95134";
    private WeatherUnderground wsInstance;

    @BeforeClass
    private void setup() {

        if (API_KEY == null) {
            throw new SkipException("No API key defined. Skipping tests.");
        }
        wsInstance = ServiceClients.createWebServiceClient(WeatherUnderground.class, WUL_API_BASE);
    }

    @Test
    public void performGeoLookup() {
        GeoLookup lookup = wsInstance.geoLookup(API_KEY, ZIP_CODE);
        Assert.assertNotNull(lookup, "Response cannot be null.");
        Assert.assertEquals(lookup.getCity(), "San Jose", "Responses do not match. Did the input data change?");
        System.out.println(lookup);
    }

    @Test
    public void printCurrentWeatherConditions() {
        WeatherCondition condition = wsInstance.getWeatherCondition(API_KEY, ZIP_CODE);
        Assert.assertNotNull(condition, "Response cannot be null.");
        Assert.assertEquals(condition.getCity(), "San Jose", "Responses do not match. Did the input data change?");
        System.out.println(condition);
    }
}
