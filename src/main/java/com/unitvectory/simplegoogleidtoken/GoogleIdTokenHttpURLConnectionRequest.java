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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

/**
 * Requests a Google ID token using a HTTP URL connection.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class GoogleIdTokenHttpURLConnectionRequest implements GoogleIdTokenRequest {

    @Override
    public SimpleResponse getGoogleIdToken(String jwt) {
        Gson gson = SimpleUtil.GSON;

        try {
            String payload = String.format("grant_type=%s&assertion=%s",
                    URLEncoder.encode("urn:ietf:params:oauth:grant-type:jwt-bearer", StandardCharsets.UTF_8),
                    URLEncoder.encode(jwt, StandardCharsets.UTF_8));

            HttpURLConnection con = createConnection(GOOGLE_TOKEN_URL, payload);

            String response = readResponse(con);

            GoogleIdTokenResponse tokenResponse = gson.fromJson(response, GoogleIdTokenResponse.class);

            return SimpleResponse.builder().idToken(tokenResponse.getId_token()).build();

        } catch (Exception e) {
            throw new SimpleExchangeException("Failed to get ID token", e);
        }
    }

    private HttpURLConnection createConnection(String url, String payload) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
        return con;
    }

    private String readResponse(HttpURLConnection con) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}