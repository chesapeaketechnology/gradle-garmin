package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base Garmin task that invokes platform build tools from Garmin to produce wearable libraries and applications.
 */
public abstract class BaseGarminTask extends DefaultTask
{
    @InputDirectory
    @Optional
    private File appDirectory;

    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected File sdkDirectory;

    @Input
    protected List<File> jungleFiles;

    @Input
    protected String outName;

    protected static final String SEPARATOR = System.getProperty("file.separator");

    protected static final boolean isWindows = System.getProperty("os.name").contains("Windows") ;

    @TaskAction
    void start()
    {

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //create the root directory that the binary files will live under
        File binaryDir = createChildOutputDirectory(outputDirectory, getBinaryDirectoryName(), byteArrayOutputStream);
        if (binaryDir == null)
        {
            getLogger().error("Failed creating binary directory: {}", byteArrayOutputStream);
            //Something happened creating the binary directory; return
            byteArrayOutputStream.reset();
            return;
        }

        runBuild(binaryDir, byteArrayOutputStream);
    }

    protected abstract String getBinaryDirectoryName();

    protected abstract void runBuild(File binaryDir, ByteArrayOutputStream byteArrayOutputStream);

    protected void execTask(String execPath, List<String> args, ByteArrayOutputStream os)
    {
        getProject().exec(execSpec -> {
            execSpec.setExecutable(execPath);
            execSpec.setArgs(args);
            execSpec.setStandardOutput(os);
        });

        getLogger().info("Results of executing GarminTask {}", os);
        os.reset();
    }

    protected File createChildOutputDirectory(File parentDir, String childDirName, ByteArrayOutputStream os)
    {
        File childDir = new File(parentDir.getPath() + SEPARATOR + childDirName);

        if (!childDir.exists())
        {
            if (!childDir.mkdir())
            {
                try
                {
                    os.write(("Unable to create child dir: " + childDirName).getBytes());
                    return null;
                } catch (IOException e)
                {
                    getLogger().error("Unable to create child dir: {}", e);
                    return null;
                }
            }
        }

        return childDir;
    }

    protected List<String> createDefaultArgs()
    {
        List<String> args = new ArrayList<>();

        args.add("-f");
        jungleFiles.forEach(file -> args.add(file.getPath()));

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

    public File getSdkDirectory()
    {
        return sdkDirectory;
    }

    public void setSdkDirectory(File sdkDirectory)
    {
        this.sdkDirectory = sdkDirectory;
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

    public File getAppDirectory()
    {
        return appDirectory;
    }

    public void setAppDirectory(File appDirectory)
    {
        this.appDirectory = appDirectory;
    }
}
