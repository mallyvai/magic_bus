package edu.umich.magicbus;

/**
 * This is the interface for a Path which a user takes to get from one location to another.
 * @author Gopalkrishna Sharma
 * Email: gopalkri@umich.edu
 */
public interface IPath
{
    /**
     * Gets the route for this path.
     * @return Route to take for this path.
     */
    public IRoute getRoute();

    /**
     * Gets the start stop for this path.
     * @return Start stop for this path.
     */
    public IStop getStartStop();

    /**
     * Gets the end stop for this path.
     * @return End stop for this path.
     */
    public IStop getEndStop();

    /**
     * Gets the estimated time of travel for this path. Time is measured in seconds.
     * @return Estimated duration of travel in seconds.
     */
    public double getDuration();

    /**
     * Gets the estimated walking distance involved in taking this path. This distance includes
     * both the distance from start location to start stop, and from end stop to end location.
     * @return Estimated walking distance involved in this path.
     */
    public double getWalkingDistance();
}
