package com.unitvectory.simplegoogleidtoken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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
