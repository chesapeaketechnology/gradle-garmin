package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.common.IExecutable;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.common.LogOutputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;

import java.io.File;
import java.util.List;

/**
 * Base Garmin task that invokes platform build tools from Garmin to produce wearable libraries and applications.
 */
public abstract class BaseGarminTask extends DefaultTask implements IExecutable
{
    @InputDirectory
    @Optional
    private File appDirectory;

    @Input
    protected File sdkDirectory;

    protected static final String SEPARATOR = System.getProperty("file.separator");

    protected static final boolean isWindows = System.getProperty("os.name").contains("Windows");

    protected final LogOutputStream infoStream = new LogOutputStream(getLogger(), LogLevel.INFO);
    protected final LogOutputStream errorStream = new LogOutputStream(getLogger(), LogLevel.ERROR);

    protected void execTask(List<String> args)
    {
        getProject().exec(execSpec -> {
            execSpec.setExecutable(buildExecPath(getExecName()));
            execSpec.setArgs(args);
            execSpec.setStandardOutput(infoStream);
            execSpec.setErrorOutput(errorStream);
        });
    }

    protected String getBinDirectory()
    {
        return sdkDirectory + SEPARATOR + "bin" + SEPARATOR;
    }

    protected void logError(String errorMessage)
    {
        errorStream.getLogger().error(errorMessage);
    }

    protected void logException(String errorMessage, Exception e)
    {
        errorStream.getLogger().error(errorMessage, e);
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

    protected String buildExecPath(String execName)
    {
        return getBinDirectory() + execName + (isWindows ? ".bat" : "");
    }
}
