package com.chesapeaketechnology.gradle.plugins.garmin.extensions;

import java.util.List;

public class GarminExtension
{
    private String sdkDirectory;

    private String appDirectory;

    private List<String> jungleFiles;

    private String appName;

    private String outputDirectory;

    public String getSdkDirectory()
    {
        return sdkDirectory;
    }

    public void setSdkDirectory(String sdkDirectory)
    {
        this.sdkDirectory = sdkDirectory;
    }

    public List<String> getJungleFiles()
    {
        return jungleFiles;
    }

    public void setJungleFiles(List<String> jungleFiles)
    {
        this.jungleFiles = jungleFiles;
    }

    public String getAppDirectory()
    {
        return appDirectory;
    }

    public void setAppDirectory(String appDirectory)
    {
        this.appDirectory = appDirectory;
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
}
