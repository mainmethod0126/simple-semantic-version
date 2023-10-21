package io.github.mainmethod0126.gradle.simple.versioning.extension;

import org.gradle.api.Project;

import io.github.mainmethod0126.gradle.simple.versioning.utils.DateUtils;

/**
 * 
 * I wanted to create it as a singleton object, but it seemed like we would
 * always have to pass the 'project' object as an argument when calling
 * {@code getInstance()} from outside. Therefore, I created the object to be
 * conveniently used after calling the initial {@code initExtension()} function.
 * 
 * warn! : This class was not designed with consideration for multi-threading
 * and is not thread-safe. Please be cautious when using it in a multi-threaded
 * environment.
 */
public class SimpleSemanticVersionPluginExtension {

    /**
     * This is not a singleton object, but rather a global variable used for the
     * convenience of users.
     */
    private static SimpleSemanticVersionPluginExtension extension;

    public static void init(Project project) {
        if (extension == null) {
            extension = project.getExtensions().create("ssv",
                    SimpleSemanticVersionPluginExtension.class);
        }
    }

    public static SimpleSemanticVersionPluginExtension getExtension() {
        return extension;
    }

    private String buildDate = DateUtils.getCurrentDate(DateUtils.DateUnit.DAY);
    private boolean isDateInBuildArtifactDirPath = false;
    private String applicationVersion = "0.0.0";

    public boolean isDateInBuildArtifactDirPath() {
        return isDateInBuildArtifactDirPath;
    }

    public void setDateInBuildPath(boolean isDateInBuildArtifactDirPath) {
        this.isDateInBuildArtifactDirPath = isDateInBuildArtifactDirPath;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }
}
