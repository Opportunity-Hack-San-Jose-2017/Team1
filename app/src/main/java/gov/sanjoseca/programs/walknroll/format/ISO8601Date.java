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

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Convenience class for REST APIs that need ISO8601 compliant date as an input.
 */
public class ISO8601Date extends Date {

    /**
     * Constructs a new instance of {@code ISO8601Date} by parsing the given input string.
     *
     * @param input the ISO8601 compliant date string.
     */
    public ISO8601Date(String input) throws ParseException {
        this(ISO8601Utils.parse(input, new ParsePosition(0)).getTime());
    }

    public ISO8601Date() {
        super();
    }

    public ISO8601Date(long date) {
        super(date);
    }

}
