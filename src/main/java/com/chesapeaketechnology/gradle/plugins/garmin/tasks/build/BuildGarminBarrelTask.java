package com.chesapeaketechnology.gradle.plugins.garmin.tasks.build;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Task that invokes platform build tools from Garmin to produce libraries (barrels).
 * <p>
 * The {@link BaseGarminTask task} invokes Garmin wearable build tools.
 *
 * @see BaseGarminTask
 */
public class BuildGarminBarrelTask extends BaseGarminBuildTask
{
    private final String BARREL_BUILD = SEPARATOR + "bin" + SEPARATOR + "barrelbuild" + (isWindows ? ".bat" : "");

    @Override
    protected String getBinaryDirectoryName()
    {
        return "barrel";
    }

    @Override
    protected void runBuild(File binaryDir)
    {
        List<String> args = createDefaultArgs();
        args.add("--output");
        args.add(binaryDir + SEPARATOR + outName + ".barrel");
        execTask(args);
    }

    @Override
    public List<File> getGeneratedArtifacts()
    {
        final String binaryDirectoryPath = getOutputDirectory() + SEPARATOR + getBinaryDirectoryName() + SEPARATOR;
        File file = new File(binaryDirectoryPath + outName + ".barrel");
        return file.exists() ? Collections.singletonList(file) : Collections.emptyList();
    }

    @Override
    public String getExecName()
    {
        return "barrelbuild";
    }

    @Override
    public List<String> getArgs()
    {
        List<String> args = createDefaultArgs();
        args.add("--output");
        args.add(outputDirectory.getPath() + SEPARATOR + getBinaryDirectoryName() + SEPARATOR + outName + ".barrel");
        return null;
    }
}
