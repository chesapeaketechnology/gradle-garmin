package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBarrelExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BuildGarminBarrelTask;
import org.gradle.api.Project;

public class GradleGarminBarrelPlugin extends BaseGarminPlugin
{
    private static final String GARMIN_BARREL_EXT = "garminBarrel";
    private static final String BUILD_GARMIN_BARREL = "buildGarminBarrel";

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
