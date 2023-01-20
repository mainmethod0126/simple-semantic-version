package gradle.simple.versioning.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.ObjectMapper;
import org.gradle.internal.impldep.com.google.gson.JsonObject;
import org.gradle.internal.impldep.com.google.gson.stream.JsonReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import gradle.simple.versioning.task.version.Major;
import gradle.simple.versioning.task.version.Minor;
import gradle.simple.versioning.task.version.Patch;
import gradle.simple.versioning.task.version.SemanticVersion;

public class BuildAndVersioning extends DefaultTask {

    @Inject
    private Project project;

    @Input
    private String javav = "";

    @Input
    private String major = "";

    @Input
    private String minor = "";

    @Input
    private String patch = "";

    @Input
    private String pr = "";

    @Input
    private String bm = "";

    /**
     * default version 정보가 적혀있는 version.json 파일을 생성합니다.
     * 
     * @return
     */
    public File createFileDefaulVersionJson() {

        String defaultPath = "version.json";

        File file = new File(defaultPath);

        JSONObject defaultVersionJson = new JSONObject();
        defaultVersionJson.put("major", "0");
        defaultVersionJson.put("minor", "0");
        defaultVersionJson.put("patch", "0");
        defaultVersionJson.put("prereleaseVersion", "");
        defaultVersionJson.put("buildMetadata", "");

        try {
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(defaultVersionJson.toString());

            fileWriter.close();
        } catch (IOException e) {
            throw new IllegalStateException("failed create Object" + e.getMessage(), e);
        }

        return file;
    }

    public SemanticVersion readVersion(TaskParam taskParam) {

        Major major = new Major(0);
        Minor minor = new Minor(0);
        Patch patch = new Patch(1);
        String prereleaseVersion = "";
        String buildMetadata = "";

        File file = new File("version.json");

        if (!file.exists()) {
            file = createFileDefaulVersionJson();
        }

        FileReader reader = new FileReader("version.json");
        JSONObject versionJson = new JSONObject(new JSONTokener(reader));

        reader.close();

        if (taskParam.getMajor().isEmpty()) {
            taskParam.getMajor() = versionJson.getString("major");
        } else if (taskParam.getMajor().equalsIgnoreCase("++")) {
            taskParam.getMajor() = versionIncreaser(versionJson.getString("major") + "");
        }

        if (taskParam.getMinor().isEmpty()) {
            taskParam.getMinor() = versionJson.getString("minor");
        } else if (taskParam.getMinor().equalsIgnoreCase("++")) {
            taskParam.getMinor() = versionIncreaser(versionJson.getString("minor") + "");
        }

        if (taskParam.getPatch().isEmpty()) {
            taskParam.getPatch() = versionJson.getString("patch");
        } else if (taskParam.getPatch().equalsIgnoreCase("++")) {
            taskParam.getPatch() = versionIncreaser(versionJson.getString("patch") + "");
        }

        String delimiterIncludedprereleaseVersion = prereleaseVersion;
        if (!taskParam.getPrereleaseVersion().isEmpty()) {
            delimiterIncludedprereleaseVersion = "-" + prereleaseVersion;
        }

        String delimiterIncludedBuildMetadata = buildMetadata;
        if (!taskParam.getBuildMetadata().isEmpty()) {
            delimiterIncludedBuildMetadata = "+" + buildMetadata;
        }
    }

