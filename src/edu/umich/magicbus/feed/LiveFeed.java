package edu.umich.magicbus.feed;

import java.util.ArrayList;
import java.util.Collections;

import edu.umich.magicbus.IPath;
import edu.umich.magicbus.LatLong;

/**
 * This class represents a Live Magic Bus feed. It provides an interface for querying, and to
 * refresh itself.
 * @author Gopalkrishna Sharma
 * Email: gopalkri@umich.edu
 */
public class LiveFeed
{
    /**
     * Fetches and constructs the feed.
     * @throws FeedException
     */
    public LiveFeed() throws FeedException
    {
        refresh();
    }

    /**
     * Refreshes the feed by fetching it from the magic bus public feed.
     * @throws FeedException
     */
    public static void refresh() throws FeedException
    {
        mPaths = null;
        mCurrentPath = 0;
        mFeed = new Feed(Utilities.fetchFeedFromURL(cFeedURL));
    }

    /**
     * Computes all paths from start to end.
     * @param start Starting location.
     * @param end Ending location.
     */
    public static void computePaths(LatLong start, LatLong end)
    {
        mPaths = new ArrayList<Path>();
        mCurrentPath = 0;
        ArrayList<Stop> srcStops = mFeed.getStopsInRangeOf(start, cMaxWalkingDistance);
        if (srcStops == null)
        {
            mPaths = null;
            return;
        }

        for (Stop srcStop : srcStops)
        {
            Route srcRoute = srcStop.getRoute();
            ArrayList<Stop> destStops = srcRoute.getStopsInRangeOf(end, cMaxWalkingDistance, srcStop);
            if (destStops == null)
            {
                continue;
            }
            for (Stop destStop : destStops)
            {
                double srcToa = srcStop.getEarliestArrivalTime();
                int busId = srcStop.getIdOfNextBus();
                double destToa = destStop.getArrivalTimeOf(busId);
                double travelTime = destToa - srcToa;
                if (travelTime < 0)
                {
                    continue;
                }

                double walkingDistance = Utilities.getDistanceBetween(start, srcStop.getLocation());
                walkingDistance += Utilities.getDistanceBetween(end, destStop.getLocation());
                assert (walkingDistance > 0);

                //double walkTime = Utilities.getWalkTime(start, srcStop.getLocation());
                //walkTime += Utilities.getWalkTime(end, destStop.getLocation());
                //assert (walkTime > 0);

                double walkTime = Utilities.getWalkTime(walkingDistance);
                walkTime += Utilities.getWalkTime(walkingDistance);
                travelTime += walkTime;

                mPaths.add(new Path(srcRoute, srcStop, destStop, travelTime, walkingDistance));
            }
        }
        Collections.sort(mPaths);
        if (mPaths.size() < 1)
        {
            mPaths = null;
        }

        // DEBUG STATEMENT - printPaths();
    }

    /**
     * Returns the best path found from the previous call to computePath.
     * @return Best path.
     * @throws NoPathsFoundException
     */
    public static IPath getBestPath() throws NoPathsFoundException
    {
        if (mPaths == null)
        {
            throw new NoPathsFoundException("No paths found.");
        }

        return mPaths.get(0);
    }

    /**
     * Returns the next path found from previous call to computePath. The object keeps track of
     * the number of calls to this function and getPreviousPath. They serve as iterators. If there
     * are no more paths to return, null is returned. However, the state can be reverted with a
     * call to getPreviousPath.
     * @return Next path in list of all found paths.
     * @throws NoPathsFoundException
     */
    public static IPath getNextPath() throws NoPathsFoundException
    {
        if (mPaths == null)
        {
            throw new NoPathsFoundException("No paths found.");
        }

        if (mCurrentPath >= mPaths.size())
        {
            return null;
        }

        return mPaths.get(mCurrentPath++);
    }

    /**
     * Returns the previous path found from previous call to computePath. Use this function in
     * conjunction with getNextPath. It serves the purpose of decrementing an iterator.
     * @return Previous path in list of all found paths.
     * @throws NoPathsFoundException
     */
    public static IPath getPreviousPath() throws NoPathsFoundException
    {
        if (mPaths == null)
        {
            throw new NoPathsFoundException("No paths found.");
        }

        if (mCurrentPath < 0)
        {
            return null;
        }

        return mPaths.get(mCurrentPath--);
    }

    /**
     * Feed as of the most recent refresh.
     */
    private static Feed mFeed;

    /**
     * All paths found from the previous call to computePaths.
     */
    private static ArrayList<Path> mPaths;

    /**
     * Position of current Path in sequence of iteration through mPaths via getNextPath and
     * getPreviousPath.
     */
    private static int mCurrentPath;

    /**
     * URL that contains Magic Bus feed.
     */
    //private final String cFeedURL = "http://mbus.pts.umich.edu/shared/public_feed.xml";
    private final static String cFeedURL = "http://www-personal.umich.edu/~gopalkri/feed.xml";

    /**
     * Maximum walking distance used as a radius.
     */
    private final static double cMaxWalkingDistance = 750.0;



    //======DEBUG====

    private static void printPaths()
    {
        if (mPaths == null || mPaths.size() < 1)
        {
            System.out.println("No paths found!");
            return;
        }
        for (Path path : mPaths)
        {
            System.out.print("Path: Route: ");
            System.out.print(path.getRoute().getName());
            System.out.print(" Start Stop: ");
            System.out.print(path.getStartStop().getUniqueName());
            System.out.print(" End Stop: ");
            System.out.print(path.getEndStop().getUniqueName());
            System.out.print(" Duration: ");
            System.out.print(path.getDuration());
            System.out.print(" Walking Distance: ");
            System.out.println(path.getWalkingDistance());
        }
    }

    //======END DEBUG====
}
