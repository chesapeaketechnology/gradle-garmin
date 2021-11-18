package us.ctic.gradle.plugins.garmin.tasks.common;

/**
 * Listener interface that classes can implement to get stream flush notifications.
 */
public interface IFlushListener
{
    /**
     * Called when the stream is about to be flushed
     * @param toBeFlushed the String stream before flushing
     */
    void flushing(String toBeFlushed);
}
