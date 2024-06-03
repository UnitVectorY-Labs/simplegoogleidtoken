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
 * Test the SimpleGoogleIdToken class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class SimpleGoogleIdTokenTest {

    @Test
    public void nullRequestTest() {
        SimpleGoogleIdToken simpleGoogleIdToken = SimpleGoogleIdToken.builder().withServiceAccountConfig(null).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> simpleGoogleIdToken.getIdToken(null));
        assertEquals("Request cannot be null", exception.getMessage());
    }

    @Test
    public void nullConfigTest() {
        SimpleGoogleIdToken simpleGoogleIdToken = SimpleGoogleIdToken.builder().withServiceAccountConfig(null).build();
        SimpleSignException exception = assertThrows(SimpleSignException.class, () -> simpleGoogleIdToken
                .getIdToken(SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("serviceAccountConfig cannot be null", exception.getMessage());
    }

    @Test
    public void mockTest() {
        SimpleGoogleIdToken simpleGoogleIdToken = SimpleGoogleIdToken.builder()
                .withServiceAccountConfig(new ServiceAccountConfig() {

                    @Override
                    public SimpleResponse signServiceAccountJwt(SimpleRequest request) {
                        return SimpleResponse.builder().withIdToken("mock").build();
                    }
                }).build();
        SimpleResponse response = simpleGoogleIdToken
                .getIdToken(SimpleRequest.builder().withTargetAudience("https://example.com").build());
        assertNotNull(response);
        assertEquals("mock", response.getIdToken());
    }
}
