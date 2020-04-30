package com.chesapeaketechnology.gradle.plugins.garmin.tasks.common;

import java.util.List;

public interface IExecutable
{
    String getExecName();

    List<String> getArgs();
}
