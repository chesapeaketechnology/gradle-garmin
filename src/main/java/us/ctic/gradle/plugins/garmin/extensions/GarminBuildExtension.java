package us.ctic.gradle.plugins.garmin.extensions;

import org.gradle.api.Action;

import java.util.Collections;
import java.util.List;

/**
 * Gradle extension for Garmin wearable applications and libraries, with common inputs and outputs for build script use.
 */
public class GarminBuildExtension extends BaseGarminExtension
{
    private List<String> jungleFiles;

    private String appName;

    private String outputDirectory;

    private String developerKey;

    private TestExtension test;

    void test(Action<? super TestExtension> action)
    {
        if (test == null)
        {
            test = new TestExtension();
            action.execute(test);
        }
    }

    public TestExtension getTest()
    {
        return test;
    }

    public void setTest(TestExtension test)
    {
        this.test = test;
    }

    public List<String> getJungleFiles()
    {
        return Collections.unmodifiableList(jungleFiles);
    }

    public void setJungleFiles(List<String> jungleFiles)
    {
        this.jungleFiles = Collections.unmodifiableList(jungleFiles);
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getOutputDirectory()
    {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory)
    {
        this.outputDirectory = outputDirectory;
    }

    public String getDeveloperKey()
    {
        return developerKey;
    }

    public void setDeveloperKey(String developerKeyLocation)
    {
        developerKey = developerKeyLocation;
    }
}
