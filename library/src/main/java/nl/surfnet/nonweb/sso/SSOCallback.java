package nl.surfnet.nonweb.sso;

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

import nl.surfnet.nonweb.sso.data.Credential;

/**
 * An {@code SSOCallback} represents the callback handler to be invoked
 * when an asynchronous call is completed or it fails.
 *
 * @author W.Elsinga
 */
public interface SSOCallback {
    /**
     * @param credential {@link Credential}
     */
    void success(Credential credential);

    /** */
    void failure();
}
