package com.chesapeaketechnology.gradle.plugins.garmin;

import com.chesapeaketechnology.gradle.plugins.garmin.extensions.GarminAppExtension;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.build.BuildGarminAppTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.test.TestGarminAppTask;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Plugin to execute the specifics of building Garmin wearable applications.
 * <p>
 * The {@link GarminAppExtension extension} provides a number of basics for configuring the wearable application for building.
 *
 * @see BaseGarminBuildPlugin
 */
public class GradleGarminAppPlugin extends BaseGarminBuildPlugin<GarminAppExtension>
{
    private static final String GARMIN_APP_EXT = "garminApp";
    public static final String BUILD_GARMIN_APP = "buildGarminApp";
    public static final String TEST_GARMIN_APP = "testGarminApp";

    @Override
    public void apply(Project project)
    {
        super.apply(project);

        project.getConfigurations().maybeCreate("barrel");
    }

    @Override
    protected GarminAppExtension createExtension(Project project)
    {
        return createAppBuildExt(project);
    }

    @Override
    protected List<BaseGarminTask> createTasks(Project project, GarminAppExtension extension)
    {
        List<BaseGarminTask> tasks = new ArrayList<>();
        BuildGarminAppTask buildTask = createBuildTask(project, extension);
        if(extension.getTest() != null)
        {
            TestGarminAppTask testTask = createTestTask(project, extension);
            testTask.dependsOn(buildTask);
            testTask.dependsOn(project.getTasks().getByPath(START_CONNECT_IQ_TASK));
            testTask.getOutputs().upToDateWhen(task -> false);
            tasks.add(testTask);
        }

        configurePublishing(project, buildTask);
        tasks.add(buildTask);
        return Collections.unmodifiableList(tasks);
    }

    private GarminAppExtension createAppBuildExt(Project project)
    {
        GarminAppExtension appExtension = (GarminAppExtension) createDefaultGarminBuildExtension(project, GARMIN_APP_EXT,
                GarminAppExtension.class);
        appExtension.setParallelBuild(true);

        return appExtension;
    }

    private BuildGarminAppTask createBuildTask(Project proj, GarminAppExtension appExtension)
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

    private TestGarminAppTask createTestTask(Project project, GarminAppExtension extension)
    {
        TestGarminAppTask testGarminAppTask = (TestGarminAppTask) super.createDefaultGarminTask(project, extension, TEST_GARMIN_APP, TestGarminAppTask.class);
        testGarminAppTask.setDevice(extension.getTest().getDevice());
        testGarminAppTask.setOutName(extension.getAppName());
        testGarminAppTask.setOutputDirectory(new File(extension.getOutputDirectory()));

        return testGarminAppTask;
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
