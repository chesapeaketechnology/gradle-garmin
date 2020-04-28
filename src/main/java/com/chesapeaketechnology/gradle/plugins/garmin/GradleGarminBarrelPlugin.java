package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBarrelExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BuildGarminBarrelTask;
import org.gradle.api.Project;

/**
 * Plugin to execute the specifics of building Garmin wearable libraries (barrels).
 * <p>
 * The {@link GarminBarrelExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminPlugin
 */
public class GradleGarminBarrelPlugin extends BaseGarminPlugin
{
    private static final String GARMIN_BARREL_EXT = "garminBarrel";
    public static final String BUILD_GARMIN_BARREL = "buildGarminBarrel";

    @Override
    public void apply(Project project)
    {
        super.apply(project);

        GarminBarrelExtension appExtension = (GarminBarrelExtension) createDefaultGarminExtension(project,
                GARMIN_BARREL_EXT, GarminBarrelExtension.class);
        project.afterEvaluate(proj -> createDefaultGarminTask(proj, appExtension, BUILD_GARMIN_BARREL,
                BuildGarminBarrelTask.class));
    }
}
