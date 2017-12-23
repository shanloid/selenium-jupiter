/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.handler;

import java.lang.reflect.Parameter;
import java.util.Optional;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import io.github.bonigarcia.AnnotationsReader;

/**
 * Resolver for other drivers.
 *
 * @author Boni Garcia (boni.gg@gmail.com)
 * @since 1.2.0
 */
public class OtherDriverHandler extends DriverHandler {

    static OtherDriverHandler instance;
    AnnotationsReader annotationsReader = new AnnotationsReader();

    public static synchronized OtherDriverHandler getInstance() {
        if (instance == null) {
            instance = new OtherDriverHandler();
        }
        return instance;
    }

    public WebDriver resolve(Parameter parameter,
            Optional<Object> testInstance) {
        WebDriver driver = null;
        try {
            Class<?> type = parameter.getType();
            Optional<Capabilities> capabilities = annotationsReader
                    .getCapabilities(parameter, testInstance);
            if (capabilities.isPresent()) {
                driver = (WebDriver) type
                        .getDeclaredConstructor(Capabilities.class)
                        .newInstance(capabilities.get());
            } else {
                driver = (WebDriver) type.newInstance();
            }
        } catch (Exception e) {
            handleException(e);
        }
        return driver;
    }

}