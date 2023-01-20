package gradle.simple.versioning.task.version;

import java.io.File;
import java.nio.file.Path;

public class SemanticVersionFile {

    private SemanticVersion semanticVersion;
    private File file;

    public SemanticVersionFile(Path versionFilePath) {
        this(new SemanticVersion(), versionFilePath);
    }

    public SemanticVersionFile(SemanticVersion semanticVersion, Path versionFilePath) {
        this.semanticVersion = semanticVersion;
        this.file = versionFilePath.toFile();
    }

    // 리플렉션으로 semanticVersion.getMajor().increase() 를 재정의하는게 과연 옳은 일인가를 고민해야합니다.
    public Major major() {
        return semanticVersion.getMajor();
    }

    public Minor minor() {
        return semanticVersion.getMinor();
    }

    public Patch patch() {
        return semanticVersion.getPatch();
    }

}
