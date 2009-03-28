package edu.umich.magicbus.feed;

import edu.umich.magicbus.IStop;
import edu.umich.magicbus.LatLong;

class Stop implements IStop
{
    public Stop(String xml, Route owner) throws XMLException
    {
        int start = xml.indexOf("<name>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        start += 6;
        int end = xml.indexOf("</name>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        mUniqueName = xml.substring(start, end);

        start = xml.indexOf("<name2>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        start += 7;
        end = xml.indexOf("</name2>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        mOtherName = xml.substring(start, end);

        start = xml.indexOf("<name3>");
        if (-1 == start)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        start += 7;
        end = xml.indexOf("</name3>");
        if (-1 == end)
        {
            throw new XMLException("Invalid xml in stop.");
        }
        String tmp = xml.substring(start, end);
        if (tmp.compareTo("None") != 0)
        {
            mOtherName += tmp;
        }

        start = xml.indexOf("<latitude>");
        if (-1 == start)
        {
            throw new XMLException("Invalid latitude tag in stop.");
        }
        start += 10;
        end = xml.indexOf("</latitude>");
        if (-1 == end)
        {
            throw new XMLException("Invalid latitude tag in stop.");
        }
        double lat = Double.parseDouble(xml.substring(start, end));

        start = xml.indexOf("<longitude>");
        if (-1 == start)
        {
            throw new XMLException("Invalid longitude tag in stop.");
        }
        start += 11;
        end = xml.indexOf("</longitude>");
        if (-1 == end)
        {
            throw new XMLException("Invalid longitude tag in stop.");
        }
        double lon = Double.parseDouble(xml.substring(start, end));
        mLocation = new LatLong(lat, lon);

        int toaCountStart = xml.indexOf("<toacount>");
        if (-1 == toaCountStart)
        {
            throw new XMLException("Invalid toacount tag.");
        }
        end = xml.indexOf("</toacount>");
        if (-1 == end)
        {
            throw new XMLException("Invalid toacount tag.");
        }
        int toaCount = Integer.parseInt(xml.substring(toaCountStart + 10, end));

        mTimesToArrival = new double[toaCount];
        mBusIds = new int[toaCount];
        int i = 0;

        while (start < xml.length())
        {
            start = xml.indexOf("<toa", start);
            if (-1 == start)
            {
                throw new XMLException("Invalid toa tag in stop.");
            }
            if (toaCountStart == start)
            {
                break;
            }
            start = xml.indexOf(">", start);
            if (-1 == start)
            {
                throw new XMLException("Invalid toa tag in stop.");
            }
            start ++;
            end = xml.indexOf("</toa", start);
            if (-1 == end)
            {
                throw new XMLException("Invalid toa tag in stop.");
            }
            double toa = Double.parseDouble(xml.substring(start, end));

            start = xml.indexOf("<id", start);
            if (-1 == start)
            {
                throw new XMLException("Invalid id tag in stop.");
            }
            start = xml.indexOf(">", start);
            if (-1 == start)
            {
                throw new XMLException("Invalid id tag in stop.");
            }
            start ++;
            end = xml.indexOf("</id", start);
            if (-1 == end)
            {
                throw new XMLException("Invalid id tag in stop.");
            }
            int id = Integer.parseInt(xml.substring(start, end));

            mTimesToArrival[i] = toa;
            mBusIds[i++] = id;

            start = end;
        }

        if (toaCount != i)
        {
            throw new XMLException("toa count does not match.");
        }
    }

    /**
     * @see edu.umich.magicbus.IStop#getLocation()
     */
    public LatLong getLocation()
    {
        return mLocation;
    }

    /**
     * @see edu.umich.magicbus.IStop#getName()
     */
    public String getName()
    {
        return mOtherName;
    }

    /**
     * @see edu.umich.magicbus.IStop#getUniqueName()
     */
    public String getUniqueName()
    {
        return mUniqueName;
    }

    public double getEarliestArrivalTime()
    {
        // TODO Incomplete definition.
        return -1;
    }

    public int getIdOfNextBus()
    {
        // TODO Incomplete definition.
        return -1;
    }

    public double getArrivalTimeOf(int busId)
    {
        // TODO Incomplete definition.
        return -1;
    }

    public Route getRoute()
    {
        return mRoute;
    }

    private Route mRoute;

    private String mUniqueName;

    private String mOtherName;

    private LatLong mLocation;

    private double[] mTimesToArrival;

    private int[] mBusIds;
}
