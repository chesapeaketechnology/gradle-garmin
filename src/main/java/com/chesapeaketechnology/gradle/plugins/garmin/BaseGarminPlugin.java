package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminExtension;
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
abstract class BaseGarminPlugin implements Plugin<Project>
{
    private final String GARMIN_SDK_HOME = "GARMIN_SDK_HOME";

    @Override
    public void apply(Project project)
    {
        project.getPluginManager().apply(JavaBasePlugin.class);
    }

    protected GarminExtension createDefaultGarminExtension(Project project, String extension, Class<? extends GarminExtension> extensionClazz)
    {
        GarminExtension garminExtension = project.getExtensions().create(extension, extensionClazz);
        garminExtension.setJungleFiles(Collections.singletonList("monkey.jungle"));
        garminExtension.setAppName(project.getName());
        garminExtension.setOutputDirectory(project.getBuildDir().getPath());

        String garminSDKHome = System.getenv(GARMIN_SDK_HOME);
        if (garminSDKHome != null)
        {
            garminExtension.setSdkDirectory(garminSDKHome);
        }

        return garminExtension;
    }

    protected BaseGarminTask createDefaultGarminTask(Project project, GarminExtension extension, String taskName,
                                                     Class<? extends BaseGarminTask> taskClazz)
    {
        Task build = project.getTasks().findByPath("build");
        if (build != null)
        {
            build.dependsOn(taskName);
        }

        BaseGarminTask buildGarminTask = project.getTasks().create(taskName, taskClazz);
        buildGarminTask.setJungleFiles(extension.getJungleFiles().stream().map(File::new).collect(Collectors.toList()));

        if (extension.getSdkDirectory() != null)
        {
            buildGarminTask.setSdkDirectory(new File(extension.getSdkDirectory()));
        } else
        {
            throw new InvalidUserDataException("Garmin SDK directory cannot be null! Please set the GARMIN_SDK_HOME " +
                    "environment variable to the location of your Garmin installation OR set 'sdkDirectory' " +
                    "in the config block.");
        }

        buildGarminTask.setOutputDirectory(new File(extension.getOutputDirectory()));
        buildGarminTask.setAppName(extension.getAppName());
        return buildGarminTask;
    }
}
