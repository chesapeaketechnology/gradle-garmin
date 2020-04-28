package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBarrelBuildExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BuildGarminBarrelTask;
import org.gradle.api.Project;

/**
 * Plugin to execute the specifics of building Garmin wearable libraries (barrels).
 * <p>
 * The {@link GarminBarrelBuildExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminBuildPlugin
 */
public class GradleGarminBarrelPlugin extends BaseGarminBuildPlugin
{
    private static final String GARMIN_BARREL_EXT = "garminBarrel";
    public static final String BUILD_GARMIN_BARREL = "buildGarminBarrel";

    @Override
    public void apply(Project project)
    {
        super.apply(project);

        GarminBarrelBuildExtension appExtension = (GarminBarrelBuildExtension) createDefaultGarminBuildExtension(project,
                GARMIN_BARREL_EXT, GarminBarrelBuildExtension.class);
        project.afterEvaluate(proj -> createDefaultGarminBuildTask(proj, appExtension, BUILD_GARMIN_BARREL,
                BuildGarminBarrelTask.class));
    }
}
