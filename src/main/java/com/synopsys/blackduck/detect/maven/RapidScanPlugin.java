/*
 * synopsys-detect
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
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
