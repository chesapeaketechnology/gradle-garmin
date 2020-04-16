package com.chesapeaketechnology.gradle.plugins.garmin.extensions;

import java.util.List;

public class GarminAppExtension extends GarminExtension
{
    private List<String> targetDevices;

    private boolean parallelBuild;

    private String developerKey;

    public List<String> getTargetDevices()
    {
        return targetDevices;
    }

    public void setTargetDevices(List<String> targetDevices)
    {
        this.targetDevices = targetDevices;
    }

    public boolean isParallelBuild()
    {
        return parallelBuild;
    }

    public void setParallelBuild(boolean parallelBuild)
    {
        this.parallelBuild = parallelBuild;
    }

    public String getDeveloperKey()
    {
        return developerKey;
    }

    public void setDeveloperKey(String developerKeyLocation)
    {
        this.developerKey = developerKeyLocation;
    }
}
