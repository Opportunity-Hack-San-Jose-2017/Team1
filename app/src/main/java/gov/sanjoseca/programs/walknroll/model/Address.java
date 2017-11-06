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

import com.googlecode.objectify.annotation.Ignore;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Representation of an address reference. It also defines the granularity of the data being stored.
 */
// @Entity
public class Address extends AbstractPersistentObject<Address> {

    private String address;
    private String zipCode;
    private String city;
    private String state;
    @Ignore
    private String displayString;

    /**
     * Get the nearest land mark for this address. Like the crossing of two streets.
     *
     * @return the nearest land mark for this address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * the nearest land mark for this address. Like the crossing of two streets.
     *
     * @param address the nearest land mark for this address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the zip code for this address.
     *
     * @return the zip code for this address.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Set the zip code for this address.
     *
     * @param zipCode the zip code for this address.
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Get the city for this address.
     *
     * @return the city for this address.
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the city for this address.
     *
     * @param city the city for this address.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the state for this address.
     *
     * @return the state for this address.
     */
    public String getState() {
        return state;
    }

    /**
     * Set the state for this address.
     *
     * @param state the state for this address.
     */
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void merge(Address entity) {
        if (StringUtils.isNotEmpty(entity.address)) {
            this.address = entity.address;
        }
        if (StringUtils.isNotEmpty(entity.city)) {
            this.address = entity.address;
        }
        if (StringUtils.isNotEmpty(entity.state)) {
            this.state = entity.state;
        }
        if (StringUtils.isNotEmpty(entity.zipCode)) {
            this.zipCode = entity.zipCode;
        }
    }

    @Override
    public String toString() {
        if (displayString == null) {
            StringJoiner joiner = new StringJoiner(", ");
            Arrays.asList(address, city, state, zipCode).forEach(it -> {
                if (StringUtils.isNotEmpty(it)) {
                    joiner.add(it);
                }
            });
            displayString = joiner.toString();
        }
        return displayString;
    }

    //
    //  Equals & hashcode.
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (this.address != null ? !this.address.equals(address.address) : address.address != null) return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        return state != null ? state.equals(address.state) : address.state == null;
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
