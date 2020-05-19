package com.chesapeaketechnology.gradle.plugins.garmin.tasks.common;

import java.util.List;

/**
 * Interface which describes an object that has an executable program to be run
 */
public interface IExecutable
{
    /**
     * The name of the executable to be run
     * @return String name
     */
    String getExecName();

    /**
     * List of String arguments to pass to the executable
     * @return {@link List} of String arguments
     */
    List<String> getArgs();
}
