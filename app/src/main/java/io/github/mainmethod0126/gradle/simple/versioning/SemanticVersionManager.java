package io.github.mainmethod0126.gradle.simple.versioning;

import java.io.IOException;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;

import io.github.mainmethod0126.gradle.simple.versioning.extension.SimpleSemanticVersionPluginExtension;
import io.github.mainmethod0126.gradle.simple.versioning.task.BuildAndVersioning;
import io.github.mainmethod0126.gradle.simple.versioning.utils.SsvPaths;

public class SemanticVersionManager implements Plugin<Project> {

        private Project project;

        @Override
        public void apply(Project project) {

                this.project = project;

                initExtensions();
                initUtils();

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

                project.getGradle().addBuildListener(new BuildListener() {

                        @Override
                        public void buildFinished(BuildResult buildResult) {
                                if (buildResult.getFailure() == null) {

                                        System.out.println("------------build Succeed------------");

                                        buildAndVersioning.commit();
                                }

                        }

                        @Override
                        public void projectsEvaluated(Gradle buildResult) {

                        }

                        @Override
                        public void projectsLoaded(Gradle buildResult) {

                        }

                        @Override
                        public void settingsEvaluated(Settings buildResult) {

                        }

                });

        }

        private void initExtensions() {
                SimpleSemanticVersionPluginExtension.init(this.project);
        }

        private void initUtils() {
                SsvPaths.init(this.project);
        }

}
