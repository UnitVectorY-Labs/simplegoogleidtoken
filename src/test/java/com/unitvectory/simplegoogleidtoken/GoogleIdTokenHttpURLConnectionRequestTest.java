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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

/**
 * Test the GoogleIdTokenHttpURLConnectionRequest class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class GoogleIdTokenHttpURLConnectionRequestTest {

    private HttpServer server;

    private int port;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void successfulTokenExchangeTest() {
        String responseJson = "{\"access_token\":\"at\",\"expires_in\":3600,\"token_type\":\"Bearer\",\"id_token\":\"test-id-token\"}";
        server.createContext("/token", exchange -> {
            byte[] responseBytes = responseJson.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        });
        server.start();

        GoogleIdTokenHttpURLConnectionRequest request = new GoogleIdTokenHttpURLConnectionRequest(
                "http://localhost:" + port + "/token");
        SimpleResponse response = request.getGoogleIdToken("test-jwt");

        assertNotNull(response);
        assertEquals("test-id-token", response.getIdToken());
    }

    @Test
    void nonOkResponseCodeTest() {
        server.createContext("/token", exchange -> {
            exchange.sendResponseHeaders(400, -1);
        });
        server.start();

        GoogleIdTokenHttpURLConnectionRequest request = new GoogleIdTokenHttpURLConnectionRequest(
                "http://localhost:" + port + "/token");
        SimpleExchangeException exception = assertThrows(SimpleExchangeException.class,
                () -> request.getGoogleIdToken("test-jwt"));
        assertEquals("Failed to get ID token. Response Code: 400", exception.getMessage());
    }

    @Test
    void invalidJsonResponseTest() {
        server.createContext("/token", exchange -> {
            byte[] responseBytes = "not-valid-json".getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        });
        server.start();

        GoogleIdTokenHttpURLConnectionRequest request = new GoogleIdTokenHttpURLConnectionRequest(
                "http://localhost:" + port + "/token");
        SimpleExchangeException exception = assertThrows(SimpleExchangeException.class,
                () -> request.getGoogleIdToken("test-jwt"));
        assertEquals("Failed to parse ID token response", exception.getMessage());
    }

    @Test
    void serverErrorResponseCodeTest() {
        server.createContext("/token", exchange -> {
            exchange.sendResponseHeaders(500, -1);
        });
        server.start();

        GoogleIdTokenHttpURLConnectionRequest request = new GoogleIdTokenHttpURLConnectionRequest(
                "http://localhost:" + port + "/token");
        SimpleExchangeException exception = assertThrows(SimpleExchangeException.class,
                () -> request.getGoogleIdToken("test-jwt"));
        assertEquals("Failed to get ID token. Response Code: 500", exception.getMessage());
    }

    @Test
    void defaultConstructorTest() {
        GoogleIdTokenHttpURLConnectionRequest request = new GoogleIdTokenHttpURLConnectionRequest();
        assertNotNull(request);
    }
}
