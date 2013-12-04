package com.example.remoteeyes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	static int SETTINGS_CODE = 100;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		DBHelper dbh = new DBHelper(getBaseContext());
		String mode = dbh.getConfigValue("mode");
		dbh.close();
		
		if(mode.equals("")) {
			Intent settings = new Intent(getApplicationContext(),Settings.class);
			startActivityForResult(settings,SETTINGS_CODE);
		}
		else {
			if(mode.equals("blind")) {
				Intent blind = new Intent(getApplicationContext(),BlindActivity.class);
				startActivity(blind);
			} else if(mode.equals("sandblind")) {
				Intent sandblind = new Intent(getApplicationContext(),SandBlind.class);
				startActivity(sandblind);
			}

	    	finish();
		}
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	if (requestCode == SETTINGS_CODE) {

    		if(resultCode == RESULT_OK){
    			String result=data.getStringExtra("result");
    			
    	      	if(result.equals("blind")) {
  					Intent blind = new Intent(getApplicationContext(),BlindActivity.class);
  					startActivity(blind);
  					
	  			} else if(result.equals("sandblind")) {
	  				Intent sandblind = new Intent(getApplicationContext(),SandBlind.class);
	  				startActivity(sandblind);
	  				
	  			}
	    	}
	
	    	if (resultCode == RESULT_CANCELED) {
	    	   finish();
	    	}
    	}
    	finish();
    }
}
