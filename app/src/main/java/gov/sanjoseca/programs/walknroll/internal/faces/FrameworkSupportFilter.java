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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.sun.faces.context.FacesFileNotFoundException;
import gov.sanjoseca.programs.walknroll.model.Actor;
import gov.sanjoseca.programs.walknroll.service.Actors;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>FrameworkSupportFilter</code> should be the first filter in the sequence of filters. It takes care of a
 * few workarounds when running in the Dev & Production mode.
 */
@WebFilter(value = "/*")
public class FrameworkSupportFilter implements Filter {

    private static final String CLASS_NAME = FrameworkSupportFilter.class.getName();
    private static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private static final Actors ACTORS = new Actors();

    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {
        final String METHOD_NAME = "doFilter";

        HttpServletRequest request = new FrameworkServletRequestWrapper((HttpServletRequest) req);
        HttpServletResponse response = new FrameworkServletResponseWrapper((HttpServletResponse) resp);

        try {
            boolean proceed = true;
            if (((HttpServletRequest) req).getRequestURI().startsWith("/submit/")) {
                UserService userService = UserServiceFactory.getUserService();
                if (userService.isUserLoggedIn()) {
                    Actor actor = ACTORS.getInstanceByName(userService.getCurrentUser().getEmail());
                    if (actor == null || actor.getInstitutions().isEmpty()) {
                        // 403.
                        response.sendError(403, "You do not have access to this resource.");
                        proceed = false;
                    }
                }
            }
            if (proceed) {
                chain.doFilter(request, response);
            }
        } catch (FacesFileNotFoundException ex) {
            LOGGER.logp(Level.INFO, CLASS_NAME, METHOD_NAME,
                    "Failed to locate requested page: " + request.getRequestURI());
            response.sendError(404);
        } catch (Exception ex) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Exception occurred", ex);
            // Redirect user to the error page.
            response.sendError(500);
        } catch (Error error) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Error occurred", error);
            // Redirect user to the error page.
            response.sendError(500);
        }
    }
}
