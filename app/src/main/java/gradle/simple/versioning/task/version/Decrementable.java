package gradle.simple.versioning.task.version;

import gradle.simple.versioning.exception.MinimumLimitException;

public interface Decrementable {

    public void decrease() throws MinimumLimitException;

}
