package com.example.remoteeyes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class MultimediaController {
    
    public File createImageFile(String album) throws IOException {
        // Create an image file name
        String imageFileName = 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = File.createTempFile( imageFileName, ".jpg", getAlbumDir(album) );
       
        return image;
    }

    public File createVideoFile(String album) throws IOException {
        // Create an image file name
        String videoFileName = 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File video = File.createTempFile( videoFileName, ".mp4", getVideoDir(album) );
       
        return video;
    }
    
    public String audioFileNameGen(String format) {
        // Create an image file name
        String timeStamp = 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = timeStamp + format;
        return audioFileName;
    }

    public String getAudioDirPath(String album) {
        File storageDir = new File(
	    Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MUSIC
	        ), album
	    );	
        storageDir.mkdirs();
        return storageDir.getAbsolutePath().toString();
    } 
    private File getAlbumDir(String album) {
        File storageDir = new File(
	    Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES
	        ), album
	    );	
        storageDir.mkdirs();
        return storageDir;
    } 
    private File getVideoDir(String album) {
        File storageDir = new File(
	    Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_MOVIES
	        ), album
	    );	
        storageDir.mkdirs();
        return storageDir;
    }
    
}
