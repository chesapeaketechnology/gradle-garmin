package com.chesapeaketechnology.gradle.plugins.garmin.extensions;

import java.util.Collections;
import java.util.List;

/**
 * Gradle extension for Garmin wearable applications.
 */
public class GarminAppExtension extends GarminExtension
{
    private List<String> targetDevices;

    private boolean parallelBuild;

    private String developerKey;

    public List<String> getTargetDevices()
    {
        return Collections.unmodifiableList(targetDevices);
    }

    public void setTargetDevices(List<String> targetDevices)
    {
        this.targetDevices = Collections.unmodifiableList(targetDevices);
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
        developerKey = developerKeyLocation;
    }
}
