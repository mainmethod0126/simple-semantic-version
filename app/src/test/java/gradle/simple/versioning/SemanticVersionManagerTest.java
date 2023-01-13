package gradle.simple.versioning;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import gradle.simple.versioning.task.BuildAndVersioning;

public class SemanticVersionManagerTest {

    @Test
    public void testApply() {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        try {
            buildAndVersioning.setProject(project);
            buildAndVersioning.doExcute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertTrue(project.getTasks().getByName("BuildAndVersioning") instanceof SemanticVersionManager);
        assertTrue(project.getTasks().getByName("build").getDependsOn().contains("myCustomTask"));
    }

}
