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

import lombok.Builder;

/**
 * Provides mechanism for easily requesting a Google ID token for a target
 * audience using a service account.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Builder(setterPrefix = "with")
public class SimpleGoogleIdToken {

    /**
     * The configuration for loading the service account.
     */
    @Builder.Default
    private ServiceAccountConfig serviceAccountConfig = new ServiceAccountDefaultGoogleCredentialsConfig();

    /**
     * The configuring for making the HTTP request to get the Google ID token.
     */
    @Builder.Default
    private GoogleIdTokenRequest googleIdTokenRequest = new GoogleIdTokenHttpURLConnectionRequest();

    /**
     * Requests a Google ID token for the target audience using the service account.
     * 
     * @param request the request
     * @return the response
     */
    public SimpleResponse getIdToken(SimpleRequest request) {

        // Input validation

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (serviceAccountConfig == null) {
            throw new SimpleSignException("serviceAccountConfig cannot be null");
        }

        if (googleIdTokenRequest == null) {
            throw new SimpleExchangeException("googleIdTokenRequest cannot be null");
        }

        // Generate the JWT
        String jwt = this.serviceAccountConfig.signServiceAccountJwt(request);

        // Exchange for the ID token
        return this.googleIdTokenRequest.getGoogleIdToken(jwt);
    }
}