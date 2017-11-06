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

package gov.sanjoseca.programs.walknroll.internal.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@FacesConverter("string.to.list.converter")
public class ListConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        List<String> result = new ArrayList<>();
        if (value != null) {
            for (String s : value.split(",")) {
                result.add(s.trim());
            }
        }
        return result;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        StringJoiner joiner = new StringJoiner(",");
        if (value instanceof List) {
            ((List<String>) value).forEach(joiner::add);
        }
        return joiner.toString();
    }
}
