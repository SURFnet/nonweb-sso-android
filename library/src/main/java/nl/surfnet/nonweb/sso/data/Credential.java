package nl.surfnet.nonweb.sso.data;

/*
 * Copyright 2015 SURFnet BV, The Netherlands
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

import android.net.Uri;
import android.support.annotation.NonNull;

import nl.surfnet.nonweb.sso.util.Constants;

/**
 * Data Class containing essential .
 *
 * @author W.Elsinga
 */
public class Credential {
    private Uri _uri;

    /**
     * Default constructor.
     *
     * @param uri {@link android.net.Uri}
     */
    public Credential(@NonNull Uri uri) {
        _uri = Uri.parse(uri.toString().replace("#access_token", "?access_token"));
    }


    /**
     * Returns the access token issued by the authorization server.
     *
     * @return access token
     */
    public String getAccessToken() {
        return _uri.getQueryParameter("access_token");
    }

    /**
     * Returns the session identifier received by the authorization server.
     *
     * @return state
     */
    public String getSessionIdentifier() {
        return _uri.getQueryParameter(Constants.STATE);
    }
}
