package us.ctic.gradle.plugins.garmin.tasks.build;

import us.ctic.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base abstract build task for apps and barrels
 */
public abstract class BaseGarminBuildTask extends BaseGarminTask {
    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected List<File> jungleFiles;

    @Input
    protected String outName;

    @Optional
    private String typeCheckLevel;

    @TaskAction
    void start()
    {
        //create the root directory that the binary files will live under
        File binaryDir = createChildOutputDirectory(outputDirectory, getBinaryDirectoryName());
        if (binaryDir == null)
        {
            logError("Failed creating binary directory!");
            return;
        }

        runBuild(binaryDir);
    }

    protected abstract String getBinaryDirectoryName();

    protected abstract void runBuild(File binaryDir);

    public abstract List<File> getGeneratedArtifacts();

    protected File createChildOutputDirectory(File parentDir, String childDirName)
    {
        File childDir = new File(parentDir.getPath() + SEPARATOR + childDirName);

        if (!childDir.exists())
        {
            if (!childDir.mkdir())
            {
                logError("Unable to create child dir: " + childDirName);
                return null;
            }
        }

        return childDir;
    }

    protected List<String> createDefaultArgs()
    {
        List<String> args = new ArrayList<>();

        args.add("-f");
        jungleFiles.forEach(file -> args.add(file.getPath()));

        // Parameter set to string here because 0 is a valid value and an integer would default to it. A error/null
        // indicates there should be no type checking applied.
        try {
            int checkLevel = Integer.parseInt(typeCheckLevel);
            if (checkLevel >= 0 && checkLevel < 4) {
                args.add("-l");
                args.add(typeCheckLevel);
            }
        }
        catch(Exception exception)
        {
            throw new IllegalArgumentException("Could not run type check on Garmin operation. Check that the SDK " +
                    "version is 4.0 or later.", exception);
        }

        args.add("--warn");

        return args;
    }

    public File getOutputDirectory()
    {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory)
    {
        this.outputDirectory = outputDirectory;
    }

    public List<File> getJungleFiles()
    {
        return Collections.unmodifiableList(jungleFiles);
    }

    public void setJungleFiles(List<File> jungleFiles)
    {
        this.jungleFiles = Collections.unmodifiableList(jungleFiles);
    }

    public String getAppName()
    {
        return outName;
    }

    public void setAppName(String appName)
    {
        outName = appName;
    }

    public String getOutName()
    {
        return outName;
    }

    public void setOutName(String outName)
    {
        this.outName = outName;
    }

    public String getTypeCheckLevel()
    {
        return typeCheckLevel;
    }

    public void setTypeCheckLevel(String typeCheckLevel)
    {
        this.typeCheckLevel = typeCheckLevel;
    }
}
