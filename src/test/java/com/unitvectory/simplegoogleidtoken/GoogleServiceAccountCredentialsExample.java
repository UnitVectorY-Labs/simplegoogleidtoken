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

/**
 * Produces example implementation of GoogleIdTokenResponse class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
class GoogleServiceAccountCredentialsExample {

    static GoogleServiceAccountCredentials exampleObject() {
        GoogleServiceAccountCredentials credentials = new GoogleServiceAccountCredentials();
        credentials.setClient_email("example@example.com");

        // This private key isn't used for anything outside of this test case
        credentials.setPrivate_key("-----BEGIN PRIVATE KEY-----\n" + //
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZp3V6pE2PYIXA\n" + //
                "P/BWWFbtmH2Jw035Y79gg7Je2hWXI5qC9V0f4v375fnYCXp6uZfSVlk8IxOe9YeE\n" + //
                "z7tMJtlZk7KJ/3WNdDeF5OMjgK/lI1iKIFSKJdEEY6wlNf3TGGbI79HTiuHcE2qg\n" + //
                "oahHqBvxlh7ikvsrLN6maj9ao05ehBF+2tJnVoXhDxpBRAmZBg/fGyV8/MkAmsca\n" + //
                "bqtiVDaHNejsgGvpbVfXwLiDtzMCStXJm8tn/LoOG5R7ijs5cnBkOWwrsa0+F7aJ\n" + //
                "cEYM97V1OjH8RwwrONmgKkdX+MdkntgqYGOYjotbMFrLIqAap0Um3PU+wsum2aNv\n" + //
                "hwWaJLoVAgMBAAECggEAIR3bjyRMZyOJkEb3mNSPX8QnAoMqkh6yh626ywC7+Ig+\n" + //
                "7AzQFmhJD3qgn7RjRoIru7TKpNB2kSMgIzrWhnI1FD5mE6IQhv1OXjhX0DYTMxKs\n" + //
                "KZEQR3ohE44b8yUw4Md4l8RAeBSN223/5EETUOZlp2sQmwrMwaB5GGr73qAl6u68\n" + //
                "iy4WQMJvXEYvDcqhhyCIoEPhoB7NZ+f1o6uZdiU38OTpbAJrNqKd5OOuXDviHnHI\n" + //
                "CsP0YDLfjduBfA6V41Q/v4tVu53oV4S6ZMhK04rlTufql/7GJzR7RSpxuYcyUDzQ\n" + //
                "ysEDjwdY5zu7whNinEjYDp/UQm6+prcPZV/aisn2wQKBgQDIa53La8gaFbBr/KlQ\n" + //
                "ckNTOIwoLto1vczP26vv7/4PjG3EM3OqCfsPodv/njY5QCJ9xuLpOS6KPTUYAnhT\n" + //
                "8JGVk5cSGEQuF0wwZNW0/2bPUzI/O7LouSFlGZz2H2z96mcXbli/iHA0MhPWb2es\n" + //
                "eRzHFqMHLvbgJpkwLC0HRn7cZQKBgQDEQ8fnH6WLhDlEXtp+1UZI3FdQmkQKYGBJ\n" + //
                "yb+czcJeU/Q7r++4AzCXa79KuQ9Cib1eJQyF0hjhPFZzDnIfy+lfH4anTXwtg4ih\n" + //
                "WxcDKf1DsStjuFgALUzJAeiOEDQkisfa/FZBtC1+R5DtTJN7yKBAxyvCV5XRjq8B\n" + //
                "BTM+/9DT8QKBgQCGcDG7t0PVw4DBHw2QCeWBWGgjuDHlZLiTIV53Jzv7wA4NUxoe\n" + //
                "EG3ZcZB6Ke1jf9LjHdst6I6AakhIH7gJTlDJzePDXoW1iSkxOSJlNAEJx1voMKp1\n" + //
                "E2IESCcSrcfVlzup6vobGHFzgbfu2nHnrqIQJe4brnQprZqKRse5np8QRQKBgQCD\n" + //
                "0YaFDDNS1lbkPdUAlNwk+aDHaSVD1uE1DgTdGzFty3NhV1RpQfMz8FFFra+7H+oJ\n" + //
                "Plb/1lGRjcX+dvhdtMYkdncpDRjXNN3iidyV6nDJM0iSLHEmhUZqRxbbu7RZysBa\n" + //
                "q1p/vLVFeGm1h9YSsHg5qcwRNmRQoEWYKNb3fDfU8QKBgGvdptmQR9vs7LEWboJ6\n" + //
                "A5wBUjCsvMafSpP2uH992HlER0nHa6fKVmxXxxj4FhAGxoOBEy6+mgNfENt28RoW\n" + //
                "/fxysnYRmpS+TUTPQfKoXYLLYDR5jrfvJhdNjcmSbRNCmAnsG7antvpVcyXwibWs\n" + //
                "Yz8A+uGKQX7kddy/lYvVPM0L\n" + //
                "-----END PRIVATE KEY-----");
        return credentials;
    }

    static String exampleJson() {
        return SimpleUtil.GSON.toJson(exampleObject());
    }
}
