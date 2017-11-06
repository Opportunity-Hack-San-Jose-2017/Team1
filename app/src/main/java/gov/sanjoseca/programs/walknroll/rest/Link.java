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

package gov.sanjoseca.programs.walknroll.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.Map;

public class Link implements Serializable {

    /**
     * Common constant used for identifying the "self" link.
     */
    public static final String REL_SELF = "self";

    /**
     * Common constant used for identifying the "first" link.
     */
    public static final String REL_FIRST = "first";

    /**
     * Common constant used for identifying the "previous" link.
     */
    public static final String REL_PREVIOUS = "prev";

    /**
     * Common constant used for identifying the "next" link.
     */
    public static final String REL_NEXT = "next";

    /**
     * Common constant used for identifying the "last" link.
     */
    public static final String REL_LAST = "last";

    private String rel;

    private String href;

    @JsonCreator
    public Link(@JsonProperty("rel") String rel, @JsonProperty("href") String href) {
        this.rel = rel;
        this.href = href;
    }

    /**
     * Get the relationship of this link with respect to the object that embeds it.
     *
     * @return the relationship of this link with respect to the object that embeds it.
     */
    @JsonProperty("rel")
    public String getRel() {
        return rel;
    }

    /**
     * Set the relationship of this link with respect to the object that embeds it.
     *
     * @param rel the relationship of this link with respect to the object that embeds it.
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * Get the target URL for getting hold of the resource identified by this link object.
     *
     * @return the target URL for getting hold of the resource identified by this link object.
     */
    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    /**
     * Set the target URL for getting hold of the resource identified by this link object.
     *
     * @param href the target URL for getting hold of the resource identified by this link object.
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Convenience method for getting the query parameters specified on this link (if available).
     *
     * @param paramName the query param name.
     * @return a Pair&lt;String,String&gt; representing the param & its value, or null if no such query param exists.
     */
    public Map.Entry<String, String> getQueryParam(String paramName) {
        Pair<String, String> pair = null;
        if (StringUtils.isNotEmpty(paramName) && href != null) {
            int idx = href.indexOf("?");
            if (idx != -1) {
                String[] parts = href.substring(idx + 1).split("&");
                for (String p : parts) {
                    String[] params = p.split("=");
                    if (params.length == 2) {
                        if (paramName.equalsIgnoreCase(params[0])) {
                            pair = Pair.of(params[0].trim(), params[1]);
                            break;
                        }
                    }
                }
            }
        }
        return pair;
    }

    /**
     * Convenience method to get the value associated with a query parameter defined for this link.
     *
     * @param queryParam the query parameter specified in the link.
     * @return the value as parse from the link, or null if no such value was found.
     */
    public String getValueFromLink(String queryParam) {
        String value = null;
        Map.Entry<String, String> entry = getQueryParam(queryParam);
        if (entry != null) {
            value = entry.getValue();
        }
        return value;
    }


    ///
    /// Equals & hash code
    ///


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        if (rel != null ? !rel.equals(link.rel) : link.rel != null) return false;
        return href != null ? href.equals(link.href) : link.href == null;
    }

    @Override
    public int hashCode() {
        int result = rel != null ? rel.hashCode() : 0;
        result = 31 * result + (href != null ? href.hashCode() : 0);
        return result;
    }
}
