package gradle.simple.versioning;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SemanticVersionManager implements Plugin<Project> {
    public void apply(Project project) {
        System.out.println("Hello, World!");
    }


}
