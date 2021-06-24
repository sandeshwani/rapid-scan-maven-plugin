/*
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.blackduck.detect.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.util.List;

/**
 * Goal which runs Sysnopsys Detect, Rapid Scanning test
 *
 * @phase process-sources
 */
@Mojo(name = "scan")
public class RapidScanPlugin
    extends AbstractMojo
{
    @Override
    public void execute()
        throws MojoExecutionException
    {
        String blackduckUrl = System.getenv("BLACKDUCK_URL");

        if (blackduckUrl == null || blackduckUrl.isEmpty()) {
            throw new MojoExecutionException("BLACKDUCK_URL environment variable not defined");
        }

        String blackduckApiToken = System.getenv("BLACKDUCK_API_TOKEN");

        if (blackduckApiToken == null || blackduckApiToken.isEmpty()) {
            throw new MojoExecutionException("BLACKDUCK_API_TOKEN environment variable not defined");
        }

        String curlCommand = "curl -s -L https://detect.synopsys.com/detect7.sh";
        ProcessBuilder curlProcessBuilder = new ProcessBuilder(curlCommand.split(" "));

        String bashCommand = "bash -s -- --detect.blackduck.scan.mode=RAPID"
                + " --detect.tools=DETECTOR --detect.required.dectector.types=MAVEN"
                + " --detect.bom.aggregate.name=aggregated.bdio";
        ProcessBuilder bashProcessBuilder = new ProcessBuilder(bashCommand.split(" "));

        try {
            List<Process> processes =
                    ProcessBuilder.startPipeline(
                            List.of(curlProcessBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
                                        .redirectError(ProcessBuilder.Redirect.INHERIT),
                                    bashProcessBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
                                         .redirectError(ProcessBuilder.Redirect.INHERIT)));

            for (Process process : processes) {
                process.waitFor();
            }
        }
        catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("Error running Synopsys Detect", e);
        }
    }
}
