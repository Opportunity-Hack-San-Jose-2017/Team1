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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The basic response object for paginated results.
 *
 * @param <T> the type of the object being encapsulated within this response object.
 */
public class PaginatedResponse<T extends Serializable> extends AbstractRestResponse {

    public static final String REQUEST_PARAM_PAGE = "page";
    public static final String REQUEST_PARAM_PAGE_SIZE = "page_size";

    private static final List<String> TEMPLATE_PARAMS = Arrays.asList(REQUEST_PARAM_PAGE, REQUEST_PARAM_PAGE_SIZE);

    private List<T> items;
    private int page;
    private int itemsPerPage;
    private long totalCount;

    /**
     * Default constructor that can be used for DESERIALIZATION on the client side.
     */
    public PaginatedResponse() {
        items = Collections.emptyList();
        page = 1;
        itemsPerPage = 25;
        totalCount = 0;
    }

    /**
     * Construct a new instance of <code>PaginatedResponse</code> with the given arguments.
     */
    public PaginatedResponse(List<T> items, int page, int itemsPerPage, int totalCount, UriInfo uriInfo) {
        this.items = items;
        this.page = page < 1 ? 1 : page;
        this.itemsPerPage = itemsPerPage;
        this.totalCount = totalCount;
        constructLinks(uriInfo);
    }

    /**
     * Get the collection of items encapsulated within this response container.
     *
     * @return the collection of items encapsulated within this response container.
     */
    @JsonProperty("items")
    public List<T> getItems() {
        return items;
    }

    /**
     * Set the collection of items encapsulated within this response container.
     *
     * @param items the collection of items encapsulated within this response container.
     */
    public void setItems(List<T> items) {
        this.items = items;
    }

    /**
     * Set the current page represented by this response object.
     *
     * @return the current page represented by this response object.
     */
    @JsonIgnore
    public int getPage() {
        return page;
    }

    /**
     * Set the current page represented by this response object.
     *
     * @param page the current page represented by this response object.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Get the number of items returned per page.
     *
     * @return the number of items returned per page.
     */
    @JsonIgnore
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * Set the number of items returned per page.
     *
     * @param itemsPerPage the number of items returned per page.
     */
    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    /**
     * Get the total number of items in the full result list, not just this page.
     *
     * @return the total number of items in the full result list, not just this page.
     */
    @JsonProperty("total_items")
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * Set the total number of items in the full result list, not just this page.
     *
     * @param totalCount the total number of items in the full result list, not just this page.
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Construct the links that will be returned along with this paginated response.
     *
     * @param uriInfo the current URI info object.
     */
    public void constructLinks(UriInfo uriInfo) {

        if (uriInfo == null) {
            return;
        }

        // Construct the path.
        StringBuilder path = new StringBuilder(100);
        path.append("/");
        if (uriInfo.getBaseUri() != null) {
            path = appendPath(path, uriInfo.getBaseUri().getPath());
        }
        if (uriInfo.getPath() != null) {
            path = appendPath(path, uriInfo.getPath());
        }

        UriBuilder builderBase = UriBuilder.fromPath(path.toString());
        MultivaluedMap<String, String> existingParams = uriInfo.getQueryParameters();
        for (String key : existingParams.keySet()) {

            if (TEMPLATE_PARAMS.contains(key)) {
                continue;
            }
            builderBase.queryParam(key, existingParams.get(key).toArray());
        }

        int totalNumberOfPages = (int) Math.ceil((totalCount * 1.0) / itemsPerPage);

        if (page > 1) {
            addLink(constructLink(builderBase, Link.REL_FIRST, 1));
            addLink(constructLink(builderBase, Link.REL_PREVIOUS, page - 1));
        }

        // Always add self.
        addLink(constructLink(builderBase, Link.REL_SELF, page));

        if (page < totalNumberOfPages) {
            addLink(constructLink(builderBase, Link.REL_NEXT, page + 1));
            addLink(constructLink(builderBase, Link.REL_LAST, totalNumberOfPages));
        }
    }

    /**
     * Appends the two paths ensuring that there is no '//' being put in place accidentally.
     *
     * @param base      the base URI path.
     * @param predicate the path to append.
     * @return a cleaned up URI path.
     */
    private StringBuilder appendPath(StringBuilder base, String predicate) {
        if (predicate != null) {
            boolean baseEndsWithSlash = base.toString().endsWith("/");
            if (!baseEndsWithSlash && !predicate.startsWith("/")) {
                // some goof up, add the slash in between.
                base.append("/").append(predicate);
            } else if (baseEndsWithSlash && predicate.startsWith("/")) {
                base.append(predicate.substring(1));
            } else {
                base.append(predicate);
            }
        }
        return base;
    }

    private Link constructLink(UriBuilder base, String relationship, int page) {
        UriBuilder uriBuilder = base.clone();
        uriBuilder.queryParam(REQUEST_PARAM_PAGE, page);
        uriBuilder.queryParam(REQUEST_PARAM_PAGE_SIZE, itemsPerPage);

        // javax.ws.rs.core.Link.Builder linkBuilder = javax.ws.rs.core.Link.fromUriBuilder(uriBuilder);
        // linkBuilder.rel(relationship);
        // return linkBuilder.build();

        return new Link(relationship, uriBuilder.build().toString());
    }

    ///
    /// Equals & hash code
    ///

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaginatedResponse)) return false;
        if (!super.equals(o)) return false;

        PaginatedResponse<?> response = (PaginatedResponse<?>) o;

        if (totalCount != response.totalCount) return false;
        return items != null ? items.equals(response.items) : response.items == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (int) (totalCount ^ (totalCount >>> 32));
        return result;
    }
}
