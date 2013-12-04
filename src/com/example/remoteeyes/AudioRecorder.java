package com.example.remoteeyes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioRecorder extends Activity {
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        
        
		Button audiobtn = (Button) findViewById(R.id.audio_btn);
		
		audiobtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MultimediaController mc = new MultimediaController();
				final String filePath = mc.getAudioDirPath(getString(R.string.album));
				final String fileName = mc.audioFileNameGen(".mp4");
				
				final MediaRecorder recorder = new MediaRecorder();
			    ContentValues values = new ContentValues(3);
			    values.put(MediaStore.MediaColumns.TITLE, fileName);
			    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			    recorder.setOutputFile(filePath + "/" + fileName);
			    try {
			      recorder.prepare();
			    } catch (Exception e){
			        e.printStackTrace();
			    }

			    final ProgressDialog mProgressDialog = new ProgressDialog(AudioRecorder.this);
			    mProgressDialog.setTitle(R.string.recordinglbl);
			    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			    mProgressDialog.setButton(getString(R.string.stoprecord), new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				        mProgressDialog.dismiss();
				        
				        recorder.stop();
				        recorder.release();
				        
				        returnResults(filePath +"/"+ fileName);
			        }
			    });

			    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			        public void onCancel(DialogInterface p1) {
			        	
			            recorder.stop();
			            recorder.release();
			        }
			    });
			    recorder.start();
			    
			    mProgressDialog.show();
			}
		});
		
	}
	
	private void returnResults(String returnable) {
		Intent result = new Intent();
		result.putExtra("audiopath", returnable);
		setResult(Activity.RESULT_OK, result);
		finish();
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
