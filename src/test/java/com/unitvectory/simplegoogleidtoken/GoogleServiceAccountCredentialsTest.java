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
 * Test the GoogleServiceAccountCredentials class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class GoogleServiceAccountCredentialsTest {

    @Test
    void settersAndGettersTest() {
        GoogleServiceAccountCredentials credentials = new GoogleServiceAccountCredentials();
        credentials.setType("service_account");
        credentials.setProject_id("my-project");
        credentials.setPrivate_key_id("key-id-123");
        credentials.setPrivate_key("-----BEGIN PRIVATE KEY-----\ntest\n-----END PRIVATE KEY-----");
        credentials.setClient_email("test@my-project.iam.gserviceaccount.com");
        credentials.setClient_id("123456789");
        credentials.setAuth_uri("https://accounts.google.com/o/oauth2/auth");
        credentials.setToken_uri("https://oauth2.googleapis.com/token");
        credentials.setAuth_provider_x509_cert_url("https://www.googleapis.com/oauth2/v1/certs");
        credentials.setClient_x509_cert_url("https://www.googleapis.com/robot/v1/metadata/x509/test");

        assertEquals("service_account", credentials.getType());
        assertEquals("my-project", credentials.getProject_id());
        assertEquals("key-id-123", credentials.getPrivate_key_id());
        assertEquals("-----BEGIN PRIVATE KEY-----\ntest\n-----END PRIVATE KEY-----", credentials.getPrivate_key());
        assertEquals("test@my-project.iam.gserviceaccount.com", credentials.getClient_email());
        assertEquals("123456789", credentials.getClient_id());
        assertEquals("https://accounts.google.com/o/oauth2/auth", credentials.getAuth_uri());
        assertEquals("https://oauth2.googleapis.com/token", credentials.getToken_uri());
        assertEquals("https://www.googleapis.com/oauth2/v1/certs", credentials.getAuth_provider_x509_cert_url());
        assertEquals("https://www.googleapis.com/robot/v1/metadata/x509/test", credentials.getClient_x509_cert_url());
    }

    @Test
    void gsonDeserializationTest() {
        String json = "{\"type\":\"service_account\",\"project_id\":\"proj\",\"private_key_id\":\"pkid\","
                + "\"private_key\":\"pk\",\"client_email\":\"ce\",\"client_id\":\"cid\","
                + "\"auth_uri\":\"au\",\"token_uri\":\"tu\","
                + "\"auth_provider_x509_cert_url\":\"apcu\",\"client_x509_cert_url\":\"cccu\"}";
        GoogleServiceAccountCredentials credentials = SimpleUtil.GSON.fromJson(json,
                GoogleServiceAccountCredentials.class);

        assertEquals("service_account", credentials.getType());
        assertEquals("proj", credentials.getProject_id());
        assertEquals("pkid", credentials.getPrivate_key_id());
        assertEquals("pk", credentials.getPrivate_key());
        assertEquals("ce", credentials.getClient_email());
        assertEquals("cid", credentials.getClient_id());
        assertEquals("au", credentials.getAuth_uri());
        assertEquals("tu", credentials.getToken_uri());
        assertEquals("apcu", credentials.getAuth_provider_x509_cert_url());
        assertEquals("cccu", credentials.getClient_x509_cert_url());
    }
}
