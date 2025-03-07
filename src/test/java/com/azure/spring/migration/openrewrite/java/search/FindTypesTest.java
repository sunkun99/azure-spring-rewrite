/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.azure.spring.migration.openrewrite.java.search;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

public final class FindTypesTest implements RewriteTest {
    @Test
    void testFindTypes() {
        rewriteRun(
                spec -> spec.recipe(new FindTypes("java.io.*File*",false,"TODO ASA-FileStorageApi: need configuration to use storage")),
                java(
                        """
                                  package org.springframework.samples.petclinic;
                                  import java.io.File;
                                    
                                  public class LocalFile {
                                  
                                      public void test(){
                                          File file = new File("c:\\\\temp\\\\d.text");
                                      }
                                  }
                                """,
                        """
                                package org.springframework.samples.petclinic;
                                import java.io.File;
                                                              
                                public class LocalFile {
                                                              
                                    public void test(){
                                        /*~~(TODO ASA-FileStorageApi: need configuration to use storage)~~>*/File file = new /*~~(TODO ASA-FileStorageApi: need configuration to use storage)~~>*/File("c:\\\\temp\\\\d.text");
                                    }
                                }
                              """
                )
        );
    }
}
