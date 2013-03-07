/*
 * Copyright (c) 2010-2013. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.dsl.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Allard Buijze
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true)
public class AxonDslMojo extends AbstractMojo {

    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/axon")
    protected File targetFolder;

    @Parameter(required = true, defaultValue = "${project.build.sourceDirectory}")
    protected File sourceFolder;

    /**
     * specify grammar file encoding; e.g., utf-8
     */
    @Parameter(property = "project.build.sourceEncoding")
    protected String encoding;

    /**
     * The current Maven project.
     */
    @Parameter(property = "project", required = true, readonly = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Scanning folder: " + sourceFolder.getAbsolutePath());
        getLog().info("Writing to folder: " + targetFolder.getAbsolutePath());
        getLog().info("Encoding: " + encoding);
        project.addCompileSourceRoot(targetFolder.getAbsolutePath());
        targetFolder.mkdirs();
        File file = new File(targetFolder, "test.txt");
        try {
            new FileInputStream(file);
            FileOutputStream out = new FileOutputStream(file);
            out.write("testing".getBytes("UTF-8"));
        } catch (Exception e) {
            throw new MojoExecutionException("", e);
        }
    }
}
