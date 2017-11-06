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

package gov.sanjoseca.programs.walknroll.view.pages.admin;

import com.google.appengine.api.datastore.Query.*;
import gov.sanjoseca.programs.walknroll.model.WalkNRollEntry;
import gov.sanjoseca.programs.walknroll.model.WeatherData;
import gov.sanjoseca.programs.walknroll.objectify.PersistentDataManager;
import gov.sanjoseca.programs.walknroll.service.WalkNRollReports;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@SessionScoped
public class ReportsBean implements Serializable {

    private static final WalkNRollReports REPORTS = new WalkNRollReports();
    private List<WalkNRollEntry> entries;

    public List<WalkNRollEntry> getEntries() {
        final String UNAVAILABLE = "Unavailable";
        if (entries == null) {
            entries = REPORTS.getEntries(1, 1000, null).getItems();

            // Associate the weather data with each of these entries.
            PersistentDataManager<WeatherData> weatherInfo = PersistentDataManager.getInstance(WeatherData.class);
            Map<String, String> cachedWeatherData = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            for (WalkNRollEntry entry : entries) {
                String wd = UNAVAILABLE;
                if (entry.getDateTime() != null) {
                    String date = dateFormat.format(entry.getDateTime());
                    wd = cachedWeatherData.computeIfAbsent(date, d -> {
                        calendar.setTime(entry.getDateTime());
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        String nextDay = dateFormat.format(calendar.getTime());
                        Filter lowerBound = new FilterPredicate("dateTime", FilterOperator.GREATER_THAN_OR_EQUAL, date);
                        Filter upperBound = new FilterPredicate("dateTime", FilterOperator.LESS_THAN, nextDay);
                        Filter compositeFilter = new CompositeFilter(CompositeFilterOperator.AND, Arrays.asList(lowerBound, upperBound));
                        List<WeatherData> availableData = weatherInfo.getItems(0, 1, compositeFilter);
                        if (availableData.isEmpty()) {
                            return UNAVAILABLE;
                        } else {
                            WeatherData dataPoint = availableData.get(0);
                            return String.format("%s (%sF)", dataPoint.getWeather(), ((int) dataPoint.getTemperature()));
                        }
                    });
                    entry.setWeather(wd);
                }
            }
        }
        return entries;
    }

}
