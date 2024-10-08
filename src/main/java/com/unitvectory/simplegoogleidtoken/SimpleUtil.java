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

import com.google.gson.Gson;

import lombok.experimental.UtilityClass;

/**
 * Internal utility class.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@UtilityClass
class SimpleUtil {

    /**
     * Shared GSO instance.
     */
    static final Gson GSON = new Gson();
}
