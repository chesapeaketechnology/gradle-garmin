package us.ctic.gradle.plugins.garmin.tasks.test;

import us.ctic.gradle.plugins.garmin.tasks.BaseGarminTask;
import us.ctic.gradle.plugins.garmin.tasks.common.IFlushListener;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

/**
 * Base test task which integrates into the test phase of the build
 */
public abstract class BaseTestTask extends BaseGarminTask implements IFlushListener
{
    @TaskAction
    public void run()
    {
        errorStream.setFlushListener(this);
        infoStream.setFlushListener(this);
        execTask(getArgs());
    }

    @Override
    public void flushing(String toBeFlushed)
    {
        if (toBeFlushed.contains("Error:"))
        {
            throw new GradleException(toBeFlushed);
        }
    }
}
