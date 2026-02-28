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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test the ServiceAccountDefaultGoogleCredentialsConfig class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class ServiceAccountDefaultGoogleCredentialsConfigTest {

    @Test
    void nullRequestTest() {
        ServiceAccountDefaultGoogleCredentialsConfig config = new ServiceAccountDefaultGoogleCredentialsConfig();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> config.signServiceAccountJwt(null));
        assertEquals("request cannot be null", exception.getMessage());
    }

    @Test
    void nullTargetAudienceTest() {
        ServiceAccountDefaultGoogleCredentialsConfig config = new ServiceAccountDefaultGoogleCredentialsConfig();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience(null).build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> config.signServiceAccountJwt(request));
        assertEquals("Target audience is required", exception.getMessage());
    }

    @Test
    void emptyTargetAudienceTest() {
        ServiceAccountDefaultGoogleCredentialsConfig config = new ServiceAccountDefaultGoogleCredentialsConfig();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("").build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> config.signServiceAccountJwt(request));
        assertEquals("Target audience is required", exception.getMessage());
    }

    @Test
    void noApplicationDefaultCredentialsTest() {
        ServiceAccountDefaultGoogleCredentialsConfig config = new ServiceAccountDefaultGoogleCredentialsConfig();
        SimpleRequest request = SimpleRequest.builder().withTargetAudience("https://example.com").build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> config.signServiceAccountJwt(request));
        assertEquals("Failed to get GoogleCredentials.getApplicationDefault()", exception.getMessage());
    }
}
