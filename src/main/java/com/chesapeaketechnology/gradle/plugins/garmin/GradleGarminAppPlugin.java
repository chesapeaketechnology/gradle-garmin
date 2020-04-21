package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminAppExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BuildGarminAppTask;
import org.gradle.api.Project;

import java.io.File;

/**
 * Plugin to execute the specifics of building Garmin wearable applications.
 * <p>
 * The {@link GarminAppExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminPlugin
 */
public class GradleGarminAppPlugin extends BaseGarminPlugin
{
    private static final String GARMIN_APP_EXT = "garminApp";
    private static final String BUILD_GARMIN_APP = "buildGarminApp";

    @Override
    public void apply(Project project)
    {
        GarminAppExtension appExtension = (GarminAppExtension) createDefaultGarminExtension(project, GARMIN_APP_EXT,
                GarminAppExtension.class);
        appExtension.setParallelBuild(true);

        project.afterEvaluate(proj -> {
            BuildGarminAppTask defaultGarminTask = (BuildGarminAppTask) createDefaultGarminTask(proj, appExtension,
                    BUILD_GARMIN_APP, BuildGarminAppTask.class);
            defaultGarminTask.setDevices(appExtension.getTargetDevices());
            defaultGarminTask.setDeveloperKey(new File(appExtension.getDeveloperKey()));
            defaultGarminTask.setParallel(appExtension.isParallelBuild());
        });
    }
}
