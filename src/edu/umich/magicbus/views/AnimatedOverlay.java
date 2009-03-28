package edu.umich.magicbus.views;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class AnimatedOverlay extends Overlay {
	private final int mRadius = 5;
	private int increment = 0;
	private int vector = 1;
	
	Location location;
	
	//Why does this function need to be a boolean?
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
		Projection projection = mapView.getProjection();
		
		if(shadow == false) {
			Double latitude = location.getLatitude()*1E6;
			Double longitude = location.getLongitude()*1E6;
				
				
				if (increment < 10 && vector == 1) {
					increment++;
				} else if (increment == 10 && vector == 1) {
					vector = -1;
					increment--;
				} else if (increment > 0 && vector == -1) {
					increment--;
				} else if (increment == 0 && vector == -1) {
					increment++;
					vector = 1;
				}
				
			
				latitude += increment*10*vector;
				longitude += increment*10*vector;
				GeoPoint geoPoint;
				geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());
				
				Point point = new Point();
				projection.toPixels(geoPoint, point);
				
				RectF oval = new RectF(point.x - mRadius, point.y - mRadius,
										point.x + mRadius, point.y + mRadius);
				
				Paint paint = new Paint();
				paint.setARGB(250, 255, 0, 0);
				paint.setAntiAlias(true);
				paint.setFakeBoldText(true);
				
				Paint backPaint = new Paint();
				backPaint.setARGB(175, 50, 50, 50);
				backPaint.setAntiAlias(true);
				
				RectF backRect = new RectF(point.x + 2 + mRadius,
											point.y - 3*mRadius,
											point.x + 65, point.y+mRadius);
				//Draw the marker
				canvas.drawOval(oval, paint);
				canvas.drawRoundRect(backRect, 5, 5, backPaint);
				
				canvas.drawText("Here I am", point.x + 2*mRadius, point.y, paint);
		}
		super.draw(canvas, mapView, shadow, when);
		return true;
	}
	
	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		return false;
	}
	
	
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}	
}