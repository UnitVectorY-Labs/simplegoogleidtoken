/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.simplegoogleidtoken;

/**
 * The interface for requesting a Google ID token given the JWT from the service
 * account.
 * 
 * This interface allows for separate implementations for different underlying
 * HTTP clients.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public interface GoogleIdTokenRequest {

    /**
     * The Google token URL.
     */
    static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    /**
     * Exchanges the service account JWT for a Google ID token.
     * 
     * @param serviceAccountJwt the service account JWT
     * @return the response
     */
    SimpleResponse getGoogleIdToken(String serviceAccountJwt);

}
