package com.example.remoteeyes;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class GPSActivity extends Activity {

	LocationManager mlocManager = null;
    LocationListener mlocListener = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        
        /* Use the LocationManager class to obtain GPS locations */

        getApplicationContext();
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	mlocManager.removeUpdates(mlocListener);
    }
    
    
    /* Class My Location Listener */

    public class MyLocationListener implements LocationListener
    {

	    @Override
	    public void onLocationChanged(Location loc) {
	    	
	    	String longitude = " W; ", latitude = " S";
	    	if(loc.getLatitude() > 0) longitude = " E; ";
	    	if(loc.getLongitude() > 0) latitude = " N";
		    String Text = "Pozycja GPS: " + loc.getLongitude() + longitude + loc.getLatitude() + latitude;

			Intent result = new Intent();
			result.putExtra("gps_location", Text);
			setResult(Activity.RESULT_OK, result);
			finish();
	    }
	
	    @Override
	    public void onProviderDisabled(String provider) {
	    	Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
	    }
	
	    @Override
	    public void onProviderEnabled(String provider) {
	    	Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }


    }/* End of Class MyLocationListener */
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
