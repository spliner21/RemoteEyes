package com.example.remoteeyes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BlindActivity extends Activity {

	static int SETTINGS_CODE = 100;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);
        
        final Handler h = new Handler();
        
        Button countBtn = (Button) findViewById(R.id.button1);
        
        
        countBtn.setOnClickListener(new OnClickListener() {

            int counter = 0;
            
            Runnable r = new Runnable() {

    			@Override
    			public void run() {
        	    	switch(counter) {
        			case 1:
        				counter = 0;
        				// TODO obs³uga foto + audio lub tylko audio
        				break;
        			case 2:
        				counter = 0;
        				// TODO obs³uga video
        				break;
        			case 3:
        				counter = 0;
        				Intent settingsIntent = new Intent(getApplicationContext(),Settings.class);
        				startActivityForResult(settingsIntent,SETTINGS_CODE);
        				break;
        			case 4:
        				finish();
        				break;
        			default:
        				counter = 0;
        				break;
        			}
    				
    			}
            };
        	
        	
			@Override
			public void onClick(View v) {
				
				// TODO Poprawiæ!
				counter = (counter+1)%5;
				h.removeCallbacks(r);
				
				h.postDelayed(r, 1500);
				
			}
		});
        

        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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

