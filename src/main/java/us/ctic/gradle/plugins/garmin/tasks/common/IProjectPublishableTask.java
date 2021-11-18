package us.ctic.gradle.plugins.garmin.tasks.common;

import us.ctic.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.Project;

/**
 * Interface that classes can implement to describe themselves as a publishable tasks. Publishable
 * tasks are tied into the maven-publish plugin tasks for publishing to maven repositories.
 * @param <T> Task type
 */
public interface IProjectPublishableTask<T extends BaseGarminTask>
{
    /**
     * Any configuration that needs to occur for publishing to maven repos
     * @param project the Project that the task is being executed on
     * @param buildGarminTask the task to configure
     */
    void configurePublishing(Project project, T buildGarminTask);
}
