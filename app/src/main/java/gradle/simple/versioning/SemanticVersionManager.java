package gradle.simple.versioning;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import gradle.simple.versioning.task.BuildAndVersioning;

public class SemanticVersionManager implements Plugin<Project> {
    public void apply(Project project) {
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);
        project.getTasks().getByName("assemble").dependsOn(buildAndVersioning);
    }

}
