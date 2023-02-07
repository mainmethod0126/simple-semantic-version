package io.github.mainmethod0126.gradle.simple.versioning.task.version;

import io.github.mainmethod0126.gradle.simple.versioning.exception.MinimumLimitException;

public interface Decrementable {

    public void decrease() throws MinimumLimitException;

}
