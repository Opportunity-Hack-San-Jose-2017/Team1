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

package gov.sanjoseca.programs.walknroll.objectify;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.*;
import gov.sanjoseca.programs.walknroll.format.ISO8601Date;
import gov.sanjoseca.programs.walknroll.format.ISO8601DateConverter;

public class ISO8601DateTranslatorFactory extends ValueTranslatorFactory<ISO8601Date, String> {

    private static final ISO8601DateConverter converter = new ISO8601DateConverter();

    protected ISO8601DateTranslatorFactory() {
        super(ISO8601Date.class);
    }

    @Override
    protected ValueTranslator<ISO8601Date, String> createValueTranslator(TypeKey<ISO8601Date> tk, CreateContext ctx, Path path) {
        return new ValueTranslator<ISO8601Date, String>(String.class) {

            @Override
            protected ISO8601Date loadValue(String value, LoadContext ctx, Path path) throws SkipException {
                return converter.fromString(value);
            }

            @Override
            protected String saveValue(ISO8601Date value, boolean index, SaveContext ctx, Path path) throws SkipException {
                return converter.toString(value);
            }
        };
    }
}
