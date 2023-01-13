package gradle.simple.versioning.task.version;

import java.io.File;
import java.io.FileNotFoundException;

import javax.annotation.Nonnull;

import org.gradle.internal.impldep.org.apache.commons.io.FileExistsException;
import org.json.JSONObject;

import gradle.simple.versioning.exception.UnwritableFileException;

public class SemanticVersion {

    private Major major;
    private Minor minor;
    private Patch patch;
    private String prereleaseVersion;
    private String buildMetadata;

    public Major major() {
        return this.major;
    }

    public Minor minor() {
        return this.minor;
    }

    public Patch patch() {
        return this.patch;
    }

    public String prereleaseVersion() {
        return this.prereleaseVersion;
    }

    public String buildMetadata() {
        return this.buildMetadata;
    }

    public void saveToJsonFile(@Nonnull File file)
            throws FileExistsException, FileNotFoundException, UnwritableFileException {

        if (!file.exists()) {
            throw new FileExistsException(file);
        }

        if (!file.isFile()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " is not File");
        }

        if (!file.canWrite()) {
            throw new UnwritableFileException(file.getAbsolutePath() + " is unwritable file");
        }

    }

    private JSONObject versionToJsonObject() {
        int ma = this.major.getValue();
        int mi = this.minor.getValue();
        int pa = this.patch.getValue();
        String pr = this.prereleaseVersion();
        String bm = this.buildMetadata();

    }

    // public void jsonToFile(String json) {

    // }

}
