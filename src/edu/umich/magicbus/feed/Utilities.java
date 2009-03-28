package edu.umich.magicbus.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.umich.magicbus.LatLong;

/**
 * Contains general utility functions for use throughout the project.
 *
 * @author Gopalkrishna Sharma
 */
public class Utilities
{
    /**
     * Gets the distance (in meters) from a to b. The distance is approximated by the Haversine
     * formula. Details can be found at: http://www.movable-type.co.uk/scripts/latlong.html
     * @param a First point.
     * @param b Second point.
     * @return Approximate distance in meters from a to b.
     */
    public static double getDistanceBetween(LatLong a, LatLong b)
    {
        double alat = a.getLatitude();
        double alon = a.getLongitude();
        double blat = b.getLatitude();
        double blon = b.getLongitude();

        double dlat = (blat - alat) * Math.PI / 180.0; // conversion from degrees to radians.
        double dlon = (blon - alon) * Math.PI / 180.0;

        double sin1 = Math.sin(dlat / 2);
        sin1 = sin1 * sin1;

        double sin2 = Math.sin(dlon / 2);
        sin2 = sin2 * sin2;

        double tmp1 = sin1 +
            (Math.cos(alat * Math.PI / 180.0) * Math.cos(blat * Math.PI / 180.0) * sin2);
        double tmp2 = 2.0 * Math.atan2(Math.sqrt(tmp1), Math.sqrt(1.0 - tmp1));

        return cRadiusOfEarth * tmp2;
    }

    public static double getWalkTime(LatLong a, LatLong b)
    {
        return -1;
    }

    /**
     * Gets all stops within radius meters of loc. Distance measured is the walking distance. All
     * stops in allStops are considered.
     * @param loc Source location.
     * @param radius Range in meters (walking distance).
     * @param allStops All stops to be considered in search.
     * @return All stops in range of loc.
     */
    public static ArrayList<Stop> getStopsInRangeOf(LatLong loc, double radius,
            ArrayList<Stop> allStops)
    {
        ArrayList<Stop> ret = new ArrayList<Stop>();
        for (Stop stop : allStops)
        {
            LatLong cur = stop.getLocation();
            double distance = Utilities.getDistanceBetween(cur, loc);
            if (distance <= radius)
            {
                ret.add(stop);
            }
        }
        if (ret.size() == 0)
        {
            return null;
        }
        return ret;
    }

    public static String fetchFeedFromURL(String url) throws FeedException
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                builder.append(line);
            }
            br.close();
            return builder.toString();
        }
        catch (MalformedURLException ex)
        {
            String errMsg = "Magic Bus feed url is invalid.\n";
            errMsg += "Perhaps it has been changed since the creation of this application.\n";
            errMsg += "URL used by this application is: ";
            errMsg += url;

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
     * Finds tagName in element and gets the text content.
     */
    public static String getTextValue(Element ele, String tagName)
    {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0)
        {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }


    /**
     * Finds tagName in element and parses it to an integer value.
     */
    public static int getIntValue(Element ele, String tagName)
    {
        return Integer.parseInt(getTextValue(ele,tagName));
    }

    public static double getDoubleValue(Element ele, String tagName)
    {
        return Double.parseDouble(getTextValue(ele, tagName));
    }

    private static final double cRadiusOfEarth = 6378100;
}
