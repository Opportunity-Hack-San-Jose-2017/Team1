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

package gov.sanjoseca.programs.walknroll.view.pages;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class UserInfoBean {

    public static final String LOGGED_IN_USER_KEY = "LOGGED_IN_USER";

    private UserService userService = UserServiceFactory.getUserService();

    /**
     * Get the Google login URL.
     */
    public String getLoginURL() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        return userService.createLoginURL(baseURL);
        // return "/oauth-init";
    }

    /**
     * Get the Google logout URL.
     */
    public String getLogoutURL() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        return userService.createLogoutURL(baseURL);
    }

    public boolean isUserLoggedIn() {
        return userService.isUserLoggedIn();
    }

    public boolean isAdminUser() {
        return isUserLoggedIn() && userService.isUserAdmin();
    }

    public String getCurrentUser() {
        User user = userService.getCurrentUser();
        return user != null ? user.getUserId() : "Guest";
    }

    public void setLoggedInUser(String username) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if (session != null) {
            session.setAttribute(LOGGED_IN_USER_KEY, username);
        }
    }
}
