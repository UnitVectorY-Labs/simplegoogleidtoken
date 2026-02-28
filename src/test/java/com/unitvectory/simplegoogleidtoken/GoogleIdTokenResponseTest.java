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

import org.junit.jupiter.api.Test;

/**
 * Test the GoogleIdTokenResponse class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class GoogleIdTokenResponseTest {

    @Test
    void settersAndGettersTest() {
        GoogleIdTokenResponse response = new GoogleIdTokenResponse();
        response.setAccess_token("access_token_value");
        response.setExpires_in(3600);
        response.setToken_type("Bearer");
        response.setId_token("id_token_value");

        assertEquals("access_token_value", response.getAccess_token());
        assertEquals(3600, response.getExpires_in());
        assertEquals("Bearer", response.getToken_type());
        assertEquals("id_token_value", response.getId_token());
    }

    @Test
    void gsonDeserializationTest() {
        String json = "{\"access_token\":\"at\",\"expires_in\":1800,\"token_type\":\"Bearer\",\"id_token\":\"idt\"}";
        GoogleIdTokenResponse response = SimpleUtil.GSON.fromJson(json, GoogleIdTokenResponse.class);

        assertEquals("at", response.getAccess_token());
        assertEquals(1800, response.getExpires_in());
        assertEquals("Bearer", response.getToken_type());
        assertEquals("idt", response.getId_token());
    }
}
