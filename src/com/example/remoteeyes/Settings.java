package com.example.remoteeyes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class Settings extends Activity {

    Boolean gps = false;
    String mode = "blind";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox attachGPS = (CheckBox) findViewById(R.id.attach_gps);
        RadioButton sandblind = (RadioButton) findViewById(R.id.sandblind_radio);
        RadioButton blind = (RadioButton) findViewById(R.id.blind_radio);
        Button save = (Button) findViewById(R.id.save);

		DBHelper dbh = new DBHelper(getBaseContext());

	    gps = Boolean.parseBoolean(dbh.getConfigValue("gps").toString());
	    mode = dbh.getConfigValue("mode").toString();
	    if(mode.equals("")) mode = "sandblind";
	    if(mode.equals("blind")) {
	        sandblind.setChecked(false);
	        blind.setChecked(true);
	    } else {
	        sandblind.setChecked(true);
	        blind.setChecked(false);
	    }
	    
        attachGPS.setChecked(gps);
        
        attachGPS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					gps = true;
				}
				else gps = false;
			}
		});
        
        sandblind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked) {
			        RadioButton blind = (RadioButton) findViewById(R.id.blind_radio);
					mode = "sandblind";
					if(blind.isChecked()) {
						blind.setChecked(false);
					}
				}
			}
		});
        
        blind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked) {
			        RadioButton sandblind = (RadioButton) findViewById(R.id.sandblind_radio);
					mode = "blind";
					if(sandblind.isChecked()) {
						sandblind.setChecked(false);
					}
				}
			}
		});
        

        save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DBHelper dbh = new DBHelper(getBaseContext());
				dbh.getWritableDatabase();
				
				dbh.insertConfigValue("mode", mode);
				if(gps) {
					dbh.insertConfigValue("gps", "true");
				}
				else {
					dbh.insertConfigValue("gps", "false");
				}
				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("result",mode);
				setResult(RESULT_OK,returnIntent);     
								
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
