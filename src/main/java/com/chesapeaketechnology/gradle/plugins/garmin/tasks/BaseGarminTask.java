package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Base Garmin task that invokes platform build tools from Garmin to produce wearable libraries and applications.
 */
public abstract class BaseGarminTask extends DefaultTask
{
    @InputDirectory
    @Optional
    private File appDirectory;

    @Input
    protected File sdkDirectory;

    protected static final String SEPARATOR = System.getProperty("file.separator");

    protected static final boolean isWindows = System.getProperty("os.name").contains("Windows");

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

    protected String getBinDirectory()
    {
        return sdkDirectory + SEPARATOR + "bin" + SEPARATOR;
    }

    public File getSdkDirectory()
    {
        return sdkDirectory;
    }

    public void setSdkDirectory(File sdkDirectory)
    {
        this.sdkDirectory = sdkDirectory;
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
