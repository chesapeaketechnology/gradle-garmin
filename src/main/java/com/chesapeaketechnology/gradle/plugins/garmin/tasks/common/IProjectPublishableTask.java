package com.chesapeaketechnology.gradle.plugins.garmin.tasks.common;

import com.chesapeaketechnology.gradle.plugins.garmin.tasks.BaseGarminTask;
import org.gradle.api.Project;

public interface IProjectPublishableTask<T extends BaseGarminTask>
{
    void configurePublishing(Project project, T buildGarminTask);
}
