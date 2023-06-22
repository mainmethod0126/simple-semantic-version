package gradle.simple.versioning;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.github.mainmethod0126.gradle.simple.versioning.task.BuildAndVersioning;

public class SemanticVersionManagerTest {

    @Test
    @DisplayName("buildAndVersioningTask 가 정상적으로 실행되는지 테스트 합니다.")
    public void buildAndVersioningTaskTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        // when, then
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();
    }

    @Test
    @DisplayName("If the build succeeds, the incremented version is committed.")
    public void buildAndVersioningTask_whenBuildSucceed_thenIncreaseCommitTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        buildAndVersioning.setMajor("++");
        buildAndVersioning.setMinor("++");
        buildAndVersioning.setPatch("++");
        buildAndVersioning.setPr("beta" + UUID.randomUUID().toString());
        buildAndVersioning.setBm("test" + UUID.randomUUID().toString());

        // when 
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();
        buildAndVersioning.commit();

        assertTrue(buildAndVersioning.changed());
    }

    @Test
    @DisplayName("If the build fails, the incremented version is not committed.")
    public void buildAndVersioningTask_whenBuildFailed_thenNotIncresaseCommitTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        buildAndVersioning.setMajor("++");
        buildAndVersioning.setMinor("++");
        buildAndVersioning.setPatch("++");
        buildAndVersioning.setPr("beta" + UUID.randomUUID().toString());
        buildAndVersioning.setBm("test" + UUID.randomUUID().toString());

        // when
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();

        // then
        assertFalse(buildAndVersioning.changed());
    }

}
