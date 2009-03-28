package edu.umich.magicbus.feed;

public class NoPathsFoundException extends FeedException
{
    private static final long serialVersionUID = -5188286527260696440L;

    public NoPathsFoundException(String message)
    {
        super(message);
    }
}
