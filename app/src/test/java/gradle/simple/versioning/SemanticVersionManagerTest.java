package gradle.simple.versioning;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.UUID;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mainmethod0126.gradle.simple.versioning.extension.SimpleSemanticVersionPluginExtension;
import io.github.mainmethod0126.gradle.simple.versioning.task.BuildAndVersioning;
import io.github.mainmethod0126.gradle.simple.versioning.utils.SsvPaths;

class SemanticVersionManagerTest {

    @Test
    @DisplayName("buildAndVersioningTask 가 정상적으로 실행되는지 테스트 합니다.")
    void buildAndVersioningTaskTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        SimpleSemanticVersionPluginExtension.init(project);
        SsvPaths.init(project);

        // when, then
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();
    }

    @Test
    @DisplayName("If the build succeeds, the incremented version is committed.")
    void buildAndVersioningTask_whenBuildSucceed_thenIncreaseCommitTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        SimpleSemanticVersionPluginExtension.init(project);
        SsvPaths.init(project);

        buildAndVersioning.setMajor("++");
        buildAndVersioning.setMinor("++");
        buildAndVersioning.setPatch("++");
        buildAndVersioning.setPr("beta" + UUID.randomUUID().toString());
        buildAndVersioning.setBm("test" + UUID.randomUUID().toString());
        buildAndVersioning.setJavav("11");

        // when
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();
        buildAndVersioning.commit();

        assertTrue(buildAndVersioning.changed());
    }

    @Test
    @DisplayName("If the build fails, the incremented version is not committed.")
    void buildAndVersioningTask_whenBuildFailed_thenNotIncresaseCommitTest() throws IOException {

        // given
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("java");
        BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
                BuildAndVersioning.class);

        SimpleSemanticVersionPluginExtension.init(project);
        SimpleSemanticVersionPluginExtension.getExtension().setDateInBuildPath(false);
        SsvPaths.init(project);

        buildAndVersioning.setMajor("++");
        buildAndVersioning.setMinor("++");
        buildAndVersioning.setPatch("++");
        buildAndVersioning.setPr("beta" + UUID.randomUUID().toString());
        buildAndVersioning.setBm("test" + UUID.randomUUID().toString());
        buildAndVersioning.setJavav("11");

        // when
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();

        // then
        assertFalse(buildAndVersioning.changed());
    }

}
