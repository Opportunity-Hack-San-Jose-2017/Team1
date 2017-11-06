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

import org.glassfish.jersey.client.proxy.WebResourceFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Producer for various service clients.
 */
public class ServiceClients {

    /**
     * Create a proxy web service client.
     *
     * @param proxyInterface the service interface.
     * @param endPointURL    the end point at which this URL is present.
     * @param <T>            the service interface type.
     * @return a JAX-RS proxy client.
     */
    public static <T> T createWebServiceClient(Class<T> proxyInterface, String endPointURL) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(endPointURL);
        return WebResourceFactory.newResource(proxyInterface, target);
    }

}
