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

import org.junit.jupiter.api.Test;

/**
 * Test the SerivceAccountJsonConfig class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class ServiceAccountJsonConfigTest {

    @Test
    void requestNullTest() {
        ServiceAccountJsonConfig serviceAccountJsonConfig = ServiceAccountJsonConfig.builder().build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> serviceAccountJsonConfig.signServiceAccountJwt(null));
        assertEquals("request cannot be null", exception.getMessage());
    }

    @Test
    void googleIdTokenRequestNullTest() {
        ServiceAccountJsonConfig serviceAccountJsonConfig = ServiceAccountJsonConfig.builder()
                .withGoogleIdTokenRequest(null).build();
        SimpleExchangeException exception = assertThrows(SimpleExchangeException.class,
                () -> serviceAccountJsonConfig.signServiceAccountJwt(
                        SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("googleIdTokenRequest not provided", exception.getMessage());
    }

    @Test
    void serviceAccountJsonNullTest() {
        ServiceAccountJsonConfig serviceAccountJsonConfig = ServiceAccountJsonConfig.builder()
                .withServiceAccountJson(null).build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> serviceAccountJsonConfig.signServiceAccountJwt(
                        SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("serviceAccountJson not provided", exception.getMessage());
    }

    @Test
    void tokenTest() {
        ServiceAccountJsonConfig serviceAccountJsonConfig = ServiceAccountJsonConfig.builder()
                .withServiceAccountJson(GoogleServiceAccountCredentialsExample.exampleJson())
                .withGoogleIdTokenRequest(new GoogleIdTokenRequest() {

                    @Override
                    public SimpleResponse getGoogleIdToken(String serviceAccountJwt) {
                        // This mock example is implementing a passthrough of the JWT
                        return SimpleResponse.builder().withIdToken(serviceAccountJwt).build();
                    }

                }).build();

        SimpleResponse response = serviceAccountJsonConfig
                .signServiceAccountJwt(SimpleRequest.builder().withTargetAudience("https://example.com").build());
        assertNotNull(response);
    }

}
