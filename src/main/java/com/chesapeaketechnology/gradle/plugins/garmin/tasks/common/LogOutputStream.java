package com.chesapeaketechnology.gradle.plugins.garmin.tasks.common;

import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;

import java.io.ByteArrayOutputStream;

public class LogOutputStream extends ByteArrayOutputStream
{
    private final Logger logger;
    private final LogLevel level;
    private IFlushListener listener;

    public LogOutputStream(Logger logger, LogLevel level)
    {
        this.logger = logger;
        this.level = level;
    }

    public Logger getLogger()
    {
        return logger;
    }

    public LogLevel getLevel()
    {
        return level;
    }

    public void setFlushListener(IFlushListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void flush()
    {
        logger.log(LogLevel.ERROR, toString());
        if (listener != null)
        {
            listener.flushing(toString());
        }
        reset();
    }
}
