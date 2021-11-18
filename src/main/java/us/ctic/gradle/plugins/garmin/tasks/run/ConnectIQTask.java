package us.ctic.gradle.plugins.garmin.tasks.run;

import us.ctic.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.tasks.TaskAction;

import java.util.Collections;
import java.util.List;

/**
 * Task for starting the ConnectIQ application
 */
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
