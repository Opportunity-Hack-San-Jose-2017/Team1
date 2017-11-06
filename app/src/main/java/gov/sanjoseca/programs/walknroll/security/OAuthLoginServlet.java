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

package gov.sanjoseca.programs.walknroll.security;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import gov.sanjoseca.programs.walknroll.internal.config.ApplicationConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@WebServlet("/oauth-login")
public class OAuthLoginServlet extends AbstractAuthorizationCodeServlet {

    private static final String CLIENT_ID_KEY = "gae_client_id";
    private static final String CLIENT_SECRET_KEY = "gae_client_secret";
    private static final Collection<String> SCOPES =
            Arrays.asList("profile", "email", "openid", "https://www.googleapis.com/auth/plus.me",
                    "https://www.googleapis.com/auth/userinfo.email");

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        // Load the clientID and secret from the Application's configuration.
        String clientID = ApplicationConfiguration.getInstance().getApplicationConfiguration(CLIENT_ID_KEY, true);
        String clientSecret = ApplicationConfiguration.getInstance().getApplicationConfiguration(CLIENT_SECRET_KEY, true);
        return new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
                clientID, clientSecret, SCOPES)
                .setDataStoreFactory(new AppEngineDataStoreFactory())
                .setAccessType("online").build();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath("/oauth");
        return url.build();
    }

    @Override
    protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
        return req.getRemoteUser();
    }
}
