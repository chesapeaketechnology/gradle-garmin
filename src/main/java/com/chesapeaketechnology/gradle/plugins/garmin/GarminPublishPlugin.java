package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

import java.io.File;
import java.util.Optional;

public class GarminPublishPlugin implements Plugin<Project>
{
    @Override
    public void apply(Project project)
    {
        project.getPlugins().apply(MavenPublishPlugin.class);

        project.afterEvaluate(proj -> {
            boolean hasBarrelPlugin = proj.getPlugins().hasPlugin(GradleGarminBarrelPlugin.class);
            boolean hasAppPlugin = proj.getPlugins().hasPlugin(GradleGarminAppPlugin.class);

            if (!hasAppPlugin && !hasBarrelPlugin)
            {
                throw new GradleException("Unable to find any Garmin Gradle plugins.");
            }

            final BaseGarminTask baseGarminTask = (BaseGarminTask) proj.getTasks().findByName(
                    hasAppPlugin ? GradleGarminAppPlugin.BUILD_GARMIN_APP : GradleGarminBarrelPlugin.BUILD_GARMIN_BARREL);

            if (baseGarminTask == null)
            {
                throw new GradleException("Unable to find Garmin build task.");
            }

            Task publishToMavenLocal = project.getTasks().findByPath("publishToMavenLocal");
            if (publishToMavenLocal != null)
            {
                publishToMavenLocal.dependsOn(baseGarminTask);
            }

            proj.getExtensions().configure(PublishingExtension.class, publishing -> publishing.publications(publications -> {
                baseGarminTask.getGeneratedArtifacts().forEach(file -> {
                            final String name = getNameFromFile(file, hasAppPlugin);
                            publications.create(
                                    name,
                                    MavenPublication.class, mavenPublication -> mavenPublication.artifact(file,
                                            mavenArtifact -> {
                                                mavenArtifact.builtBy(baseGarminTask);
                                                mavenArtifact.setExtension(getExtensionByStringHandling(file.getName()).orElse(""));
                                                if (hasAppPlugin)
                                                {
                                                    //find the device name to set the classifier
                                                    mavenArtifact.setClassifier(name);
                                                }
                                            }))
                                    .setArtifactId(baseGarminTask.getOutName());
                        });
                    }
            ));
        });
    }

    private String getNameFromFile(File file, boolean isApp)
    {
        String fileName = file.getName();

        if (isApp)
        {
            //example file name: app-fenix5plus.prg
            final String[] split = fileName.split("-");//[app, fenix5plus.prg]
            fileName = split[split.length - 1]; // fenix5plus.prg
        }

        return fileName.split("\\.")[0];
    }

    public Optional<String> getExtensionByStringHandling(String filename)
    {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
