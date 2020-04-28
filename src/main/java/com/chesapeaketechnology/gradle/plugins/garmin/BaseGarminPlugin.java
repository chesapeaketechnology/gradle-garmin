package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.BaseGarminExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.run.ConnectIQTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaBasePlugin;

import java.io.File;
import java.util.List;

public abstract class BaseGarminPlugin<E extends BaseGarminExtension, T extends BaseGarminTask> implements Plugin<Project>
{
    private final String GARMIN_SDK_HOME = "GARMIN_SDK_HOME";
    protected static final String START_CONNECT_IQ_TASK = "connectIQ";
    private final String GARMIN_GROUP = "garmin";

    @Override
    public void apply(Project project)
    {
        project.getPluginManager().apply(JavaBasePlugin.class);
        E extension = createExtension(project);
        project.afterEvaluate(proj -> {
            createConnectIQTask(proj, extension);
            createTasks(proj, extension);
        });
    }

    protected abstract E createExtension(Project project);

    protected abstract List<T> createTasks(Project project, E extension);

    protected BaseGarminTask createDefaultGarminTask(Project project, BaseGarminExtension extension, String taskName,
                                                     Class<? extends BaseGarminTask> taskClazz)
    {
        BaseGarminTask buildGarminTask = project.getTasks().create(taskName, taskClazz);
        if (extension.getSdkDirectory() != null)
        {
            buildGarminTask.setSdkDirectory(new File(extension.getSdkDirectory()));
        } else
        {
            throw new InvalidUserDataException("Garmin SDK directory cannot be null! Please set the GARMIN_SDK_HOME " +
                    "environment variable to the location of your Garmin installation OR set 'sdkDirectory' " +
                    "in the config block.");
        }
        buildGarminTask.setGroup(GARMIN_GROUP);

        return buildGarminTask;
    }

    protected ConnectIQTask createConnectIQTask(Project proj, E extension)
    {
        return (ConnectIQTask) createDefaultGarminTask(proj, extension, START_CONNECT_IQ_TASK, ConnectIQTask.class);
    }

    protected BaseGarminExtension createDefaultGarminExt(Project project, String extension, Class<E> extensionClazz)
    {
        BaseGarminExtension baseGarminExtension = project.getExtensions().create(extension, extensionClazz);
        String garminSDKHome = System.getenv(GARMIN_SDK_HOME);
        if (garminSDKHome != null)
        {
            baseGarminExtension.setSdkDirectory(garminSDKHome);
        }
        return baseGarminExtension;
    }
}
