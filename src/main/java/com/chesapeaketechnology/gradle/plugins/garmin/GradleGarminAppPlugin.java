package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminAppExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BuildGarminAppTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import java.io.File;

public class GradleGarminAppPlugin extends BaseGarminPlugin
{
    private static final String GARMIN_APP_EXT = "garminApp";
    private static final String BUILD_GARMIN_APP = "buildGarminApp";

    private static final String DEVELOPER_KEY_ENV = "GARMIN_DEV_KEY";

    @Override
    public void apply(Project project)
    {
        super.apply(project);
        GarminAppExtension appExtension = (GarminAppExtension) createDefaultGarminExtension(project, GARMIN_APP_EXT,
                GarminAppExtension.class);
        appExtension.setParallelBuild(true);

        String devKey = System.getenv(DEVELOPER_KEY_ENV);
        if (devKey != null)
        {
            appExtension.setDeveloperKey(devKey);
        }

        project.afterEvaluate(proj -> {
            BuildGarminAppTask defaultGarminTask = (BuildGarminAppTask) createDefaultGarminTask(proj, appExtension,
                    BUILD_GARMIN_APP, BuildGarminAppTask.class);
            defaultGarminTask.setDevice(appExtension.getTargetDevices());
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
        });
    }
}
