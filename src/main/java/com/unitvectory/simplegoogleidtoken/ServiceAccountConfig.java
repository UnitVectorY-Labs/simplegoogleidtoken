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
 * The interface for loading the service account and utilizing it to sign the
 * JWT that will be exchanged for an ID token.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public interface ServiceAccountConfig {

    /**
     * Takes the request for the signature that includes the target audience and
     * utilizes the local service account to sign the JWT that will be exchanged for
     * an ID token.
     * 
     * @param request the request
     * @return the response
     */
    SimpleResponse signServiceAccountJwt(SimpleRequest request);
}
