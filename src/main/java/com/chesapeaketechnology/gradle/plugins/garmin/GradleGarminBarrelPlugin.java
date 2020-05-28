package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminBarrelExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BuildGarminBarrelTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.test.TestGarminBarrelTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plugin to execute the specifics of building Garmin wearable libraries (barrels).
 * <p>
 * The {@link GarminBarrelExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminBuildPlugin
 */
public class GradleGarminBarrelPlugin extends BaseGarminBuildPlugin<GarminBarrelExtension>
{
    private static final String GARMIN_BARREL_EXT = "garminBarrel";
    public static final String BUILD_GARMIN_BARREL = "buildGarminBarrel";
    public static final String TEST_GARMIN_BARREL = "testGarminBarrel";

    public GradleGarminBarrelPlugin()
    {
        super(BuildType.BARREL);
    }

    @Override
    protected GarminBarrelExtension createExtension(Project project)
    {
        return (GarminBarrelExtension) createDefaultGarminBuildExtension(project, GARMIN_BARREL_EXT, GarminBarrelExtension.class);
    }

    @Override
    protected List<BaseGarminTask> createTasks(Project project, GarminBarrelExtension extension)
    {
        List<BaseGarminTask> tasks = new ArrayList<>();
        BuildGarminBarrelTask barrelBuildTask = (BuildGarminBarrelTask) createDefaultGarminBuildTask(project, extension, BUILD_GARMIN_BARREL,
                BuildGarminBarrelTask.class);

        if(extension.getTest() != null)
        {
            TestGarminBarrelTask barrelTestTask = createTestTask(project, extension);
            barrelTestTask.dependsOn(barrelBuildTask);
            tasks.add(barrelTestTask);
        }

        configurePublishing(project, barrelBuildTask);
        tasks.add(barrelBuildTask);
        return Collections.unmodifiableList(tasks);
    }

    private TestGarminBarrelTask createTestTask(Project project, GarminBarrelExtension extension)
    {
        TestGarminBarrelTask barrelTestTask = (TestGarminBarrelTask) createDefaultGarminTask(project, extension, TEST_GARMIN_BARREL,
                TestGarminBarrelTask.class);
        barrelTestTask.setJungleFiles(extension.getJungleFiles().stream().map(File::new).collect(Collectors.toList()));
        barrelTestTask.setOutName(extension.getAppName());
        barrelTestTask.setOutputDirectory(new File(extension.getOutputDirectory()));
        barrelTestTask.setDevice(extension.getTest().getDevice());

        if (extension.getDeveloperKey() != null)
        {
            barrelTestTask.setDeveloperKey(new File(extension.getDeveloperKey()));
        } else
        {
            throw new InvalidUserDataException("Developer key cannot be null! Please set the GARMIN_DEV_KEY " +
                    "environment variable to the location of your Garmin developer key OR set 'developerKey' " +
                    "in the garminApp config block.");
        }

        return barrelTestTask;
    }
}
