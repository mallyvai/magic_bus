package edu.umich.magicbus.feed;

/**
 * This is an exception class that gets thrown when MBusFeed exceptions occur.
 *
 * @author Gopalkrishna Sharma
 * Email: gopalkri@umich.edu || gopalkrishnaps@gmail.com
 */
public class FeedException extends Exception
{
    private static final long serialVersionUID = 441297648039507196L;

    public FeedException(String message)
    {
        mMessage = message;
    }

    @Override
    public String toString()
    {
        return mMessage;
    }

    private String mMessage;
}

