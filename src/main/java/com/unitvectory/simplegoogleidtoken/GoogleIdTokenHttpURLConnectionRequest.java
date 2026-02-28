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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Requests a Google ID token using a HTTP URL connection.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class GoogleIdTokenHttpURLConnectionRequest implements GoogleIdTokenRequest {

    private final String tokenUrl;

    /**
     * Creates a new instance using the default Google token URL.
     */
    public GoogleIdTokenHttpURLConnectionRequest() {
        this.tokenUrl = GOOGLE_TOKEN_URL;
    }

    /**
     * Creates a new instance using a custom token URL.
     * 
     * @param tokenUrl the token URL
     */
    GoogleIdTokenHttpURLConnectionRequest(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    @Override
    public SimpleResponse getGoogleIdToken(String serviceAccountJwt) {
        Gson gson = SimpleUtil.GSON;

        String payload = String.format("grant_type=%s&assertion=%s",
                URLEncoder.encode("urn:ietf:params:oauth:grant-type:jwt-bearer", StandardCharsets.UTF_8),
                URLEncoder.encode(serviceAccountJwt, StandardCharsets.UTF_8));

        HttpURLConnection con = null;
        try {

            con = createConnection(this.tokenUrl, payload);

            int responseCode = con.getResponseCode();

            String response;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = readResponse(con.getInputStream());
            } else {
                throw new SimpleExchangeException(
                        "Failed to get ID token. Response Code: " + responseCode);
            }

            GoogleIdTokenResponse tokenResponse = gson.fromJson(response, GoogleIdTokenResponse.class);

            return SimpleResponse.builder().withIdToken(tokenResponse.getId_token()).build();

        } catch (IOException e) {
            throw new SimpleExchangeException("IO error occurred while getting ID token", e);
        } catch (JsonSyntaxException e) {
            throw new SimpleExchangeException("Failed to parse ID token response", e);
        } catch (SimpleExchangeException e) {
            throw e; // Re-throwing the custom exception
        } catch (Exception e) {
            throw new SimpleExchangeException("Unexpected error occurred while getting ID token", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private HttpURLConnection createConnection(String url, String payload) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream os = con.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }
        return con;
    }

    private String readResponse(InputStream stream) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}