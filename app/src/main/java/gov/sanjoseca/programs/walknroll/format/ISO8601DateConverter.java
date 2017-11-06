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

package gov.sanjoseca.programs.walknroll.format;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.googlecode.objectify.stringifier.Stringifier;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.ext.ParamConverter;
import java.text.ParseException;

/**
 * Converter for ISO8601Date.
 */
public class ISO8601DateConverter implements ParamConverter<ISO8601Date>, Stringifier<ISO8601Date> {

    @Override
    public ISO8601Date fromString(String value) {
        if (StringUtils.isEmpty(StringUtils.trim(value))) {
            return null;
        }
        try {
            return new ISO8601Date(value);
        } catch (ParseException e) {
            String msg = String.format("Date parameter is not in ISO format: '%s'", value);
            throw new IllegalArgumentException(msg, e);
        }
    }

    @Override
    public String toString(ISO8601Date value) {
        return value != null ? ISO8601Utils.format(value) : "";
    }
}
