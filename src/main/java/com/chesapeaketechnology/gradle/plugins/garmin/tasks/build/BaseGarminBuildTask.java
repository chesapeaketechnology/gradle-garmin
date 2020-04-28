package com.chesapeaketechnology.gradle.plugins.garmin.tasks.build;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseGarminBuildTask extends BaseGarminTask
{
    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected List<File> jungleFiles;

    @Input
    protected String outName;

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

    public abstract List<File> getGeneratedArtifacts();

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
}
