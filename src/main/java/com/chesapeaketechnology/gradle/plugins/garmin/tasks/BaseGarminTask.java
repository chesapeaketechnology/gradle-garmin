package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGarminTask extends DefaultTask
{
    @InputDirectory
    private File appDirectory;

    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected File sdkDirectory;

    @Input
    protected List<File> jungleFiles;

    @Input
    protected String outName;

    @TaskAction
    void start()
    {
        //make sure data is valid
        checkUserData();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //create the root directory that the binary files will live under
        File binaryDir = createChildOutputDirectory(outputDirectory, getBinaryDirectoryName(), byteArrayOutputStream);
        if (binaryDir == null)
        {
            System.out.println(byteArrayOutputStream.toString());
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

        System.out.println(os.toString());
        os.reset();
    }

    protected File createChildOutputDirectory(File parentDir, String childDirName, ByteArrayOutputStream os)
    {
        File childDir = new File(parentDir.getPath() + "/" + childDirName);

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
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return childDir;
    }

    protected void checkUserData()
    {

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
        return jungleFiles;
    }

    public void setJungleFiles(List<File> jungleFiles)
    {
        this.jungleFiles = jungleFiles;
    }

    public String getAppName()
    {
        return outName;
    }

    public void setAppName(String appName)
    {
        this.outName = appName;
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
