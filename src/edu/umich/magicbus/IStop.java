package edu.umich.magicbus;

public interface IStop
{
    /**
     * Gets the name for this stop. This name is not necessarily unique, and is intended to give a
     * human being a good idea of what the stop is.
     * @return Name of stop.
     */
    public String getName();

    /**
     * Returns the unique name of this stop. The stop name may not make much sense to a human.
     * @return Unique name of stop.
     */
    public String getUniqueName();

    /**
     * Returns the location of this stop.
     * @return Location of stop.
     */
    public LatLong getLocation();
}
