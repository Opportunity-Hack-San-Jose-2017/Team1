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

import gov.sanjoseca.programs.walknroll.internal.config.ApplicationConfiguration;
import gov.sanjoseca.programs.walknroll.internal.config.ConfigData;
import gov.sanjoseca.programs.walknroll.internal.wul.WeatherUnderground;
import org.apache.commons.lang3.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * The settings bean.
 */
@ManagedBean
@RequestScoped
public class SettingsBean implements Serializable {

    private static ApplicationConfiguration APP_CONFIGURATION = new ApplicationConfiguration();

    public String getWeatherUndergroundAPIKey() {
        return getApplicationConfiguration(WeatherUnderground.API_KEY_NAME, false);
    }

    public void setWeatherUndergroundAPIKey(String value) {
        ConfigData data = new ConfigData(WeatherUnderground.API_KEY_NAME, value);
        APP_CONFIGURATION.setConfiguration(data);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Configurations saved successfully.", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    protected String getApplicationConfiguration(String key, boolean raiseExceptionIfNull) {
        ConfigData configData = APP_CONFIGURATION.getConfiguration(key);
        String value = configData != null ? configData.getValue() : null;
        if (StringUtils.isEmpty(value) && raiseExceptionIfNull) {
            throw new IllegalStateException("Unable to process task. Value for " + key
                    + " was null. Please specify a valid value.");
        }
        return value;
    }
}
