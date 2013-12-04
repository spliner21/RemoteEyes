package com.example.remoteeyes;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class SandBlind extends Activity implements TextToSpeech.OnInitListener {

	static final int SETTINGS_CODE = 100;
	static final int PHOTO_CODE = 200;
	static final int VIDEO_CODE = 300;
	static final int AUDIO_CODE = 400;
	static final int GPS_CODE = 500;
	static final int DATA_SEND_CODE = 600;

	private DataContainer dataContainer;

	MultimediaController mc = new MultimediaController();
	
	private TextToSpeech tts;
	private Boolean firstrun = true;
	
	public void sayAString(String txt) {
		tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
		return;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandblind);
        
        tts = new TextToSpeech(this, this);
        if(dataContainer == null)
        	dataContainer = new DataContainer();
        
        Button photoBtn = (Button)findViewById(R.id.button_foto);
        Button videoBtn = (Button)findViewById(R.id.button_video);
        Button audioBtn = (Button)findViewById(R.id.button_audio);
        Button settingsBtn = (Button)findViewById(R.id.button_config);
        Button quitBtn = (Button)findViewById(R.id.button_exit);

        LayoutParams params = new LinearLayout.LayoutParams(
        		LayoutParams.MATCH_PARENT,
                0, 0.1f);
        // Sprawdzenie czy w telefonie jest kamera
        if(!(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)))
        {
        	// jeœli nie, to blokujemy przyciski
        	photoBtn.setVisibility(View.INVISIBLE);
        	videoBtn.setVisibility(View.INVISIBLE);
        	photoBtn.setLayoutParams(params);
        	videoBtn.setLayoutParams(params);
        } else
        {
        	// jeœli tak, to blokujemy przycisk "Nagraj dŸwiêk
        	audioBtn.setVisibility(View.INVISIBLE);
        	audioBtn.setLayoutParams(params);
        }
        	// jesli jest to oprogramowujemy obs³ugê naciœniêæ na przyciski
    	
    	// Robienie zdjêcia
    	photoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File f = null;
				
				try {
					f = mc.createImageFile(getString(R.string.album));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.v("SandBlindPhoto","Plik foto:"+Uri.fromFile(f).toString());
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				dataContainer.setFile1path(Uri.fromFile(f).toString().replace("file://", ""));
			    startActivityForResult(takePictureIntent,PHOTO_CODE);

			}
		});
    	

    	// Nagrywanie wideo
    	videoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				File f = null;
				
				try {
					f = mc.createVideoFile(getString(R.string.album));
				} catch (IOException e) {
					e.printStackTrace();
				}

				takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
			    startActivityForResult(takeVideoIntent, VIDEO_CODE);
			}
		});

    	// Robienie zdjêcia
    	audioBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent recordAudioIntent = new Intent(getApplicationContext(),AudioRecorder.class);
				startActivityForResult(recordAudioIntent, AUDIO_CODE);
			}
		});
        // Obs³uga przycisku ustawieñ
    	settingsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent settingsIntent = new Intent(getApplicationContext(),Settings.class);
				startActivityForResult(settingsIntent,SETTINGS_CODE);
			}
		});
    	
    	
    	// Obs³uga przycisku wyjœcia
    	quitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


	@Override
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(new Locale("pl"));

        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "Telefon nie obs³uguje polskiego jêzyka. Komunikaty bêd¹ odczytywane za pomoc¹ syntezy w j. angielskim.");
			tts.setLanguage(Locale.ENGLISH);
			sayAString("Uwaga! Jêzyk polski nie jest obs³ugiwany. Komunikaty bêd¹ odczytywane syntezatorem z innego jêzyka!");
        } else {
            Log.e("TTS", "Jêzyk polski jest obs³ugiwany.");
        }
        if(firstrun) {
	        if(!(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))) {
	        	sayAString("Dostêpne funkcje: Nagrywanie audio, Ustawienia, Wyjœcie.");
	        } else {
	        	sayAString("Dostêpne funkcje: Nagrywanie wideo, Robienie zdjêæ i nagrywanie audio, Ustawienia, Wyjœcie.");
	        }
	        firstrun = false;
        }
        
    } else {
        Log.e("TTS", "Initilization Failed!");
    }
		
	} 
	@Override
	public void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
	

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		DBHelper dbh = new DBHelper(getBaseContext());
    	String result;
		Boolean gps = Boolean.parseBoolean(dbh.getConfigValue("gps"));
    	Intent next;
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case  SETTINGS_CODE:
				result = data.getStringExtra("result");
    			
    	      	if(result.equals("blind")) {
  					Intent blind = new Intent(getApplicationContext(),BlindActivity.class);
  					startActivity(blind);
  					
	  			} else if(result.equals("sandblind")) {
	  				Intent sandblind = new Intent(getApplicationContext(),SandBlind.class);
	  				startActivity(sandblind);
	  				
	  			}
    	      	break;
			case VIDEO_CODE: 
				dataContainer.setFile1path(data.getData().toString().replace("file://", ""));
				if(gps) {
					next = new Intent(getApplicationContext(),GPSActivity.class);
					startActivityForResult(next, GPS_CODE);
				} else {
					next = new Intent(getApplicationContext(),DataSendActivity.class);
			        Log.v("SandBlind", "Wrzucanie zmiennych; wartoœci:  file1 = "+dataContainer.getFile1path()+", file2 = "+dataContainer.getFile2path()+", gps = "+dataContainer.getGpsdata()+".");
					next.putExtra("file1path", dataContainer.getFile1path());
					next.putExtra("file2path", dataContainer.getFile2path());
					next.putExtra("gpsdata", dataContainer.getGpsdata());
					startActivityForResult(next, DATA_SEND_CODE);
				}
				break;
			case PHOTO_CODE: 
				next = new Intent(getApplicationContext(),AudioRecorder.class);
				startActivityForResult(next, AUDIO_CODE);
				break;
			case AUDIO_CODE:
				if(dataContainer.getFile1path() == "") {
					dataContainer.setFile1path(data.getStringExtra("audiopath"));
				}
				else {
					dataContainer.setFile2path(data.getStringExtra("audiopath"));
				}

				if(gps) {
					next = new Intent(getApplicationContext(),GPSActivity.class);
					startActivityForResult(next, GPS_CODE);
				} else {
					next = new Intent(getApplicationContext(),DataSendActivity.class);
			        Log.v("SandBlind", "Wrzucanie zmiennych; wartoœci:  file1 = "+dataContainer.getFile1path()+", file2 = "+dataContainer.getFile2path()+", gps = "+dataContainer.getGpsdata()+".");
					next.putExtra("file1path", dataContainer.getFile1path());
					next.putExtra("file2path", dataContainer.getFile2path());
					next.putExtra("gpsdata", dataContainer.getGpsdata());
					startActivityForResult(next, DATA_SEND_CODE);
				}
				break;
			case GPS_CODE:
    			result = data.getStringExtra("gps_location");
    			dataContainer.setGpsdata(result);

				next = new Intent(getApplicationContext(),DataSendActivity.class);
		        Log.v("SandBlind", "Wrzucanie zmiennych; wartoœci:  file1 = "+dataContainer.getFile1path()+", file2 = "+dataContainer.getFile2path()+", gps = "+dataContainer.getGpsdata()+".");
				next.putExtra("file1path", dataContainer.getFile1path());
				next.putExtra("file2path", dataContainer.getFile2path());
				next.putExtra("gpsdata", dataContainer.getGpsdata());
				startActivityForResult(next, DATA_SEND_CODE);
				break;
			default: 
				dataContainer = new DataContainer();
				break;
			}
			
    	}
		
		else if (resultCode == RESULT_CANCELED) {
    	   finish();
    	}
    }	
}
