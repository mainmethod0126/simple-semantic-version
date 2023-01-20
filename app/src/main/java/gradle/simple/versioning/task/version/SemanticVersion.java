package gradle.simple.versioning.task.version;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

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

    private JSONObject toJsonObject() {
        int ma = this.major.get();
        int mi = this.minor.get();
        int pa = this.patch.get();
        String pr = this.prereleaseVersion();
        String bm = this.buildMetadata();

        Map<String, Object> json = new HashMap<>();
        json.put("major", ma);
        json.put("minor", mi);
        json.put("patch", pa);
        json.put("prereleaseVersion", pr);
        json.put("buildMetadata", bm);

        return new JSONObject(json);
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Minor getMinor() {
        return minor;
    }

    public void setMinor(Minor minor) {
        this.minor = minor;
    }

    public Patch getPatch() {
        return patch;
    }

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    public String getPrereleaseVersion() {
        return prereleaseVersion;
    }

    public void setPrereleaseVersion(String prereleaseVersion) {
        this.prereleaseVersion = prereleaseVersion;
    }

    public String getBuildMetadata() {
        return buildMetadata;
    }

    public void setBuildMetadata(String buildMetadata) {
        this.buildMetadata = buildMetadata;
    }

}
