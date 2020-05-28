package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBuildExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BaseGarminBuildTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.common.IProjectPublishableTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;

import java.io.File;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base plugin to execute the specifics of building Garmin wearable software with platform specific tooling.
 */
abstract class BaseGarminBuildPlugin<E extends GarminBuildExtension> extends BaseGarminPlugin<E>
        implements IProjectPublishableTask<BaseGarminBuildTask>
{
    private static final String DEVELOPER_KEY_ENV = "GARMIN_DEV_KEY";

    protected BuildType buildType;

    public BaseGarminBuildPlugin(BuildType buildType)
    {
        this.buildType = buildType;
    }

    @Override
    public void apply(Project project)
    {
        super.apply(project);
        project.getPlugins().apply(MavenPublishPlugin.class);
    }

    protected GarminBuildExtension createDefaultGarminBuildExtension(Project project, String extension, Class<E> extensionClazz)
    {
        GarminBuildExtension garminBuildExtension = (GarminBuildExtension) createDefaultGarminExt(project, extension, extensionClazz);
        garminBuildExtension.setJungleFiles(Collections.singletonList("monkey.jungle"));
        garminBuildExtension.setAppName(project.getName());
        garminBuildExtension.setOutputDirectory(project.getBuildDir().getPath());
        String devKey = System.getenv(DEVELOPER_KEY_ENV);
        if (devKey != null)
        {
            garminBuildExtension.setDeveloperKey(devKey);
        }
        return garminBuildExtension;
    }

    protected BaseGarminBuildTask createDefaultGarminBuildTask(Project project, GarminBuildExtension extension, String taskName,
                                                               Class<? extends BaseGarminBuildTask> taskClazz)
    {
        BaseGarminBuildTask buildGarminTask = (BaseGarminBuildTask) super.createDefaultGarminTask(project, extension, taskName, taskClazz);

        Task build = project.getTasks().findByPath("build");
        if (build != null)
        {
            build.dependsOn(taskName);
        }

        buildGarminTask.setJungleFiles(extension.getJungleFiles().stream().map(File::new).collect(Collectors.toList()));
        buildGarminTask.setOutputDirectory(new File(extension.getOutputDirectory()));
        buildGarminTask.setAppName(extension.getAppName());
        return buildGarminTask;
    }

    @Override
    public void configurePublishing(Project project, BaseGarminBuildTask buildGarminTask)
    {
        Task publishToMavenLocal = project.getTasks().findByPath("publishToMavenLocal");
        if (publishToMavenLocal != null)
        {
            publishToMavenLocal.dependsOn(buildGarminTask);
        }

        Task publish = project.getTasks().findByPath("publish");
        if (publish != null)
        {
            publish.dependsOn(buildGarminTask);
        }

        project.getExtensions().configure(PublishingExtension.class, publishing -> publishing.publications(publications -> {
                    buildGarminTask.getGeneratedArtifacts().forEach(file -> {
                        final String name = getNameFromFile(file);
                        publications.create(
                                name,
                                MavenPublication.class, mavenPublication -> mavenPublication.artifact(file,
                                        mavenArtifact -> {
                                            mavenArtifact.builtBy(buildGarminTask);
                                            mavenArtifact.setExtension(getExtensionByStringHandling(file.getName()).orElse(""));
                                            if (buildType.equals(BuildType.APP))
                                            {
                                                mavenArtifact.setClassifier(name);
                                            }
                                        }))
                                .setArtifactId(buildGarminTask.getOutName());
                    });
                }
        ));
    }

    protected String getNameFromFile(File file)
    {
        String fileName = file.getName();

        return fileName.split("\\.")[0];
    }

    protected Optional<String> getExtensionByStringHandling(String filename)
    {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    protected enum BuildType
    {
        APP,
        BARREL
    }
}
