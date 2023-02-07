package io.github.mainmethod0126.gradle.simple.versioning.task;

import java.nio.file.Path;

public class Artifact {
    Path path;
    String version;

    public Artifact() {
    }

    public Artifact(Path path, String version) {
        this.path = path;
        this.version = version;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
