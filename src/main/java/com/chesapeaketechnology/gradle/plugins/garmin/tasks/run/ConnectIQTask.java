package com.chesapeaketechnology.gradle.plugins.garmin.tasks.run;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.tasks.TaskAction;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

public class ConnectIQTask extends BaseGarminTask
{
    private final String CONNECTIQ_EXEC = "connectiq" + (isWindows ? ".bat" : "");

    @TaskAction
    void start()
    {
        execTask(getBinDirectory() + CONNECTIQ_EXEC, Collections.emptyList());
    }
}
