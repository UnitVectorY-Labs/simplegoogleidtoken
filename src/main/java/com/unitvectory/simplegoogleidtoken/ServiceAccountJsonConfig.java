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

import com.google.gson.Gson;

import lombok.Builder;

/**
 * The service account configuration that passes the service account JSON content in directly.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Builder(setterPrefix = "with")
public class ServiceAccountJsonConfig implements ServiceAccountConfig {

    /**
     * The service account JSON credentials.
     */
    private final String serviceAccountJson;

    @Override
    public String signServiceAccountJwt( SimpleRequest request) {

        if(request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }

        if(serviceAccountJson == null) {
            throw new SimpleSignException("serviceAccountJson not provided");
        }

        Gson gson  = SimpleUtil.GSON;

        // Parse the service account JSON
        GoogleServiceAccountCredentials accountInfo =  gson.fromJson(serviceAccountJson, GoogleServiceAccountCredentials.class);

        // JWT requires the current time for expiration
        long now = System.currentTimeMillis();

        // Generate and sign the JWT 
        return LocalSignerHelper.createJWT(accountInfo, request, now);
    }

}
