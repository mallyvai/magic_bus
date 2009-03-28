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
     * Latitude of this LatLong object.
     */
    private double mLatitude;

    /**
     * Longitude of this LatLog object.
     */
    private double mLongitude;
}
