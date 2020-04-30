package com.chesapeaketechnology.gradle.plugins.garmin.tasks.test;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: class description
 */
public class TestGarminBarrelTask extends BaseTestTask
{
    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected File developerKey;

    @Input
    protected String outName;

    @Input
    private String device;

    @Input
    protected List<File> jungleFiles;

    @Override
    public String getExecName()
    {
        return "barreltest";
    }

    public List<String> getArgs()
    {
        List<String> args = new ArrayList<>();
        args.add("-o");
        args.add(outputDirectory + SEPARATOR + outName + ".barrel");
        args.add("-f");
        jungleFiles.forEach(file -> args.add(file.getPath()));
        args.add("-d");
        args.add(device);
        args.add("-y");
        args.add(developerKey.getPath());
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

    public String getOutName()
    {
        return outName;
    }

    public void setOutName(String outName)
    {
        this.outName = outName;
    }

    public List<File> getJungleFiles()
    {
        return jungleFiles;
    }

    public void setJungleFiles(List<File> jungleFiles)
    {
        this.jungleFiles = jungleFiles;
    }

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }

    public File getDeveloperKey()
    {
        return developerKey;
    }

    public void setDeveloperKey(File developerKey)
    {
        this.developerKey = developerKey;
    }

    @Override
    public void flushing(String toBeFlushed)
    {
        if (toBeFlushed.contains("Error:"))
        {
            throw new GradleException(toBeFlushed);
        }
    }
}