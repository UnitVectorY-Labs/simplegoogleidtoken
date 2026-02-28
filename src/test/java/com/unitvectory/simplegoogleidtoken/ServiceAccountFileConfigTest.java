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

import java.io.File;
import java.io.FileWriter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test the ServiceAccountFileConfig class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class ServiceAccountFileConfigTest {

    @Test
    void requestNullTest() {
        ServiceAccountFileConfig config = ServiceAccountFileConfig.builder().build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> config.signServiceAccountJwt(null));
        assertEquals("request not provided", exception.getMessage());
    }

    @Test
    void googleIdTokenRequestNullTest() {
        ServiceAccountFileConfig config = ServiceAccountFileConfig.builder()
                .withGoogleIdTokenRequest(null).build();
        SimpleExchangeException exception = assertThrows(SimpleExchangeException.class,
                () -> config.signServiceAccountJwt(
                        SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("googleIdTokenRequest not provided", exception.getMessage());
    }

    @Test
    void filePathNullTest() {
        ServiceAccountFileConfig config = ServiceAccountFileConfig.builder()
                .withFilePath(null).build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> config.signServiceAccountJwt(
                        SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("filePath not provided", exception.getMessage());
    }

    @Test
    void fileNotFoundTest() {
        ServiceAccountFileConfig config = ServiceAccountFileConfig.builder()
                .withFilePath("/nonexistent/path/to/file.json").build();
        SimpleSignException exception = assertThrows(SimpleSignException.class,
                () -> config.signServiceAccountJwt(
                        SimpleRequest.builder().withTargetAudience("https://example.com").build()));
        assertEquals("Error reading service account file", exception.getMessage());
    }

    @Test
    void tokenTest(@TempDir File tempDir) throws Exception {
        // Write a temporary service account JSON file
        File tempFile = new File(tempDir, "service-account.json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(GoogleServiceAccountCredentialsExample.exampleJson());
        }

        ServiceAccountFileConfig config = ServiceAccountFileConfig.builder()
                .withFilePath(tempFile.getAbsolutePath())
                .withGoogleIdTokenRequest(new GoogleIdTokenRequest() {
                    @Override
                    public SimpleResponse getGoogleIdToken(String serviceAccountJwt) {
                        return SimpleResponse.builder().withIdToken(serviceAccountJwt).build();
                    }
                }).build();

        SimpleResponse response = config
                .signServiceAccountJwt(SimpleRequest.builder().withTargetAudience("https://example.com").build());
        assertNotNull(response);
        assertNotNull(response.getIdToken());
    }
}
