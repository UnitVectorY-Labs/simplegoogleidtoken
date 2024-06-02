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

import java.io.FileInputStream;

import com.google.gson.Gson;

import lombok.Builder;

/**
 * The service account configuration that is loaded from a specified file path.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Builder(setterPrefix = "with")
public class ServiceAccountFileConfig implements ServiceAccountConfig {

    /**
     * The path to the service account file.
     */
    private final String filePath;

    @Override
    public String signServiceAccountJwt(SimpleRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("request not provided");
        }

        if (filePath == null) {
            throw new SimpleSignException("filePath not provided");
        }

        Gson gson = SimpleUtil.GSON;

        GoogleServiceAccountCredentials accountInfo = null;

        try (FileInputStream serviceAccountStream = new FileInputStream(filePath)) {
            // Parse the service account JSON
            accountInfo = gson.fromJson(new String(serviceAccountStream.readAllBytes()),
                    GoogleServiceAccountCredentials.class);
        } catch (Exception e) {
            throw new SimpleSignException("Error reading service account file", e);
        }

        // JWT requires the current time for expiration
        long now = System.currentTimeMillis();

        // Generate and sign the JWT
        return LocalSignerHelper.createJWT(accountInfo, request, now);
    }

}
