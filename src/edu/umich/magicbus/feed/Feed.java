package edu.umich.magicbus.feed;

import java.util.ArrayList;

import edu.umich.magicbus.LatLong;

class Feed
{
    public Feed(String xml) throws XMLException, FeedDownException
    {
        if ((-1 == xml.indexOf("<livefeed>")) || (-1 == xml.indexOf("</livefeed>")))
        {
            throw new XMLException("Invalid livefeed tags.");
        }

        int startPos, endPos;
        startPos = xml.indexOf("<routecount>");
        if (-1 == startPos)
        {
            throw new XMLException("Invalid routecount tag.");
        }
        startPos += 12;
        endPos = xml.indexOf("</routecount>");
        if (-1 == endPos)
        {
            throw new XMLException("Invalid routecount tag.");
        }
        int routeCount = Integer.parseInt(xml.substring(startPos, endPos));
        if (routeCount == 0)
        {
            throw new FeedDownException("Route count is 0.");
        }
        mRoutes = new ArrayList<Route>(routeCount);

        startPos = 0;
        endPos = 0;

        while (startPos < xml.length())
        {
            startPos = xml.indexOf("<route>", startPos);
            if (startPos == -1)
            {
                break;
            }
            endPos = xml.indexOf("</route>", startPos);
            if (endPos == -1)
            {
                throw new XMLException("Invalid xml in livefeed.");
            }
            endPos += 8;
            mRoutes.add(new Route(xml.substring(startPos, endPos)));
            startPos = endPos;
        }

        if (routeCount != mRoutes.size())
        {
            throw new XMLException("Number of routes does not match routecount.");
        }
    }

    /**
     * Gets all stops within radius meters of loc. Distance measured is the walking distance.
     * @param loc Source location.
     * @param radius Range in meters (walking distance).
     * @return All stops in range of loc.
     */
    public ArrayList<Stop> getStopsInRangeOf(LatLong loc, double radius)
    {
        return Utilities.getStopsInRangeOf(loc, radius, mStops);
    }

    /**
     * Adds stop to list of stops.
     * @param stop Stop to add.
     */
    public void addStop(Stop stop)
    {
        mStops.add(stop);
    }

    private ArrayList<Route> mRoutes;

    private ArrayList<Stop> mStops = new ArrayList<Stop>();
}
