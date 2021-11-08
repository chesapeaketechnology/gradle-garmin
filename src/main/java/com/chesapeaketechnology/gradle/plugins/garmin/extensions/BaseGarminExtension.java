package com.chesapeaketechnology.gradle.plugins.garmin.extensions;

/**
 * Base extension class for all Garmin extensions
 */
public class BaseGarminExtension
{
    private String sdkDirectory;

    private String appDirectory;

    private String typeCheckLevel;

    public String getSdkDirectory()
    {
        return sdkDirectory;
    }

    public void setSdkDirectory(String sdkDirectory)
    {
        this.sdkDirectory = sdkDirectory;
    }

    public String getAppDirectory()
    {
        return appDirectory;
    }

    public void setAppDirectory(String appDirectory)
    {
        this.appDirectory = appDirectory;
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
