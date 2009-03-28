package edu.umich.magicbus.busfeed;

import org.w3c.dom.Element;

import edu.umich.magicbus.LatLong;
import edu.umich.magicbus.feed.Utilities;

public class Bus
{
    public Bus(Element el)
    {
        mId = Utilities.getIntValue(el, "id");
        double lat = Utilities.getDoubleValue(el, "latitude");
        double lon = Utilities.getDoubleValue(el, "longitude");
        mLocation = new LatLong(lat, lon);
        mRouteName = Utilities.getTextValue(el, "route");
        mHeading = Utilities.getIntValue(el, "heading");
        String color = Utilities.getTextValue(el, "busroutecolor");
        mColor = Integer.parseInt(color, 16);
    }

    public int getId()
    {
        return mId;
    }

    public int getHeading()
    {
        return mHeading;
    }

    public LatLong getLocation()
    {
        return mLocation;
    }

    public String getRouteName()
    {
        return mRouteName;
    }

    public int getColor()
    {
        return mColor;
    }

    private int mId;
    private LatLong mLocation;
    private String mRouteName;
    private int mColor;
    private int mHeading;
}
