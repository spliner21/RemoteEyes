package com.example.remoteeyes;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

public class DataSendActivity extends Activity {

	private DataContainer dcontainer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_send);
        
        Log.v("DataSend", "Pobieranie parent inent...");

        Intent parentIntent = getIntent();

        Log.v("DataSend", "Pobrano parent intent. Inicjowanie zmiennych.");
        dcontainer = new DataContainer();
        

        dcontainer.setFile1path(parentIntent.getStringExtra("file1path"));
        dcontainer.setFile2path(parentIntent.getStringExtra("file2path"));
        dcontainer.setGpsdata(parentIntent.getStringExtra("gpsdata"));

        Log.v("DataSend", "Zainicjowano dataContainer.");
        Log.v("DataSend", "Wartoœci: file1 = "+dcontainer.getFile1path()+", file2 = "+dcontainer.getFile2path()+", gps = "+dcontainer.getGpsdata()+".");
        Log.v("DataSend", "Pobieranie identyfikatora telefonu.");
        
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        String senderID = tm.getDeviceId();

        Log.v("DataSend", "Pobrano identyfikator telefonu: "+senderID+". Nawi¹zywanie po³¹czenia HTTP...");
        // Wysy³anie danych na serwer
        

        String urlServer = "http://89-25-169-28.huxnet.pl/re-webapp/send.php";	// TODO zmieniæ na adres z db

        sendFiles(urlServer,senderID);
     
    }
    
    private void sendFiles(String urlServer, String senderID) {

        try {
        	DefaultHttpClient mHttpClient;


            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            mHttpClient = new DefaultHttpClient(params);
	        Log.v("DataSend", "Ustawianie parametrów po³¹czenia: Wersja protoko³u: HTTP 1.1 . Ustawienia domyœlne.");
        	
        	HttpPost httppost = new HttpPost(urlServer);
	        Log.v("DataSend", "Ustawianie parametrów po³¹czenia: Adres url serwera = "+urlServer);

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); 
	        Log.v("DataSend", "Wysy³anie danych:"); 
            multipartEntity.addPart("gpsdata", new StringBody(dcontainer.getGpsdata()));
	        Log.v("DataSend", "Dane GPS: "+dcontainer.getGpsdata());
            multipartEntity.addPart("senderid", new StringBody(senderID));
	        Log.v("DataSend", "ID telefonu: "+senderID);
            multipartEntity.addPart("file1", new FileBody(new File(dcontainer.getFile1path())));
	        Log.v("DataSend", "Plik nr 1: "+dcontainer.getFile1path());
	        File f2 = new File(dcontainer.getFile2path());
	        if(f2 != null) {
	        	multipartEntity.addPart("file2", new FileBody(f2));
	        	Log.v("DataSend", "Plik nr 2: "+dcontainer.getFile2path());
	        }
            httppost.setEntity(multipartEntity);

            mHttpClient.execute(httppost, new FileUploadResponseHandler());
        	
        }
        catch (Exception ex) {
            Log.e("DataSendCrash", ex.getLocalizedMessage(), ex);
            }
    }
    
    private class FileUploadResponseHandler implements ResponseHandler<Object> {

        @Override
        public Object handleResponse(HttpResponse arg0)
                throws ClientProtocolException, IOException {

            HttpEntity r_entity = arg0.getEntity();
            String responseString = EntityUtils.toString(r_entity);
            Log.v("DataSend", "Wartoœæ zwrócona przez serwer: "+responseString);
            DBHelper dbh = new DBHelper(getBaseContext());
            
        	dbh.insertMessage(responseString,dcontainer);
        	
            Intent returnIntent = new Intent();
            returnIntent.putExtra("response",responseString);
    		setResult(RESULT_OK,returnIntent);     
    		
    		finish();
            return null;
        }

    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
