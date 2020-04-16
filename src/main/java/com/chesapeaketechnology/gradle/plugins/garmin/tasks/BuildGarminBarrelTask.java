package com.chesapeaketechnology.gradle.plugins.garmin.tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

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
