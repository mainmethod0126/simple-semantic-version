package gradle.simple.versioning;

import java.io.IOException;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import gradle.simple.versioning.task.BuildAndVersioning;

public class SemanticVersionManager implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        String javav = (project.getProperties().get("javav") == null) ? ""
                : (String) project.getProperties().get("javav");
        String major = (project.getProperties().get("major") == null) ? ""
                : (String) project.getProperties().get("major");
        String minor = (project.getProperties().get("minor") == null) ? ""
                : (String) project.getProperties().get("minor");
        String patch = (project.getProperties().get("patch") == null) ? ""
                : (String) project.getProperties().get("patch");
        String pr = (project.getProperties().get("pr") == null) ? ""
                : (String) project.getProperties().get("pr");
        String bm = (project.getProperties().get("bm") == null) ? ""
                : (String) project.getProperties().get("bm");

        buildAndVersioning.setJavav(javav);
        buildAndVersioning.setMajor(major);
        buildAndVersioning.setMinor(minor);
        buildAndVersioning.setPatch(patch);
        buildAndVersioning.setPr(pr);
        buildAndVersioning.setBm(bm);

        buildAndVersioning.setProject(project);
        try {
            buildAndVersioning.doExcute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
