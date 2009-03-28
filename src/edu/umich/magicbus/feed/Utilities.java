package edu.umich.magicbus.feed;

import java.util.ArrayList;

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

        double dlat = blat - alat;
        double dlon = blon - alon;

        double sin1 = Math.sin(dlat / 2);
        sin1 = sin1 * sin1;

        double sin2 = Math.sin(dlon / 2);
        sin2 = sin2 * sin2;

        double tmp1 = sin1 + (Math.cos(alat) * Math.cos(blat) * sin2);
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

    private static final double cRadiusOfEarth = 6378100;
}
