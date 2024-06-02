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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * The request for a Google ID token which specifies the target audience.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Getter(AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
public class SimpleRequest {

    /**
     * The target audience for the ID token.
     */
    private final String targetAudience;
}
