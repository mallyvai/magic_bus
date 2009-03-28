package edu.umich.magicbus;

import android.app.Activity;
import android.os.Bundle;
import edu.umich.magicbus.feed.FeedException;
import edu.umich.magicbus.feed.LiveFeed;

public class MagicBusView extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try
        {
            LiveFeed lf = new LiveFeed();
        }
        catch (FeedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}