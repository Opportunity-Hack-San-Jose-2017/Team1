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

package gov.sanjoseca.programs.walknroll.model;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Translate;
import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherCondition;
import gov.sanjoseca.programs.walknroll.objectify.ISO8601DateTranslatorFactory;

@Entity
@Cache
public class WeatherData extends AbstractPersistentObject<WeatherData> {

    @Index
    private String city;

    @Index
    private String state;

    @Index
    @Translate(ISO8601DateTranslatorFactory.class)
    private ISO8601Date dateTime;

    private float temperature;
    private String weather;

    public WeatherData() {
    }

    public WeatherData(WeatherCondition condition) {
        this.city = condition.getCity();
        this.state = condition.getState();
        this.temperature = condition.getTemp_f();
        this.weather = condition.getWeather();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ISO8601Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(ISO8601Date dateTime) {
        this.dateTime = dateTime;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public void merge(WeatherData entity) {
        // Do nothing.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherData)) return false;

        WeatherData that = (WeatherData) o;

        if (Float.compare(that.temperature, temperature) != 0) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null) return false;
        return weather != null ? weather.equals(that.weather) : that.weather == null;
    }

    @Override
    public int hashCode() {
        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (temperature != +0.0f ? Float.floatToIntBits(temperature) : 0);
        result = 31 * result + (weather != null ? weather.hashCode() : 0);
        return result;
    }
}
