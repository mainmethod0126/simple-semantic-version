package gradle.simple.versioning.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.internal.impldep.org.apache.commons.io.FileExistsException;
import org.json.JSONObject;

import gradle.simple.versioning.task.version.SemanticVersionFile;
import gradle.simple.versioning.utils.DateUtils;
import gradle.simple.versioning.utils.NumberUtils;

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

    private SemanticVersionFile semanticVersionFile;

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
        defaultVersionJson.put("patch", "1");
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

    public void taskParamResolve(final TaskParam taskParam) {

        if (!taskParam.getMajor().isEmpty() && taskParam.getMajor().equalsIgnoreCase("++")) {
            semanticVersionFile.increase().major();
        } else if (!taskParam.getMajor().isEmpty() && NumberUtils.isPositiveInteger(taskParam.getMajor())) {
            semanticVersionFile.increase(Integer.valueOf(taskParam.getMajor())).major();
        }

        if (!taskParam.getMinor().isEmpty() && taskParam.getMinor().equalsIgnoreCase("++")) {
            semanticVersionFile.increase().minor();
        } else if (!taskParam.getMinor().isEmpty() && NumberUtils.isPositiveInteger(taskParam.getMinor())) {
            semanticVersionFile.increase(Integer.valueOf(taskParam.getMinor())).minor();
        }

        if (!taskParam.getPatch().isEmpty() && taskParam.getPatch().equalsIgnoreCase("++")) {
            semanticVersionFile.increase().patch();
        } else if (!taskParam.getPatch().isEmpty() && NumberUtils.isPositiveInteger(taskParam.getPatch())) {
            semanticVersionFile.increase(Integer.valueOf(taskParam.getPatch())).patch();
        }

        if (!taskParam.getPrereleaseVersion().isEmpty()) {
            semanticVersionFile.set("").prereleaseVersion();
        }

        if (!taskParam.getBuildMetadata().isEmpty()) {
            semanticVersionFile.set("").buildMetadata();
        }
    }

    private void init() throws IOException {

        Path versionFilePath = Paths.get("version.json");
        semanticVersionFile = new SemanticVersionFile(versionFilePath);
    }

    private void createArtifacts(String sourceCompatibility, String applicationVersion) throws IOException {

        if (sourceCompatibility == null) {
            throw new NullPointerException("sourceCompatibility is null");
        }

        if (applicationVersion == null) {
            throw new NullPointerException("applicationVersion is null");
        }

        String buildDate = DateUtils.getCurrentDate(DateUtils.DateUnit.DAY);

        Path buildDirPath = createBuildDirPath(System.getProperty("user.dir"), buildDate, sourceCompatibility,
                applicationVersion);

        project.setProperty("version", applicationVersion);

        File buildDir = new File(buildDirPath.toAbsolutePath().toString());

        if (!buildDir.mkdirs()) {
            throw new FileExistsException(buildDirPath + " Failed Create Build Directory");
        }

        setJar(buildDirPath.toAbsolutePath().toString(), applicationVersion, buildDate);

    }

    private Path createBuildDirPath(String rootPath, String buildDate, String sourceCompatibility,
            String applicationVersion) {

        return Paths.get(rootPath, buildDate, sourceCompatibility, applicationVersion);
    }

    private void setJar(String buildDirPath, String applicationVersion, String buildDate) {
        Task task = project.getTasks().getByName("jar");
        Jar jar = (Jar) task;
        jar.getDestinationDirectory().set(new File(buildDirPath));
        Map<String, String> attributes = Map.of("Application-Version", applicationVersion, "Build-Date", buildDate);
        jar.getManifest().attributes(attributes);
        this.dependsOn(jar);
    }

    @TaskAction
    public void doExcute() throws IOException {

        init();

        String sourceCompatibility = this.javav;

        // set default java version
        if (sourceCompatibility.isEmpty()) {
            sourceCompatibility = System.getProperty("java.version");
        }

        project.setProperty("sourceCompatibility", sourceCompatibility);

        TaskParam userInputVersion = new TaskParam(this.javav, this.major, this.minor, this.patch, this.pr, this.bm);
        taskParamResolve(userInputVersion);

        String applicationVersion = semanticVersionFile.getFullString();

        createArtifacts(sourceCompatibility, applicationVersion);

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
     * @throws IOException
     */
    void writeVersion(String major, String minor, String patch, String prereleaseVersion, String buildMetadata)
            throws IOException {

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

        try (FileWriter fileWriter = new FileWriter(versionJosnFile);) {
            fileWriter.write(json.toString());
            fileWriter.flush();
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
