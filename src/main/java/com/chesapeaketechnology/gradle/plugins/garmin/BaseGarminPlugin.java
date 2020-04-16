package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

abstract class BaseGarminPlugin implements Plugin<Project>
{
    @Override
    public void apply(Project project)
    {
        project.getPlugins().apply(JavaPlugin.class);
    }

    protected GarminExtension createDefaultGarminExtension(Project project, String extension, Class<? extends GarminExtension> extensionClazz)
    {
        GarminExtension garminExtension = project.getExtensions().create(extension, extensionClazz);
        garminExtension.setAppDirectory(project.getProjectDir().toString());
        garminExtension.setJungleFiles(Collections.singletonList("monkey.jungle"));
        garminExtension.setAppName(project.getName());
        garminExtension.setOutputDirectory("bin");
        return garminExtension;
    }

    protected BaseGarminTask createDefaultGarminTask(Project project, GarminExtension extension, String taskName,
                                                     Class<? extends BaseGarminTask> taskClazz)
    {
        BaseGarminTask buildGarminTask = project.getTasks().create(taskName, taskClazz);
        buildGarminTask.setAppDirectory(new File(extension.getAppDirectory()));
        buildGarminTask.setJungleFiles(extension.getJungleFiles().stream().map(File::new).collect(Collectors.toList()));
        buildGarminTask.setSdkDirectory(new File(extension.getSdkDirectory()));
        buildGarminTask.setOutputDirectory(new File(extension.getOutputDirectory()));
        buildGarminTask.setAppName(extension.getAppName());
        return buildGarminTask;
    }
}
