package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminAppBuildExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BuildGarminAppTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Plugin to execute the specifics of building Garmin wearable applications.
 * <p>
 * The {@link GarminAppBuildExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminBuildPlugin
 */
public class GradleGarminAppPlugin extends BaseGarminBuildPlugin<GarminAppBuildExtension, BuildGarminAppTask>
{
    private static final String GARMIN_APP_EXT = "garminApp";
    public static final String BUILD_GARMIN_APP = "buildGarminApp";
    private static final String DEVELOPER_KEY_ENV = "GARMIN_DEV_KEY";

    @Override
    public void apply(Project project)
    {
        super.apply(project);

        project.getConfigurations().maybeCreate("barrel");
    }

    @Override
    protected GarminAppBuildExtension createExtension(Project project)
    {
        return createAppBuildExt(project);
    }

    @Override
    protected List<BuildGarminAppTask> createTasks(Project project, GarminAppBuildExtension extension)
    {
        BuildGarminAppTask buildTask = createBuildTask(project, extension);
        configurePublishing(project, buildTask);
        return Collections.singletonList(buildTask);
    }

    private GarminAppBuildExtension createAppBuildExt(Project project)
    {
        GarminAppBuildExtension appExtension = (GarminAppBuildExtension) createDefaultGarminBuildExtension(project, GARMIN_APP_EXT,
                GarminAppBuildExtension.class);
        appExtension.setParallelBuild(true);

        String devKey = System.getenv(DEVELOPER_KEY_ENV);
        if (devKey != null)
        {
            appExtension.setDeveloperKey(devKey);
        }

        return appExtension;
    }

    private BuildGarminAppTask createBuildTask(Project proj, GarminAppBuildExtension appExtension)
    {
        BuildGarminAppTask defaultGarminTask = (BuildGarminAppTask) createDefaultGarminBuildTask(proj, appExtension,
                BUILD_GARMIN_APP, BuildGarminAppTask.class);
        defaultGarminTask.setDevices(appExtension.getTargetDevices());

        if (appExtension.getDeveloperKey() != null)
        {
            defaultGarminTask.setDeveloperKey(new File(appExtension.getDeveloperKey()));
        } else
        {
            throw new InvalidUserDataException("Developer key cannot be null! Please set the GARMIN_DEV_KEY " +
                    "environment variable to the location of your Garmin developer key OR set 'developerKey' " +
                    "in the garminApp config block.");
        }

        defaultGarminTask.setParallel(appExtension.isParallelBuild());
        return defaultGarminTask;
    }

    @Override
    protected String getNameFromFile(File file)
    {
        String fileName = file.getName();
        //example file name: app-fenix5plus.prg
        final String[] split = fileName.split("-");//[app, fenix5plus.prg]
        fileName = split[split.length - 1]; // fenix5plus.prg
        return fileName.split("\\.")[0];
    }
}
