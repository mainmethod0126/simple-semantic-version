package io.github.mainmethod0126.gradle.simple.versioning.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.gradle.api.Project;

import io.github.mainmethod0126.gradle.simple.versioning.extension.SimpleSemanticVersionPluginExtension;

public class SsvPaths {

    private SsvPaths() {
        throw new IllegalStateException("Utility class");
    }

    private static Project project;

    public static void init(Project pj) {
        project = pj;
    }

    public static Path getBuildDir() {
        if (SimpleSemanticVersionPluginExtension.getExtension().isDateInBuildArtifactDirPath()) {
            return Paths.get(project.getProjectDir().toString(), "dist",
                    SimpleSemanticVersionPluginExtension.getExtension().getBuildDate(),
                    SimpleSemanticVersionPluginExtension.getExtension().getApplicationVersion());
        } else {
            return Paths.get(project.getProjectDir().toString(), "dist",
                    SimpleSemanticVersionPluginExtension.getExtension().getApplicationVersion());
        }

    }

}
