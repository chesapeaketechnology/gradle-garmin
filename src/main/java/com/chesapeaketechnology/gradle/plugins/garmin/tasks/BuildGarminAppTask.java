package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.GradleException;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.tasks.Input;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
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

    private static final String APP_BUILD = SEPARATOR + "bin" + SEPARATOR + "monkeyc" + (isWindows ? ".bat" : "");

    @Override
    protected String getBinaryDirectoryName()
    {
        return "devices";
    }

    @Override
    protected void runBuild(File binaryDir, ByteArrayOutputStream byteArrayOutputStream)
    {
        processDependencies(byteArrayOutputStream);

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
            args.add(deviceDirectory + SEPARATOR + outName + "-" + device + ".prg");

            execTask(sdkDirectory + APP_BUILD, args, byteArrayOutputStream);
        });
    }

    private void processDependencies(final ByteArrayOutputStream byteArrayOutputStream)
    {
        File dependenciesDir = createChildOutputDirectory(getProject().getBuildDir(), "dependencies", byteArrayOutputStream);

        if (dependenciesDir == null)
        {
            throw new GradleException("Cannot process dependencies due to null dependency directory!");
        }

        Configuration barrelConfig = getProject().getConfigurations().getByName("barrel");
        DependencySet barrelDependencies = barrelConfig.getAllDependencies();

        Iterator<Dependency> iterator = barrelDependencies.iterator();

        while (iterator.hasNext())
        {
            Dependency next = iterator.next();
            Set<File> files = barrelConfig.files(next);

            files.forEach(file -> {
                        try
                        {
                            String[] split = file.getName().split("-");
                            File dependency = new File(dependenciesDir.getPath() + SEPARATOR + split[0] + ".barrel");
                            Files.copy(file.toPath(), dependency.toPath(),
                                    StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
            );
        }

        appendToJungleFile();
    }

    private void appendToJungleFile()
    {
        final String dependencyDir = "build" + SEPARATOR + "dependencies";
        final String DEFAULT_JUNGLE_FILE = "monkey.jungle";
        final String BARREL_PATH_PROP = "base.barrelPath";

        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(DEFAULT_JUNGLE_FILE))
        {
            properties.load(input);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        try (OutputStream outputStream = new FileOutputStream(DEFAULT_JUNGLE_FILE))
        {
            String barrelPathProp = properties.getProperty(BARREL_PATH_PROP);

            if (barrelPathProp != null)
            {
                if (!barrelPathProp.contains(dependencyDir))
                {
                    barrelPathProp += ";" + dependencyDir;
                }
            } else
            {
                barrelPathProp = dependencyDir;
            }

            properties.setProperty(BARREL_PATH_PROP, barrelPathProp);
            properties.store(outputStream, null);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<File> getGeneratedArtifacts()
    {
        final String binaryDirectoryPath = getOutputDirectory() + SEPARATOR + getBinaryDirectoryName() + SEPARATOR;

        return devices.stream()
                .map(deviceStr -> {
                    final String deviceDirectory = deviceStr + SEPARATOR;
                    final String PRGFileName = outName + "-" + deviceStr + ".prg";
                    return new File(binaryDirectoryPath + deviceDirectory + PRGFileName);
                })
                .filter(File::exists)
                .collect(Collectors.toList());
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
