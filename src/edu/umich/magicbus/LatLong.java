package edu.umich.magicbus;

/**
 * This is a representation of a Latitude, Longitude pair.
 * @author Gopalkrishna Sharma
 * Email: gopalkri@umich.edu
 */
public class LatLong
{
    /**
     * Initializes this LatLong object with latitude and longitude.
     * @param latitude Latitude to initialize with.
     * @param longitude Longitude to initialize with.
     */
    public LatLong(double latitude, double longitude)
    {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    /**
     * Gets the latitude of this LatLong pair.
     * @return Latitude.
     */
    public double getLatitude()
    {
        return mLatitude;
    }

    /**
     * Gets the longitude of this LatLong pair.
     * @return Longitude.
     */
    public double getLongitude()
    {
        return mLongitude;
    }

    /**
     * Returns the latitude times 1E6 as an integer.
     * @return Latitude * 1E6.
     */
    public int getLatitudeE6()
    {
        Double tmp = mLatitude * 1E6;
        return tmp.intValue();
    }

    /**
     * Returns the longitude times 1E6 as an integer.
     * @return Longitude * 1E6.
     */
    public int getLongitudeE6()
    {
        Double tmp = mLongitude * 1E6;
        return tmp.intValue();
    }

    /**
     * Latitude of this LatLong object.
     */
    private double mLatitude;

    /**
     * Longitude of this LatLog object.
     */
    private double mLongitude;
}
