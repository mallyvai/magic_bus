package edu.umich.magicbus.feed;

import java.util.ArrayList;

import edu.umich.magicbus.IRoute;
import edu.umich.magicbus.LatLong;

class Route implements IRoute
{
    public Route(String xml) throws XMLException
    {
        if ((-1 == xml.indexOf("<route>")) || (-1 == xml.indexOf("</route>")))
        {
            throw new XMLException("Invalid xml in route.");
        }

        int start = xml.indexOf("<name>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in route.");
        }
        start += 6;
        int end = xml.indexOf("</name>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in route.");
        }
        mName = xml.substring(start, end);

        start = xml.indexOf("<id>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in route.");
        }
        start += 4;
        end = xml.indexOf("</id>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in route.");
        }
        mId = Integer.parseInt(xml.substring(start, end));

        start = xml.indexOf("<topofloop>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in route.");
        }
        start += 11;
        end = xml.indexOf("</topofloop>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in route.");
        }
        mTopOfLoop = Integer.parseInt(xml.substring(start, end));

        start = xml.indexOf("<stopcount>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in route.");
        }
        start += 11;
        end = xml.indexOf("</stopcount>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in route.");
        }
        int stopCount = Integer.parseInt(xml.substring(start, end));

        start = 0;
        end = 0;
        while (start < xml.length())
        {
            start = xml.indexOf("<stop>", start);
            if (start == -1)
            {
                break;
            }
            end = xml.indexOf("</stop>", start);
            if (end == -1)
            {
                throw new XMLException("Invalid stop tag.");
            }
            end += 7;
            mStops.add(new Stop(xml.substring(start, end), this));
            start = end;
        }

        if (stopCount != mStops.size())
        {
            throw new XMLException("Number of stops does not match stopcount.");
        }
    }

    /**
     * @see edu.umich.magicbus.IRoute#getName()
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Gets Route's Id.
     * @return
     */
    public int getId()
    {
        return mId;
    }

    /**
     * Returns true if route is a loop. False otherwise.
     * @return Whether the route is a loop.
     */
    public boolean isLoop()
    {
        return (mTopOfLoop == 0);
    }

    /**
     * Gets all stops on this route within radius meters of loc (walking distance).
     * @param loc Source location.
     * @param radius Range in meters (walking distance).
     * @return All stops on this route in range of loc.
     */
    public ArrayList<Stop> getStopsInRangeOf(LatLong loc, double radius)
    {
        return Utilities.getStopsInRangeOf(loc, radius, mStops);
    }

    private String mName;

    private int mId;

    private int mTopOfLoop;

    private ArrayList<Stop> mStops;
}
