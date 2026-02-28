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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Test the LocalSignerHelper class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class LocalSignerHelperTest {

    @Test
    void nullServiceAccountInfoTest() {
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("https://example.com").build();
        assertThrows(NullPointerException.class,
                () -> LocalSignerHelper.createJWT(null, request, System.currentTimeMillis()));
    }

    @Test
    void nullSimpleRequestTest() {
        GoogleServiceAccountCredentials credentials = GoogleServiceAccountCredentialsExample.exampleObject();
        assertThrows(NullPointerException.class,
                () -> LocalSignerHelper.createJWT(credentials, null, System.currentTimeMillis()));
    }

    @Test
    void nullTargetAudienceTest() {
        GoogleServiceAccountCredentials credentials = GoogleServiceAccountCredentialsExample.exampleObject();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience(null).build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> LocalSignerHelper.createJWT(credentials, request, System.currentTimeMillis()));
        assertEquals("Target audience is required", exception.getMessage());
    }

    @Test
    void emptyTargetAudienceTest() {
        GoogleServiceAccountCredentials credentials = GoogleServiceAccountCredentialsExample.exampleObject();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("").build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> LocalSignerHelper.createJWT(credentials, request, System.currentTimeMillis()));
        assertEquals("Target audience is required", exception.getMessage());
    }

    @Test
    void invalidPrivateKeyTest() {
        GoogleServiceAccountCredentials credentials = new GoogleServiceAccountCredentials();
        credentials.setClient_email("test@example.com");
        credentials.setPrivate_key("not-a-valid-key");
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("https://example.com").build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> LocalSignerHelper.createJWT(credentials, request, System.currentTimeMillis()));
        assertEquals("Failed to sign Service Account with private key", exception.getMessage());
    }

    @Test
    void createJwtSuccessTest() {
        GoogleServiceAccountCredentials credentials = GoogleServiceAccountCredentialsExample.exampleObject();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("https://example.com").build();
        long now = 1700000000000L;

        String jwt = LocalSignerHelper.createJWT(credentials, request, now);

        assertNotNull(jwt);

        // Verify JWT has 3 parts
        String[] parts = jwt.split("\\.");
        assertEquals(3, parts.length);

        // Verify header
        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        JsonObject header = JsonParser.parseString(headerJson).getAsJsonObject();
        assertEquals("RS256", header.get("alg").getAsString());
        assertEquals("JWT", header.get("typ").getAsString());

        // Verify payload
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        JsonObject payload = JsonParser.parseString(payloadJson).getAsJsonObject();
        assertEquals("example@example.com", payload.get("iss").getAsString());
        assertEquals("example@example.com", payload.get("sub").getAsString());
        assertEquals("https://oauth2.googleapis.com/token", payload.get("aud").getAsString());
        assertEquals("https://example.com", payload.get("target_audience").getAsString());
        assertEquals(now / 1000, payload.get("iat").getAsLong());
        assertEquals((now + 3600 * 1000) / 1000, payload.get("exp").getAsLong());

        // Verify signature is not empty
        assertTrue(parts[2].length() > 0);
    }
}
