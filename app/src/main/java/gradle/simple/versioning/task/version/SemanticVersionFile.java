package gradle.simple.versioning.task.version;

import java.io.File;
import java.io.FileNotFoundException;
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

        if (this.file.exists()) {

            this.file.get





            new SemanticVersion(null, null, null, null, null);


        }

        if (!this.file.createNewFile()) {
            throw new IOException("failed create file : " + this.file.getPath());
        }
    }

    private void save(JSONObject version) throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.file);) {
            fileWriter.write(version.toString());
        }
    }

    private void load() throws IOException {
        try (FileReader reader = new FileReader("version.json");) {
            JSONObject versionJson = new JSONObject(new JSONTokener(reader));
        
        
        Major major = new Major();
        Minor minor = new Minor();
        Patch patch = new Patch();
        String pr = "";
        String bm = "";


        if (versionJson.get("major") instanceof String) {
            major.set(versionJson.get("major"));
        }


            Major major = 

        }
    }

    public Increaser increase() {
        return new Increaser();
    }

    public Increaser increase(int amount) {
        return new Increaser(amount);
    }

    public class Increaser {

        private int value;

        public Increaser() {
            this.value = 0;
        }

        public Increaser(int value) {
            this.value = value;
        }

        public void major() {
            semanticVersion.getMajor().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void minor() {
            semanticVersion.getMinor().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void patch() {
            semanticVersion.getPatch().increase(value);

            try {
                save(semanticVersion.toJsonObject());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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

}
