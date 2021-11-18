package us.ctic.gradle.plugins.garmin.tasks.test;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test task for Garmin apps
 */
public class TestGarminAppTask extends BaseTestTask
{
    @OutputDirectory
    protected File outputDirectory;

    @Input
    protected String outName;

    @Input
    private String device;

    @Override
    public String getExecName()
    {
        return "monkeydo";
    }

    public List<String> getArgs()
    {
        List<String> args = new ArrayList<>();
        args.add(outputDirectory + SEPARATOR + "devices" + SEPARATOR + device + SEPARATOR + outName + "-" + device + ".prg");
        args.add(device);
        args.add("-t");
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

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }
}
