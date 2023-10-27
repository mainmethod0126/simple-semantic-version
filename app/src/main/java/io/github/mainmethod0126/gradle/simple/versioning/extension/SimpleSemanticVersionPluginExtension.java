package io.github.mainmethod0126.gradle.simple.versioning.extension;

import org.gradle.api.provider.Property;

public interface SimpleSemanticVersionPluginExtension {
    Property<Boolean> getIsDateInBuildArtifactDirPath();

}