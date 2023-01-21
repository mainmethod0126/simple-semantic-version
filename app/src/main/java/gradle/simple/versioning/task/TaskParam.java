package gradle.simple.versioning.task;

public class TaskParam {
    String javaVersion;
    String major;
    String minor;
    String patch;
    String prereleaseVersion;
    String buildMetadata;

    public TaskParam(String javaVersion, String major, String minor, String patch, String prereleaseVersion,
            String buildMetadata) {
        this.javaVersion = javaVersion;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.prereleaseVersion = prereleaseVersion;
        this.buildMetadata = buildMetadata;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
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
