package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBarrelBuildExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BuildGarminBarrelTask;
import org.gradle.api.Project;

import java.util.Collections;
import java.util.List;

/**
 * Plugin to execute the specifics of building Garmin wearable libraries (barrels).
 * <p>
 * The {@link GarminBarrelBuildExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminBuildPlugin
 */
public class GradleGarminBarrelPlugin extends BaseGarminBuildPlugin<GarminBarrelBuildExtension>
{
    private static final String GARMIN_BARREL_EXT = "garminBarrel";
    public static final String BUILD_GARMIN_BARREL = "buildGarminBarrel";

    @Override
    protected GarminBarrelBuildExtension createExtension(Project project)
    {
        return (GarminBarrelBuildExtension) createDefaultGarminBuildExtension(project, GARMIN_BARREL_EXT, GarminBarrelBuildExtension.class);
    }

    @Override
    protected List<BaseGarminTask> createTasks(Project project, GarminBarrelBuildExtension extension)
    {
        BuildGarminBarrelTask barrelBuildTask = (BuildGarminBarrelTask) createDefaultGarminBuildTask(project, extension, BUILD_GARMIN_BARREL,
                BuildGarminBarrelTask.class);
        configurePublishing(project, barrelBuildTask);
        return Collections.singletonList(barrelBuildTask);
    }
}
