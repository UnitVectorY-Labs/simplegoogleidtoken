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

import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Helper class to sign a JWT with a Service Account private key.
 * 
 * This implementation is designed to avoid as many dependencies as possible,
 * but it does require GSON.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@UtilityClass
class LocalSignerHelper {

    private static final String SERVICE_ACCOUNT_TOKEN_AUDIENCE = "https://oauth2.googleapis.com/token";

    private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";

    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

    static String createJWT(@NonNull GoogleServiceAccountCredentials serviceAccountInfo,
            @NonNull SimpleRequest simpleRequest, long currentTimeMillis) {

        Gson gson = SimpleUtil.GSON;

        String targetAudience = simpleRequest.getTargetAudience();
        if (targetAudience == null || targetAudience.isEmpty()) {
            throw new SimpleSignException("Target audience is required");
        }

        Map<String, Object> headerMap = new TreeMap<>();
        headerMap.put("alg", "RS256");
        headerMap.put("typ", "JWT");
        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(gson.toJson(headerMap).getBytes());

        Map<String, Object> payloadMap = new TreeMap<>();
        payloadMap.put("iss", serviceAccountInfo.getClient_email());
        payloadMap.put("sub", serviceAccountInfo.getClient_email());
        payloadMap.put("aud", SERVICE_ACCOUNT_TOKEN_AUDIENCE);
        payloadMap.put("iat", currentTimeMillis / 1000);
        payloadMap.put("exp", (currentTimeMillis + 3600 * 1000) / 1000);
        payloadMap.put("target_audience", targetAudience);
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(gson.toJson(payloadMap).getBytes());

        String signatureInput = header + "." + payload;
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signWithPrivateKey(signatureInput.getBytes(), serviceAccountInfo.getPrivate_key()));

        return signatureInput + "." + signature;
    }

    private static byte[] signWithPrivateKey(byte[] data, String privateKeyPem) {
        try {
            String privateKeyPEM = privateKeyPem.replace(BEGIN_PRIVATE_KEY, "")
                    .replace(END_PRIVATE_KEY, "").replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            PrivateKey privateKey = java.security.KeyFactory.getInstance("RSA").generatePrivate(spec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data);

            return signature.sign();
        } catch (Exception e) {
            throw new SimpleSignException("Failed to sign Service Account with private key", e);
        }
    }
}