    @TaskAction
    public void doExcute() throws IOException {

        String sourceCompatibility = this.javav;

        if (sourceCompatibility.isEmpty()) {
            sourceCompatibility = System.getProperty("java.version");
        }

        project.setProperty("sourceCompatibility", sourceCompatibility);

        SemanticVersion semanticVersion = readVersion(
                new TaskParam(this.major, this.minor, this.patch, this.pr, this.bm));

        String applicationVersion = tempMajor + "." + tempMinor + "." + tempPatch + delimiterIncludedprereleaseVersion
                + delimiterIncludedBuildMetadata;

        String buildDirPath = "./dist/${(new Date()).format('yyyy-MM-dd')}" + "/" + sourceCompatibility + "/"
                + applicationVersion + "/";

        project.setProperty("version", applicationVersion);

        project.file(buildDirPath).mkdir();

        Task task = project.getTasks().getByName("jar");
        Jar jar = (Jar) task;
        jar.getDestinationDirectory().set(new File(buildDirPath));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> attributes = Map.of("Application-Version", applicationVersion, "Build-Date",
                dateFormat.format(new Date()));
        jar.getManifest().attributes(attributes);
        this.dependsOn(jar);

        writeVersion(tempMajor, tempMinor, tempPatch, prereleaseVersion, buildMetadata);

        System.out.println(
                "----------------------------------------------------------------------------------------------------");
        System.out.println(
                "--------------------------------|                                 |---------------------------------");
        System.out.println(
                "--------------------------------|           Build Result          |---------------------------------");
        System.out.println(
                "--------------------------------|                                 |---------------------------------");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
        System.out.println("Build version : " + applicationVersion);
        System.out.println("Build result path : " + buildDirPath);
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");

    }

    /**
     * version.json 파일에 json 형식으로 version을 입력합니다.
     * 
     * @param major
     * @param minor
     * @param patch
     * @param prereleaseVersion
     * @param buildMetadata
     * @throws FileNotFoundException
     */
    void writeVersion(String major, String minor, String patch, String prereleaseVersion, String buildMetadata)
            throws FileNotFoundException {

        String tempPrereleaseVersion = "\"\"";
        if (prereleaseVersion != null && !prereleaseVersion.isEmpty()) {
            tempPrereleaseVersion = "\"" + prereleaseVersion + "\"";
        }

        String tempBuildMetadata = "\"\"";
        if (buildMetadata != null && !buildMetadata.isEmpty()) {
            tempBuildMetadata = "\"" + buildMetadata + "\"";
        }

        File versionJosnFile = new File("version.json");
        if (!versionJosnFile.exists()) {
            throw new FileNotFoundException("version.json file does not exist");
        }

        JSONObject json = new JSONObject();
        json.put("major", "\"" + major + "\"");
        json.put("minor", "\"" + minor + "\"");
        json.put("patch", "\"" + patch + "\"");
        json.put("prereleaseVersion", "\"" + tempPrereleaseVersion + "\"");
        json.put("buildMetadata", "\"" + tempBuildMetadata + "\"");

        try {
            FileWriter fileWriter = new FileWriter(versionJosnFile);
            fileWriter.write(json.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 버전 값을 1만큼 증가시킵니다.
     * 
     * @param prevVersion
     * @return
     */
    String versionIncreaser(String prevVersion) {
        Integer versionNumber = Integer.valueOf(prevVersion);
        versionNumber++;
        return intToString(versionNumber);
    }

    /**
     * 정수값을 문자열로 변환합니다.
     * 
     * @param number
     * @return
     */
    String intToString(Integer number) {
        return number + "";
    }

    /**
     * 사용자에게 버전 증가 여부를 입력받아 버전을 증가시키거나 그대로 유지합니다.
     * 
     * @param versionName
     * @param oldVersion
     * @return
     */
    String inputVersionIncrement(String versionName, String oldVersion) {
        System.out
                .println("Enter 'Y' if you want to increment the " + versionName + " version, otherwise press any key");
        Scanner sc = new Scanner(System.in);
        String inputFlag = sc.next();
        String nextVersion;
        if (inputFlag.equalsIgnoreCase("Y")) {
            nextVersion = String.valueOf(Integer.valueOf(oldVersion) + 1);
            System.out.println("version changed");
            System.out.println(versionName + " version : " + oldVersion + " ---> " + nextVersion);
        } else {
            nextVersion = oldVersion;
            System.out.println("version not change");
            System.out.println(versionName + " version : " + oldVersion);
        }
        sc.close();
        return nextVersion;
    }

    public Project getProject() {
        return project;
    }

    public String getJavav() {
        return javav;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public String getPatch() {
        return patch;
    }

    public String getPr() {
        return pr;
    }

    public String getBm() {
        return bm;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setJavav(String javav) {
        this.javav = javav;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

}
