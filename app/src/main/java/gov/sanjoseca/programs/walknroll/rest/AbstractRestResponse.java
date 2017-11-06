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

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for all REST responses generated or consumed within this application.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractRestResponse implements Serializable {

    private static final String CLASS_NAME = AbstractRestResponse.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private Map<String, Link> linksMapping;

    // The same class can also be used on the client side to deserialize the data being received.
    // Hence, this class has methods in place that allow for reuse on the client side of the equation as well.
    // Do not force clients to upgrade just because we started sending a new attribute in our response. Allow the
    // clients to access the required information from an unmapped list.
    private Map<String, Object> unmappedData = new HashMap<>();

    /**
     * During the serialization of REST data, store any unmapped attributes in an internal map using this method.
     *
     * @param name  the name of the attribute.
     * @param value the value of the attribute.
     */
    @JsonAnySetter
    public void setUnmappedData(String name, Object value) {
        this.unmappedData.put(name, value);
    }

    /**
     * Get the collection containing any unmapped data that was found during the serialization of REST response.
     */
    @JsonIgnore
    public Map<String, Object> getUnmappedData() {
        return this.unmappedData;
    }

    /**
     * Add a link, identified by the given relationship, to this response object.
     *
     * @param link the link reference.
     */
    public void addLink(Link link) {
        final String METHOD_NAME = "addLink";

        if (link == null) {
            return;
        }
        String relationship = link.getRel();

        if (linksMapping == null) {
            linksMapping = new HashMap<>();
        }

        if (linksMapping.containsKey(relationship)) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME, "A link with rel={0} already exists. Overwriting it.",
                    relationship);
        }

        linksMapping.put(relationship, link);
    }

    /**
     * Remove a link, identified by the given relationship, from this response object.
     *
     * @param relationship the relationship of the link to this response object.
     */
    public void removeLink(String relationship) {
        if (linksMapping != null) {
            linksMapping.remove(relationship);
        }
    }

    /**
     * Get the link identified by the given relationship name. If no such relationship exists, then this method will
     * return null.
     *
     * @param relationship the relationship of the link to this paginated response.
     * @return the link identified by the given relationship name.
     */
    @JsonIgnore
    public Link getLink(String relationship) {
        return linksMapping != null ? linksMapping.get(relationship) : null;
    }


    /**
     * Get the collection of links associated with this response object.
     *
     * @return the collection of links associated with this response object.
     */
    @JsonProperty("links")
    public Collection<Link> getLinks() {
        return linksMapping == null ? Collections.emptyList() : linksMapping.values();
    }

    /**
     * Set the collection of links associated with this response object.
     *
     * @param links the collection of links associated with this response object.
     */
    public void setLinks(List<Link> links) {
        if (links != null) {
            links.forEach(this::addLink);
        }
    }

    ///
    /// Equals & hash code
    ///

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRestResponse)) return false;

        AbstractRestResponse that = (AbstractRestResponse) o;

        return linksMapping != null ? linksMapping.equals(that.linksMapping) : that.linksMapping == null;
    }

    @Override
    public int hashCode() {
        return linksMapping != null ? linksMapping.hashCode() : 0;
    }
}
