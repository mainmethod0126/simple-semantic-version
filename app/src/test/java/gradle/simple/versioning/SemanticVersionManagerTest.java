package gradle.simple.versioning;

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
    @DisplayName("buildAndVersioningTask 가 정상적으로 실행되는지 테스트 합니다.")
    public void buildAndVersioningTask_increaseTest() throws IOException {

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

        // when, then
        buildAndVersioning.setProject(project);
        buildAndVersioning.doExcute();

        buildAndVersioning.commit();
    }

    // @Test
    // @DisplayName("When I didn't call commit in buildAndVersioningTask , the version file doesn't change.")
    // public void buildAndVersioningTask_increase_not_commit_Test() throws IOException {

    //     // given
    //     Project project = ProjectBuilder.builder().build();
    //     project.getPlugins().apply("java");
    //     BuildAndVersioning buildAndVersioning = project.getTasks().create("BuildAndVersioning",
    //             BuildAndVersioning.class);

    //     buildAndVersioning.setMajor("++");
    //     buildAndVersioning.setMinor("++");
    //     buildAndVersioning.setPatch("++");
    //     buildAndVersioning.setPr("beta" + UUID.randomUUID().toString());
    //     buildAndVersioning.setBm("test" + UUID.randomUUID().toString());

    //     // when
    //     buildAndVersioning.setProject(project);

    //     buildAndVersioning.doExcute();

    //     // then
    //     buildAndVersioning.commit();

    // }

}
