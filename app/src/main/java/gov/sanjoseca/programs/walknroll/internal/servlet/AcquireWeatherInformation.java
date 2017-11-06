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

package gov.sanjoseca.programs.walknroll.internal.servlet;

import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.internal.config.ApplicationConfiguration;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherCondition;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherUnderground;
import gov.sanjoseca.programs.walknroll.model.WeatherData;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.rest.ServiceClients;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Task based servlet for acquiring the weather information at the current time.
 */
@WebServlet(name = "AcquireWeatherInformation", value = "/tasks/acquireWeatherInfo")
public class AcquireWeatherInformation extends AppCommonServlet {

    private static final String CLASS_NAME = AcquireWeatherInformation.class.getName();

    private WeatherUnderground client = ServiceClients.createWebServiceClient(WeatherUnderground.class,
            "http://api.wunderground.com");

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String METHOD_NAME = "processRequest";

        // Check if the key has been saved or not.
        String apiKey = ApplicationConfiguration.getInstance()
                .getApplicationConfiguration(WeatherUnderground.API_KEY_NAME, false);
        if (StringUtils.isEmpty(apiKey)) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME,
                    "Weather API Key has not been set. Cannot acquire weather data.");
            return;
        }

        WeatherCondition condition = client.getWeatherCondition(apiKey, "95134");
        // Persist this data to the backend.
        WeatherData data = new WeatherData(condition);
        data.setDateTime(new ISO8601Date());
        PersistentDataManager.getInstance(WeatherData.class).persist(data);
    }
}
