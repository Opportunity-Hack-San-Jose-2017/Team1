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

package gov.sanjoseca.programs.walknroll.internal.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;

// TODO: Do I still need this one??
@WebServlet(name = "Session Cleaner", value = "/tasks/sessionCleaner")
public class SessionCleaner extends AppCommonServlet {
    private static final String CLASS_NAME = SessionCleaner.class.getName();

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String METHOD_NAME = "processRequest";
        // get the session ttl in milliseconds.
        long ttl = request.getSession().getMaxInactiveInterval() * 1000;
        // double up that value. We will delete any record that goes beyond that value.
        ttl += ttl;
        try {
//            List<Key> keys = getStaleSessionKeys(ttl);
//            deleteSessionEntries(keys);
        } catch (Exception e) {
            LOGGER.logp(Level.WARNING, CLASS_NAME, METHOD_NAME, "Failed to clean up stale sessions.", e);
        }
    }
}
