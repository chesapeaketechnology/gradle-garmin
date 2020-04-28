package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBuildExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BaseGarminBuildTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaBasePlugin;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Base plugin to execute the specifics of building Garmin wearable software with platform specific tooling.
 */
abstract class BaseGarminBuildPlugin extends BaseGarminPlugin
{
    @Override
    public void apply(Project project)
    {
        super.apply(project);
    }

    protected GarminBuildExtension createDefaultGarminBuildExtension(Project project, String extension, Class<? extends GarminBuildExtension> extensionClazz)
    {
        GarminBuildExtension garminBuildExtension = (GarminBuildExtension) createDefaultGarminExt(project, extension, extensionClazz);
        garminBuildExtension.setJungleFiles(Collections.singletonList("monkey.jungle"));
        garminBuildExtension.setAppName(project.getName());
        garminBuildExtension.setOutputDirectory(project.getBuildDir().getPath());
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
}
