package com.chesapeaketechnology.gradle.plugins.garmin.tasks.run;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.tasks.TaskAction;

import java.util.Collections;
import java.util.List;

public class ConnectIQTask extends BaseGarminTask
{
    @TaskAction
    void start()
    {
        execTask(Collections.emptyList());
    }

    @Override
    public String getExecName()
    {
        return "connectiq";
    }

    @Override
    public List<String> getArgs()
    {
        return null;
    }
}
