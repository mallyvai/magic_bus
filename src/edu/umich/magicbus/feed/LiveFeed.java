package edu.umich.magicbus.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
    public void refresh() throws FeedException
    {
        mPaths = null;
        mCurrentPath = 0;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(cFeedURL).openStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                builder.append(line);
            }
            br.close();
            mFeed = new Feed(builder.toString());
        }
        catch (MalformedURLException ex)
        {
            String errMsg = "Magic Bus public feed url is invalid.\n";
            errMsg += "Perhaps it has been changed since the creation of this application.\n";
            errMsg += "URL used by this application is: ";
            errMsg += cFeedURL;

            throw new FeedException(errMsg);
        }
        catch (IOException ex)
        {
            String errMsg = "Encountered an unexpected error while accessing Magic Bus Feed.\n";
            errMsg += "Details: ";
            errMsg += ex.getMessage();

            throw new FeedException(errMsg);
        }
    }

    /**
     * Computes all paths from start to end.
     * @param start Starting location.
     * @param end Ending location.
     */
    public void computePaths(LatLong start, LatLong end)
    {
        mPaths = new ArrayList<Path>();
        mCurrentPath = 0;
        ArrayList<Stop> srcStops = mFeed.getStopsInRangeOf(start, cMaxWalkingDistance);

        for (Stop srcStop : srcStops)
        {
            Route srcRoute = srcStop.getRoute();
            ArrayList<Stop> destStops = srcRoute.getStopsInRangeOf(end, cMaxWalkingDistance);
            for (Stop destStop : destStops)
            {
                double srcToa = srcStop.getEarliestArrivalTime();
                int busId = srcStop.getIdOfNextBus();
                double destToa = destStop.getArrivalTimeOf(busId);
                double travelTime = destToa - srcToa;
                assert (travelTime > 0);

                double walkingDistance = Utilities.getDistanceBetween(start, srcStop.getLocation());
                walkingDistance += Utilities.getDistanceBetween(end, destStop.getLocation());
                assert (walkingDistance > 0);

                double walkTime = Utilities.getWalkTime(start, srcStop.getLocation());
                walkTime += Utilities.getWalkTime(end, destStop.getLocation());
                assert (walkTime > 0);
                travelTime += walkTime;

                mPaths.add(new Path(srcRoute, srcStop, destStop, travelTime, walkingDistance));
            }
        }
        Collections.sort(mPaths);
        if (mPaths.size() < 1)
        {
            mPaths = null;
        }
    }

    /**
     * Returns the best path found from the previous call to computePath.
     * @return Best path.
     * @throws NoPathsFoundException
     */
    public IPath getBestPath() throws NoPathsFoundException
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
    public IPath getNextPath() throws NoPathsFoundException
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
    public IPath getPreviousPath() throws NoPathsFoundException
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
    private Feed mFeed;

    /**
     * All paths found from the previous call to computePaths.
     */
    private ArrayList<Path> mPaths;

    /**
     * Position of current Path in sequence of iteration through mPaths via getNextPath and
     * getPreviousPath.
     */
    private int mCurrentPath;

    /**
     * URL that contains Magic Bus feed.
     */
    private final String cFeedURL = "http://mbus.pts.umich.edu/shared/public_feed.xml";
    //private final String cFeedURL = "http://www-personal.umich.edu/~gopalkri/feed.xml";

    /**
     * Maximum walking distance used as a radius.
     */
    private final double cMaxWalkingDistance = 750.0;
}
