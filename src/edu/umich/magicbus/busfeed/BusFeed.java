package edu.umich.magicbus.busfeed;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.umich.magicbus.feed.FeedException;

public class BusFeed
{
    public BusFeed() throws FeedException
    {
        mPreviousFetch = System.currentTimeMillis();
        fetchFeed();
    }

    public ArrayList<Bus> getBuses() throws FeedException
    {
        long curTime = System.currentTimeMillis();
        if (curTime - mPreviousFetch >= cRefetchThreshold)
        {
            fetchFeed();
        }
        return mBuses;
    }

    private void fetchFeed() throws FeedException
    {
        mPreviousFetch = cRefetchThreshold;
        mBuses = new ArrayList<Bus>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(cFeedURL);
        }
        catch (ParserConfigurationException e)
        {
            String errMsg = "Unable to parse Magic Bus feed found at: " + cFeedURL + ".\n";
            errMsg += "Details: " + e.getMessage();

            throw new FeedException(errMsg);
        }
        catch (SAXException e)
        {
            String errMsg = "Unable to parse Magic Bus feed found at: " + cFeedURL + ".\n";
            errMsg += "Details: " + e.getMessage();

            throw new FeedException(errMsg);
        }
        catch (IOException e)
        {
            String errMsg = "Unable to parse Magic Bus feed found at: " + cFeedURL + ".\n";
            errMsg += "Details: " + e.getMessage();

            throw new FeedException(errMsg);
        }

        Element docElement = doc.getDocumentElement();
        NodeList nl = docElement.getElementsByTagName("item");
        if(nl != null && nl.getLength() > 0)
        {
            for(int i = 0 ; i < nl.getLength();i++)
            {
                Element el = (Element)nl.item(i);
                Bus bus = new Bus(el);
                mBuses.add(bus);
            }
        }

        mBuses.trimToSize();
    }

    private ArrayList<Bus> mBuses;

    private long mPreviousFetch;

    private static final String cFeedURL = "http://mbus.pts.umich.edu/shared/location_feed.xml";

    /**
     * Time in milli seconds between consecutive fetches.
     */
    private static final int cRefetchThreshold = 1000;
}
