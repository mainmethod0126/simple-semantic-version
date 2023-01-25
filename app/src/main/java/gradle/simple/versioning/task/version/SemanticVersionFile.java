package gradle.simple.versioning.task.version;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.json.JSONObject;
import org.json.JSONTokener;

import gradle.simple.versioning.exception.MinimumLimitException;

public class SemanticVersionFile {

    private SemanticVersion semanticVersion;
    private File file;

    public SemanticVersionFile(Path versionFilePath) throws IOException {
        this.file = versionFilePath.toFile();

        if (this.semanticVersion == null) {
            this.semanticVersion = new SemanticVersion(new Major(), new Minor(), new Patch(), "", "");
        }

        if (this.file.exists()) {

            load();
            return;
        }

        if (!this.file.createNewFile()) {
            throw new IOException("failed create file : " + this.file.getPath());
        }

        save(semanticVersion.toJsonObject());
    }

    private void save(JSONObject version) throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.file);) {
            fileWriter.write(version.toString());
        }
    }

    private void load() throws IOException {
        try (FileReader reader = new FileReader("version.json");) {
            JSONObject versionJson = new JSONObject(new JSONTokener(reader));

            this.semanticVersion.getMajor().set(versionJson.get("major").toString());

            this.semanticVersion.getMinor().set(versionJson.get("minor").toString());

            this.semanticVersion.getPatch().set(versionJson.get("patch").toString());

            this.semanticVersion.setPrereleaseVersion(versionJson.get("prereleaseVersion").toString());

            this.semanticVersion.setBuildMetadata(versionJson.get("buildMetadata").toString());
        }
    }

    public String getFullString() throws IOException {
        load();
        return semanticVersion.getFullString();
    }

    public Increaser increase() {
        return new Increaser();
    }

    public Increaser increase(int amount) {
        return new Increaser(amount);
    }

    public TextVersionSetter set(String value) {
        return new TextVersionSetter(value);
    }

    public class Increaser {

        private int value;

        public Increaser() {
            this.value = 1;
        }

        public Increaser(int value) {
            this.value = value;
        }

        public int major() {
            semanticVersion.getMajor().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return semanticVersion.getMajor().get();
        }

        public int minor() {
            semanticVersion.getMinor().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return semanticVersion.getMinor().get();
        }

        public int patch() {
            semanticVersion.getPatch().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return semanticVersion.getPatch().get();
        }
    }

    public class Decreaser {
        private int value;

        public Decreaser() {
            this.value = 0;
        }

        public Decreaser(int value) {
            this.value = value;
        }

        public void major() {
            try {
                semanticVersion.getMajor().decrease(value);
            } catch (MinimumLimitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void minor() {
            try {
                semanticVersion.getMinor().decrease(value);
            } catch (MinimumLimitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void patch() {
            try {
                semanticVersion.getPatch().decrease(value);
            } catch (MinimumLimitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public class TextVersionSetter {

        private String value;

        public TextVersionSetter(String value) {
            this.value = value;
        }

        public void prereleaseVersion() {
            semanticVersion.setPrereleaseVersion(this.value);
            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public void buildMetadata() {
            semanticVersion.setBuildMetadata(this.value);
            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public int getMajor() {
        return semanticVersion.getMajor().get();
    }

    public int getMinor() {
        return semanticVersion.getMinor().get();
    }

    public int getPatch() {
        return semanticVersion.getPatch().get();
    }

    public String getPrereleaseVersion() {
        return semanticVersion.getPrereleaseVersion();
    }

    public String getBuildMetadata() {
        return semanticVersion.getBuildMetadata();
    }

}
