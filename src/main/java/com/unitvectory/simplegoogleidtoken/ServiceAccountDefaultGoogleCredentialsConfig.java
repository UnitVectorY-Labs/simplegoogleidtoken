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

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;

/**
 * The service account configuration that loads the service account credentials
 * from Google in envirionments such as Cloud Run or Cloud Fucntions avoiding
 * the need to have a hard coded service account credentials.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class ServiceAccountDefaultGoogleCredentialsConfig implements ServiceAccountConfig {

    @Override
    public SimpleResponse signServiceAccountJwt(SimpleRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }

        String targetAudience = request.getTargetAudience();
        if (targetAudience == null || targetAudience.isEmpty()) {
            throw new SimpleSignException("Target audience is required");
        }

        // Obtain the GoogleCredentials instance from the application default
        // credentials
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.getApplicationDefault();
        } catch (IOException e) {
            throw new SimpleSignException("Failed to get GoogleCredentials.getApplicationDefault()", e);
        }

        if (!(credentials instanceof IdTokenProvider)) {
            throw new SimpleSignException("GoogleCredentials.getApplicationDefault() was not an IdTokenProvider");
        }

        IdTokenProvider idTokenProvider = (IdTokenProvider) credentials;

        // Create IdTokenCredentials with the target audience
        IdTokenCredentials idTokenCredentials = IdTokenCredentials.newBuilder()
                .setIdTokenProvider(idTokenProvider)
                .setTargetAudience(targetAudience)
                .build();

        // Get the ID token
        try {
            String idToken = idTokenCredentials.refreshAccessToken().getTokenValue();
            return SimpleResponse.builder().idToken(idToken).build();
        } catch (IOException e) {
            throw new SimpleSignException("Failed to get ID token", e);
        }
    }
}
