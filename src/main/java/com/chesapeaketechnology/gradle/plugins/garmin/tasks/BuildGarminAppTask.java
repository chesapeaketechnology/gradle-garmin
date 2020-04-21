package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.tasks.Input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Task that invokes platform build tools from Garmin to produce wearable applications.
 * <p>
 * The {@link BaseGarminTask task} invokes Garmin wearable build tools.
 *
 * @see BaseGarminTask
 */
public class BuildGarminAppTask extends BaseGarminTask
{
    @Input
    private List<String> devices;

    @Input
    private boolean parallel;

    @Input
    protected File developerKey;

    private static final String APP_BUILD = "/bin/monkeyc";

    @Override
    protected String getBinaryDirectoryName()
    {
        return "devices";
    }

    @Override
    protected void runBuild(File binaryDir, ByteArrayOutputStream byteArrayOutputStream)
    {
        Stream<String> devicesStream = parallel ? devices.parallelStream() : devices.stream();

        devicesStream.forEach(device -> {
            File deviceDirectory = createChildOutputDirectory(binaryDir, device, byteArrayOutputStream);

            if (deviceDirectory == null)
            {
                getLogger().error("Error while creating the device directory, skipping this device: {}", byteArrayOutputStream);
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
        return Collections.unmodifiableList(devices);
    }

    public void setDevices(List<String> devices)
    {
        this.devices = Collections.unmodifiableList(devices);
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
