package edu.umich.magicbus.feed;

import edu.umich.magicbus.IPath;
import edu.umich.magicbus.IRoute;
import edu.umich.magicbus.IStop;

class Path implements IPath, Comparable<Path>
{
    public Path(Route route, Stop srcStop, Stop destStop, double duration, double walkingDistance)
    {
        mRoute = route;
        mSourceStop = srcStop;
        mDestinationStop = destStop;
        mDuration = duration;
        mWalkingDistance = walkingDistance;
    }

    public double getDuration()
    {
        return mDuration;
    }

    public IStop getEndStop()
    {
        return mDestinationStop;
    }

    public IRoute getRoute()
    {
        return mRoute;
    }

    public IStop getStartStop()
    {
        return mSourceStop;
    }

    public double getWalkingDistance()
    {
        return mWalkingDistance;
    }

    public int compareTo(Path otherPath)
    {
        double otherDuration = otherPath.getDuration();
        if (mDuration < otherDuration)
        {
            return -1;
        }
        else if (mDuration > otherDuration)
        {
            return 1;
        }
        return 0;
    }

    private Route mRoute;
    private Stop mSourceStop;
    private Stop mDestinationStop;
    private double mDuration;
    private double mWalkingDistance;
}
