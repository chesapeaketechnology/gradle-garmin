package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.tasks.Input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class BuildGarminAppTask extends BaseGarminTask
{
    @Input
    private List<String> devices;

    @Input
    private boolean parallel;

    @Input
    protected File developerKey;

    private final String APP_BUILD = "/bin/monkeyc";

    @Override
    protected String getBinaryDirectoryName()
    {
        return "devices";
    }

    @Override
    protected void runBuild(File binaryDir, ByteArrayOutputStream byteArrayOutputStream)
    {
        Stream<String> devicesStream = parallel ? this.devices.parallelStream() : this.devices.stream();

        devicesStream.forEach(device -> {
            File deviceDirectory = createChildOutputDirectory(binaryDir, device, byteArrayOutputStream);

            if (deviceDirectory == null)
            {
                System.out.println(byteArrayOutputStream.toString());
                //Something happened creating the device directory; skip this device
                byteArrayOutputStream.reset();
                return;
            }

            List<String> args = createDefaultArgs();
            args.add("--private-key");
            args.add(developerKey.getPath());
            args.add("--device");
            args.add(device);
            args.add("--output");
            args.add(deviceDirectory + "/" + outName + "-" + device + ".prg");

            execTask(sdkDirectory + APP_BUILD, args, byteArrayOutputStream);
        });
    }

    public boolean isParallel()
    {
        return parallel;
    }

    public void setParallel(boolean parallel)
    {
        this.parallel = parallel;
    }

    public List<String> getDevices()
    {
        return devices;
    }

    public void setDevice(List<String> devices)
    {
        this.devices = devices;
    }

    public File getDeveloperKey()
    {
        return developerKey;
    }

    public void setDeveloperKey(File developerKey)
    {
        this.developerKey = developerKey;
    }
}