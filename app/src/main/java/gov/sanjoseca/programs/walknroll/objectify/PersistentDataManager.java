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

import com.google.appengine.api.datastore.Query.Filter;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.cmd.Query;
import gov.sanjoseca.programs.walknroll.model.PersistentObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Generic class for managing persistent data within the system.
 *
 * @param <T> the persistent data object being managed.
 */
public class PersistentDataManager<T extends PersistentObject<T, ?>> {

    private static final String CLASS_NAME = PersistentDataManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private static final Map<Class<? extends PersistentObject>, PersistentDataManager> CACHED_INSTANCES = new HashMap<>();

    private Class<T> dataType;

    public PersistentDataManager(Class<T> dataType) {
        this.dataType = dataType;
    }

    /**
     * Get a list of items stored against the data type in the backend.
     *
     * @param offset the offset to start from. Default to 0
     * @param limit  the number of records to fetch. Default to 100.
     * @param filter any filter that needs to be applied on the query.
     * @return a list of items.
     */
    public List<T> getItems(int offset, int limit, Filter filter) {
        Query<T> query = ofy().load().type(dataType).limit(limit).offset(offset);
        if (filter != null) {
            query.filter(filter);
        }
        return query.list();
    }

    /**
     * Get an item stored in the datastore, identified by the given id.
     *
     * @param id the id associated with the data type.
     * @return an item if found.
     */
    public T getItemById(long id) {
        return ofy().load().type(dataType).id(id).now();
    }

    /**
     * Get an item stored in the datastore, identified by the given id.
     *
     * @param id the id associated with the data type.
     * @return an item if found.
     */
    public T getItemById(String id) {
        return ofy().load().type(dataType).id(id).now();
    }

    /**
     * Get an item stored in the datastore, identified by the given filter.
     *
     * @param filter the filter to get to the item.
     * @return an item if found.
     */
    public T getItemByFilter(Filter filter) {
        return ofy().load().type(dataType).filter(filter).first().now();
    }

    /**
     * Save the given item to the backend.
     *
     * @param item the item that needs to be saved.
     */
    public void persist(T item) {
        final String METHOD_NAME = "persist";
        if (item == null) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME, "Ignoring the persistence of null entity.");
            return;
        }
        ofy().save().entity(item).now();
    }

    /**
     * Delete the given item from the backend.
     *
     * @param item the item to delete.
     */
    public void delete(T item) {
        final String METHOD_NAME = "delete";
        if (item == null || item.getId() == null) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME, "Ignoring deletion of null entity / entity with no ID.");
            return;
        }
        ofy().delete().entities(item);
    }

    /**
     * Get an instance of <code>PersistentDataManager</code> associated with the given data type.
     *
     * @param type the data type that needs be managed.
     * @return a PersistentDataManager instance associated with the given data type.
     */
    public static <E extends PersistentObject<E, ?>> PersistentDataManager<E> getInstance(Class<E> type) {
        // Check if the class has the required objectify annotations or not.
        if (type.getAnnotation(Entity.class) == null) {
            String msg = String.format("Given type does not have either of the required annotations: %s",
                    Arrays.toString(new Object[]{Entity.class.getName(), Subclass.class.getName()}));
            throw new IllegalArgumentException(msg);
        }
        return (PersistentDataManager<E>) CACHED_INSTANCES.computeIfAbsent(type, PersistentDataManager::new);
    }
}
