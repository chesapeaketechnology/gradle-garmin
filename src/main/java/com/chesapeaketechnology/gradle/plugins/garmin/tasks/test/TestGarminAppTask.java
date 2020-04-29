package com.chesapeaketechnology.gradle.plugins.garmin.tasks.test;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import com.chesapeaketechnology.gradle.plugins.garmin.tasks.common.IFlushListener;
import org.gradle.api.GradleException;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestGarminAppTask extends BaseGarminTask implements IFlushListener
{
    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected String outName;

    @Input
    private List<String> devices;

    @Input
    @Optional
    private String testDevice;

    protected List<String> getArgs()
    {
        List<String> args = new ArrayList<>();
        args.add(outputDirectory + SEPARATOR + "devices" + SEPARATOR + devices.get(0) + SEPARATOR + outName + "-" + devices.get(0) + ".prg");
        args.add(devices.get(0));
        args.add("-t");
        return args;
    }

    @TaskAction
    protected void run()
    {
        errorStream.setFlushListener(this);
        infoStream.setFlushListener(this);
        execTask(getBinDirectory() + "monkeydo" + (isWindows ? ".bat" : ""), getArgs());
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

    public List<String> getDevices()
    {
        return devices;
    }

    public void setDevices(List<String> devices)
    {
        this.devices = devices;
    }

    public String getTestDevice()
    {
        return testDevice;
    }

    public void setTestDevice(String testDevice)
    {
        this.testDevice = testDevice;
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
