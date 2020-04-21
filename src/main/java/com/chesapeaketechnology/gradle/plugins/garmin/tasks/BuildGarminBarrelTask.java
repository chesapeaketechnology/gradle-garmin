package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import org.gradle.api.tasks.Input;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Task that invokes platform build tools from Garmin to produce libraries (barrels).
 * <p>
 * The {@link BaseGarminTask task} invokes Garmin wearable build tools.
 *
 * @see BaseGarminTask
 */
public class BuildGarminBarrelTask extends BaseGarminTask
{

    private final String BARREL_BUILD = "/bin/barrelbuild";

    @Override
    protected String getBinaryDirectoryName()
    {
        return "barrel";
    }

    @Override
    protected void runBuild(File binaryDir, ByteArrayOutputStream byteArrayOutputStream)
    {
        List<String> args = createDefaultArgs();
        args.add("--output");
        args.add(binaryDir + "/" + outName + ".barrel");
        execTask(sdkDirectory + BARREL_BUILD, args, byteArrayOutputStream);
    }
}
