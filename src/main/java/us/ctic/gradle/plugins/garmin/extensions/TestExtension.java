package us.ctic.gradle.plugins.garmin.extensions;

/**
 * Gradle extension for the Test configuration
 */
public class TestExtension
{
    private String device;

    public String getDevice()
    {
        return device;
    }

    public void setDevice(String device)
    {
        this.device = device;
    }
}
